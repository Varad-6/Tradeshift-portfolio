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
  Link,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Stepper,
  Step,
  StepLabel
} from '@mui/material';
import { useNavigate, Link as RouterLink } from 'react-router-dom';
import { userAPI } from '../services/api';

const ForgotPassword = () => {
  const [activeStep, setActiveStep] = useState(0);
  const [formData, setFormData] = useState({
    email: '',
    verificationType: 'EMAIL'
  });
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [sessionId, setSessionId] = useState('');

  const navigate = useNavigate();

  const steps = ['Enter Email', 'Verify OTP', 'Reset Password'];

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSendOTP = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    setSuccess('');

    try {
      const result = await userAPI.sendForgotPasswordOTP(formData.email, formData.verificationType);
      setSessionId(result.session);
      setSuccess('OTP sent successfully! Please check your email.');
      setActiveStep(1);
    } catch (err) {
      setError('Failed to send OTP. Please check your email and try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerifyOTP = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (otp.length !== 6) {
      setError('Please enter a valid 6-digit OTP');
      setLoading(false);
      return;
    }

    try {
      // For now, just move to next step. In real implementation, you'd verify OTP here
      setSuccess('OTP verified successfully!');
      setActiveStep(2);
    } catch (err) {
      setError('Invalid OTP. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    if (newPassword !== confirmPassword) {
      setError('Passwords do not match');
      setLoading(false);
      return;
    }

    if (newPassword.length < 6) {
      setError('Password must be at least 6 characters long');
      setLoading(false);
      return;
    }

    try {
      await userAPI.resetPassword(sessionId, otp, newPassword);
      setSuccess('Password reset successfully! Redirecting to login...');
      setTimeout(() => {
        navigate('/login');
      }, 2000);
    } catch (err) {
      setError('Failed to reset password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const renderStepContent = (step) => {
    switch (step) {
      case 0:
        return (
          <Box component="form" onSubmit={handleSendOTP} sx={{ mt: 1 }}>
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
            
            <FormControl fullWidth margin="normal">
              <InputLabel id="verification-type-label">Verification Method</InputLabel>
              <Select
                labelId="verification-type-label"
                id="verificationType"
                name="verificationType"
                value={formData.verificationType}
                label="Verification Method"
                onChange={handleChange}
              >
                <MenuItem value="EMAIL">Email</MenuItem>
                <MenuItem value="MOBILE">Mobile</MenuItem>
              </Select>
            </FormControl>
            
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={loading}
            >
              {loading ? <CircularProgress size={24} /> : 'Send OTP'}
            </Button>
          </Box>
        );

      case 1:
        return (
          <Box component="form" onSubmit={handleVerifyOTP} sx={{ mt: 1 }}>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Please enter the OTP sent to your {formData.verificationType.toLowerCase()}:
            </Typography>
            
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
          </Box>
        );

      case 2:
        return (
          <Box component="form" onSubmit={handleResetPassword} sx={{ mt: 1 }}>
            <TextField
              margin="normal"
              required
              fullWidth
              name="newPassword"
              label="New Password"
              type="password"
              id="newPassword"
              autoComplete="new-password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              helperText="Password must be at least 6 characters"
            />
            
            <TextField
              margin="normal"
              required
              fullWidth
              name="confirmPassword"
              label="Confirm New Password"
              type="password"
              id="confirmPassword"
              autoComplete="new-password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            
            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2 }}
              disabled={loading}
            >
              {loading ? <CircularProgress size={24} /> : 'Reset Password'}
            </Button>
          </Box>
        );

      default:
        return null;
    }
  };

  return (
    <Container component="main" maxWidth="sm">
      <Box
        sx={{
          marginTop: 4,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
        }}
      >
        <Paper elevation={3} sx={{ padding: 4, width: '100%' }}>
          <Typography component="h1" variant="h4" align="center" gutterBottom>
            TradeShift Portfolio
          </Typography>
          <Typography component="h2" variant="h5" align="center" gutterBottom>
            Reset Password
          </Typography>
          
          <Stepper activeStep={activeStep} sx={{ mb: 4 }}>
            {steps.map((label) => (
              <Step key={label}>
                <StepLabel>{label}</StepLabel>
              </Step>
            ))}
          </Stepper>
          
          {error && (
            <Alert severity="error" sx={{ mb: 2 }}>
              {error}
            </Alert>
          )}

          {success && (
            <Alert severity="success" sx={{ mb: 2 }}>
              {success}
            </Alert>
          )}

          {renderStepContent(activeStep)}
          
          <Box textAlign="center" sx={{ mt: 2 }}>
            <Link component={RouterLink} to="/login" variant="body2">
              Back to Login
            </Link>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default ForgotPassword;
