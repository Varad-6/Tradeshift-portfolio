import axios from 'axios';

// Base URL for your backend API
const API_BASE_URL = 'http://localhost:8081';

// Create axios instance with default config
const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request interceptor to add JWT token to requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwt');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Response interceptor to handle token expiration
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('jwt');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Authentication API calls
export const authAPI = {
  // User registration
  signup: async (userData) => {
    const response = await api.post('/auth/signup', userData);
    return response.data;
  },

  // User login
  login: async (credentials) => {
    const response = await api.post('/auth/signin', credentials);
    return response.data;
  },

  // Verify 2FA OTP
  verifyOTP: async (otp, sessionId) => {
    const response = await api.post(`/auth/verify-signin-otp/${otp}?id=${sessionId}`);
    return response.data;
  },
};

// User API calls
export const userAPI = {
  // Get user profile
  getProfile: async () => {
    const response = await api.get('/auth/api/user/profile');
    return response.data;
  },

  // Send verification OTP
  sendVerificationOTP: async (verificationType) => {
    const response = await api.post(`/auth/api/user/verification/${verificationType}/send-otp`);
    return response.data;
  },

  // Enable 2FA
  enable2FA: async (otp) => {
    const response = await api.patch(`/auth/users/enable-2fa/verify-otp/${otp}`);
    return response.data;
  },

  // Send forgot password OTP (public endpoint - no auth required)
  sendForgotPasswordOTP: async (email, verificationType) => {
    const response = await axios.post(`${API_BASE_URL}/auth/users/reset-password/send-otp`, {
      sendTo: email,
      verificationType: verificationType
    });
    return response.data;
  },

  // Reset password (public endpoint - no auth required)
  resetPassword: async (sessionId, otp, newPassword) => {
    const response = await axios.patch(`${API_BASE_URL}/auth/users/reset-password/verify-otp`, {
      otp: otp,
      password: newPassword
    }, {
      params: { id: sessionId }
    });
    return response.data;
  },
};

export default api;
