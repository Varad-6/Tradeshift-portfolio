import React, { createContext, useContext, useState, useEffect } from 'react';
import { authAPI, userAPI } from '../services/api';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  // Check if user is logged in on app start
  useEffect(() => {
    checkAuthStatus();
  }, []);

  const checkAuthStatus = async () => {
    try {
      const token = localStorage.getItem('jwt');
      if (token) {
        const userData = await userAPI.getProfile();
        setUser(userData);
        setIsAuthenticated(true);
      }
    } catch (error) {
      console.error('Auth check failed:', error);
      localStorage.removeItem('jwt');
      localStorage.removeItem('user');
    } finally {
      setLoading(false);
    }
  };

  const login = async (credentials) => {
    try {
      const response = await authAPI.login(credentials);
      
      if (response.jwt) {
        localStorage.setItem('jwt', response.jwt);
        const userData = await userAPI.getProfile();
        setUser(userData);
        setIsAuthenticated(true);
        return { success: true, data: response };
      } else if (response.twoFactorAuthEnabled) {
        // 2FA is enabled, return session info
        return { 
          success: true, 
          requires2FA: true, 
          sessionId: response.session,
          message: response.message 
        };
      }
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Login failed' 
      };
    }
  };

  const verify2FA = async (otp, sessionId) => {
    try {
      const response = await authAPI.verifyOTP(otp, sessionId);
      if (response.jwt) {
        localStorage.setItem('jwt', response.jwt);
        const userData = await userAPI.getProfile();
        setUser(userData);
        setIsAuthenticated(true);
        return { success: true, data: response };
      }
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'OTP verification failed' 
      };
    }
  };

  const signup = async (userData) => {
    try {
      const response = await authAPI.signup(userData);
      if (response.jwt) {
        localStorage.setItem('jwt', response.jwt);
        const userProfile = await userAPI.getProfile();
        setUser(userProfile);
        setIsAuthenticated(true);
        return { success: true, data: response };
      }
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.message || 'Registration failed' 
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('jwt');
    localStorage.removeItem('user');
    setUser(null);
    setIsAuthenticated(false);
  };

  const value = {
    user,
    loading,
    isAuthenticated,
    login,
    signup,
    verify2FA,
    logout,
    checkAuthStatus
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
