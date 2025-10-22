import React, { useState } from 'react';
import {
  Container,
  Paper,
  TextField,
  Button,
  Typography,
  Box,
  Alert,
  CircularProgress,
  Link
} from '@mui/material';
import { useAuth } from '../contexts/AuthContext';
import { useNavigate, Link as RouterLink } from 'react-router-dom';

const Login = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    twoFactorAuth: { isEnabled: false }
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [show2FA, setShow2FA] = useState(false);
  const [otp, setOtp] = useState('');
  const [sessionId, setSessionId] = useState('');

  const { login, verify2FA } = useAuth();
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const result = await login(formData);
      
      if (result.success) {
        if (result.requires2FA) {
          setShow2FA(true);
          setSessionId(result.sessionId);
          setError('');
        } else {
          navigate('/dashboard');
        }
      } else {
        setError(result.error);
      }
    } catch (err) {
      setError('An unexpected error occurred');
    } finally {
      setLoading(false);
    }
  };

  const handle2FAVerify = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const result = await verify2FA(otp, sessionId);
      
      if (result.success) {
        navigate('/dashboard');
      } else {
        setError(result.error);
      }
    } catch (err) {
      setError('OTP verification failed');
    } finally {
      setLoading(false);
    }
  };

  if (show2FA) {
    return (
      <Container component="main" maxWidth="xs">
        <Box
          sx={{
            marginTop: 8,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          <Paper elevation={3} sx={{ padding: 4, width: '100%' }}>
            <Typography component="h1" variant="h5" align="center" gutterBottom>
              Two-Factor Authentication
            </Typography>
            <Typography variant="body2" align="center" color="text.secondary" sx={{ mb: 3 }}>
              Please enter the OTP sent to your email
            </Typography>
            
            {error && (
              <Alert severity="error" sx={{ mb: 2 }}>
                {error}
              </Alert>
            )}

            <Box component="form" onSubmit={handle2FAVerify} sx={{ mt: 1 }}>
              <TextField
                margin="normal"
                required
                fullWidth
                id="otp"
                label="Enter OTP"
                name="otp"
                value={otp}
                onChange={(e) => setOtp(e.target.value)}
                autoComplete="off"
                inputProps={{ maxLength: 6 }}
              />
              
              <Button
                type="submit"
                fullWidth
                variant="contained"
                sx={{ mt: 3, mb: 2 }}
                disabled={loading || otp.length !== 6}
              >
                {loading ? <CircularProgress size={24} /> : 'Verify OTP'}
              </Button>

              <Button
                fullWidth
                variant="text"
                onClick={() => setShow2FA(false)}
                sx={{ mt: 1 }}
              >
                Back to Login
              </Button>
            </Box>
          </Paper>
        </Box>
      </Container>
    );
  }

  return (
    <Container component="main" maxWidth="xs">
      <Box
        sx={{
          marginTop: 8,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Paper 
          elevation={8} 
          sx={{ 
            padding: 4, 
            width: '100%',
            background: 'linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)',
            border: '1px solid #e2e8f0'
          }}
        >
          <Box sx={{ textAlign: 'center', mb: 3 }}>
            <Typography 
              component="h1" 
              variant="h4" 
              gutterBottom
              sx={{ 
                background: 'linear-gradient(135deg, #1e3a8a 0%, #3b82f6 100%)',
                backgroundClip: 'text',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
                fontWeight: 700
              }}
            >
              TradeShift Portfolio
            </Typography>
            <Typography component="h2" variant="h5" gutterBottom color="text.secondary">
              Welcome Back
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Sign in to access your portfolio dashboard
            </Typography>
          </Box>
          
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              id="email"
              label="Email Address"
              name="email"
              autoComplete="email"
              autoFocus
              value={formData.email}
              onChange={handleChange}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              name="password"
              label="Password"
              type="password"
              id="password"
              autoComplete="current-password"
              value={formData.password}
              onChange={handleChange}
            />
            
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={loading}
            >
              {loading ? <CircularProgress size={24} /> : 'Sign In'}
            </Button>
            
            <Box textAlign="center" sx={{ mt: 2 }}>
              <Link component={RouterLink} to="/signup" variant="body2" sx={{ display: 'block', mb: 1 }}>
                Don't have an account? Sign Up
              </Link>
              <Link component={RouterLink} to="/forgot-password" variant="body2">
                Forgot your password?
              </Link>
            </Box>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default Login;
