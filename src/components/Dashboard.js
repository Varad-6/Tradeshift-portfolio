import React, { useState, useEffect } from 'react';
import {
  Container,
  Paper,
  Typography,
  Box,
  Button,
  Grid,
  Card,
  CardContent,
  Avatar,
  Chip,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem
} from '@mui/material';
import {
  Person,
  Security,
  Email,
  Phone,
  Logout,
  Settings
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import { userAPI } from '../services/api';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');
  const [show2FADialog, setShow2FADialog] = useState(false);
  const [showForgotPasswordDialog, setShowForgotPasswordDialog] = useState(false);
  const [verificationType, setVerificationType] = useState('EMAIL');
  const [otp, setOtp] = useState('');
  const [sessionId, setSessionId] = useState('');

  const handleLogout = () => {
    logout();
  };

  const handleEnable2FA = async () => {
    setLoading(true);
    setError('');
    setMessage('');

    try {
      await userAPI.sendVerificationOTP(verificationType.toLowerCase());
      setMessage('OTP sent successfully! Please check your email/mobile.');
      setShow2FADialog(true);
    } catch (err) {
      setError('Failed to send OTP. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleVerify2FA = async () => {
    setLoading(true);
    setError('');

    try {
      const result = await userAPI.enable2FA(otp);
      setMessage('Two-Factor Authentication enabled successfully!');
      setShow2FADialog(false);
      setOtp('');
      // Refresh user data to show updated 2FA status
      window.location.reload();
    } catch (err) {
      setError('Invalid OTP. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleForgotPassword = async () => {
    setLoading(true);
    setError('');
    setMessage('');

    try {
      const result = await userAPI.sendForgotPasswordOTP(user.email, verificationType);
      setMessage('Password reset OTP sent successfully!');
      setSessionId(result.session);
      setShowForgotPasswordDialog(true);
    } catch (err) {
      setError('Failed to send password reset OTP. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    const newPassword = e.target.newPassword.value;
    const confirmPassword = e.target.confirmPassword.value;

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
      setMessage('Password updated successfully!');
      setShowForgotPasswordDialog(false);
      setOtp('');
    } catch (err) {
      setError('Failed to reset password. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (!user) {
    return (
      <Container>
        <Typography>Loading...</Typography>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom>
          Welcome to TradeShift Portfolio
        </Typography>
        <Typography variant="subtitle1" color="text.secondary">
          Manage your account and security settings
        </Typography>
      </Box>

      {message && (
        <Alert severity="success" sx={{ mb: 3 }} onClose={() => setMessage('')}>
          {message}
        </Alert>
      )}

      {error && (
        <Alert severity="error" sx={{ mb: 3 }} onClose={() => setError('')}>
          {error}
        </Alert>
      )}

      <Grid container spacing={3}>
        {/* User Profile Card */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: 'primary.main', mr: 2 }}>
                  <Person />
                </Avatar>
                <Box>
                  <Typography variant="h6">{user.fullName}</Typography>
                  <Chip 
                    label={user.role} 
                    color={user.role === 'ADMIN' ? 'secondary' : 'primary'}
                    size="small"
                  />
                </Box>
              </Box>
              
              <Box sx={{ mt: 2 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                  <Email sx={{ mr: 1, fontSize: 20 }} />
                  <Typography variant="body2">{user.email}</Typography>
                </Box>
                
                {user.mobile && (
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                    <Phone sx={{ mr: 1, fontSize: 20 }} />
                    <Typography variant="body2">{user.mobile}</Typography>
                  </Box>
                )}
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Security Settings Card */}
        <Grid item xs={12} md={6}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Security sx={{ mr: 1 }} />
                <Typography variant="h6">Security Settings</Typography>
              </Box>
              
              <Box sx={{ mb: 2 }}>
                <Typography variant="body2" gutterBottom>
                  Two-Factor Authentication: 
                  <Chip 
                    label={user.twoFactorAuth?.isEnabled ? 'Enabled' : 'Disabled'}
                    color={user.twoFactorAuth?.isEnabled ? 'success' : 'default'}
                    size="small"
                    sx={{ ml: 1 }}
                  />
                </Typography>
              </Box>

              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                {!user.twoFactorAuth?.isEnabled && (
                  <Button
                    variant="outlined"
                    size="small"
                    onClick={handleEnable2FA}
                    disabled={loading}
                    startIcon={<Security />}
                  >
                    Enable 2FA
                  </Button>
                )}
                
                <Button
                  variant="outlined"
                  size="small"
                  onClick={handleForgotPassword}
                  disabled={loading}
                  startIcon={<Settings />}
                >
                  Reset Password
                </Button>
              </Box>
            </CardContent>
          </Card>
        </Grid>

        {/* Quick Actions Card */}
        <Grid item xs={12}>
          <Card>
            <CardContent>
              <Typography variant="h6" gutterBottom>
                Quick Actions
              </Typography>
              <Box sx={{ display: 'flex', gap: 2, flexWrap: 'wrap' }}>
                <Button
                  variant="contained"
                  color="primary"
                  startIcon={<Settings />}
                >
                  Account Settings
                </Button>
                <Button
                  variant="outlined"
                  color="error"
                  startIcon={<Logout />}
                  onClick={handleLogout}
                >
                  Logout
                </Button>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* 2FA Verification Dialog */}
      <Dialog open={show2FADialog} onClose={() => setShow2FADialog(false)}>
        <DialogTitle>Enable Two-Factor Authentication</DialogTitle>
        <DialogContent>
          <Typography variant="body2" sx={{ mb: 2 }}>
            Please enter the OTP sent to your {verificationType.toLowerCase()}:
          </Typography>
          <TextField
            autoFocus
            margin="dense"
            label="Enter OTP"
            fullWidth
            variant="outlined"
            value={otp}
            onChange={(e) => setOtp(e.target.value)}
            inputProps={{ maxLength: 6 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShow2FADialog(false)}>Cancel</Button>
          <Button 
            onClick={handleVerify2FA} 
            variant="contained"
            disabled={loading || otp.length !== 6}
          >
            {loading ? 'Verifying...' : 'Verify & Enable'}
          </Button>
        </DialogActions>
      </Dialog>

      {/* Forgot Password Dialog */}
      <Dialog open={showForgotPasswordDialog} onClose={() => setShowForgotPasswordDialog(false)}>
        <DialogTitle>Reset Password</DialogTitle>
        <DialogContent>
          <form onSubmit={handleResetPassword}>
            <Typography variant="body2" sx={{ mb: 2 }}>
              Please enter the OTP sent to your {verificationType.toLowerCase()} and your new password:
            </Typography>
            <TextField
              autoFocus
              margin="dense"
              label="Enter OTP"
              fullWidth
              variant="outlined"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              inputProps={{ maxLength: 6 }}
              sx={{ mb: 2 }}
            />
            <TextField
              margin="dense"
              label="New Password"
              type="password"
              fullWidth
              variant="outlined"
              name="newPassword"
              sx={{ mb: 2 }}
            />
            <TextField
              margin="dense"
              label="Confirm Password"
              type="password"
              fullWidth
              variant="outlined"
              name="confirmPassword"
            />
            <DialogActions sx={{ mt: 2, p: 0 }}>
              <Button onClick={() => setShowForgotPasswordDialog(false)}>Cancel</Button>
              <Button 
                type="submit"
                variant="contained"
                disabled={loading || otp.length !== 6}
              >
                {loading ? 'Updating...' : 'Update Password'}
              </Button>
            </DialogActions>
          </form>
        </DialogContent>
      </Dialog>
    </Container>
  );
};

export default Dashboard;
