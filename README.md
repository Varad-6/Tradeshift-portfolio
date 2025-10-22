# TradeShift Portfolio Frontend

A modern, enterprise-grade financial portfolio management platform built with React and Material-UI. This frontend provides a comprehensive solution for investors to track multi-brokerage portfolios, analyze real-time market data, and execute trades securely.

## 🚀 Features

### Authentication & Security
- **Secure Authentication**: Login and Registration with JWT tokens
- **Two-Factor Authentication**: Email OTP verification for enhanced security
- **Password Reset**: Complete forgot password flow with OTP verification
- **Role-based Access Control**: Support for USER and ADMIN roles
- **Session Management**: Automatic token refresh and secure logout

### Portfolio Management
- **Real-time Portfolio Overview**: Live portfolio value, gains/losses, and performance metrics
- **Holdings Management**: View and manage current stock positions
- **Watchlist**: Track stocks of interest with real-time price updates
- **Trading Interface**: Place buy/sell orders with multiple order types
- **Analytics Dashboard**: Portfolio performance charts and risk analysis

### User Experience
- **Modern UI Design**: Professional financial platform aesthetics
- **Responsive Design**: Works seamlessly on desktop, tablet, and mobile
- **Real-time Updates**: Live market data and portfolio updates
- **Interactive Charts**: Portfolio performance and asset allocation visualizations
- **Intuitive Navigation**: Tab-based interface for easy feature access

## Project Structure

```
src/
├── components/          # React components
│   ├── Login.js        # Login form with 2FA support
│   ├── Signup.js       # User registration form
│   ├── Dashboard.js    # Main dashboard with user profile
│   └── ProtectedRoute.js # Route protection component
├── contexts/           # React Context for state management
│   └── AuthContext.js  # Authentication state management
├── services/           # API service layer
│   └── api.js         # Backend API integration
└── App.js             # Main application component
```

## Backend Integration

The frontend connects to your Spring Boot backend with these endpoints:

### Authentication Endpoints
- `POST /auth/signup` - User registration
- `POST /auth/login` - User login
- `POST /auth/verify-signin-otp/{otp}` - 2FA verification

### User Management Endpoints
- `GET /auth/api/user/profile` - Get user profile
- `POST /auth/api/user/verification/{type}/send-otp` - Send verification OTP
- `PATCH /auth/api/user/enable-2fa/verify-opt/{otp}` - Enable 2FA
- `POST /auth/api/user/reset-password/send-otp` - Send password reset OTP
- `PATCH /auth/users/reset-password/verify-otp` - Reset password

## Installation & Setup

1. **Install Dependencies**:
   ```bash
   npm install
   ```

2. **Start Backend** (from your Spring Boot project):
   ```bash
   mvn spring-boot:run
   ```

3. **Start Frontend**:
   ```bash
   npm start
   ```

4. **Access Application**:
   - Frontend: http://localhost:3000
   - Backend: http://localhost:8080

## How to Use

### 1. User Registration
- Go to `/signup`
- Fill in: Full Name, Email, Password, Mobile, Role
- Click "Create Account"
- User is automatically logged in after registration

### 2. User Login
- Go to `/login`
- Enter email and password
- If 2FA is enabled, enter OTP sent to email
- Redirected to dashboard after successful login

### 3. Dashboard Features
- **View Profile**: See user information and role
- **Enable 2FA**: Set up two-factor authentication
- **Reset Password**: Change password with OTP verification
- **Logout**: Sign out of the application

## Key Components Explained

### AuthContext.js
- Manages global authentication state
- Handles login, signup, logout, and 2FA verification
- Automatically adds JWT tokens to API requests
- Provides authentication status to all components

### api.js
- Centralized API service for backend communication
- Handles JWT token management
- Automatic token refresh and error handling
- Organized API functions for different features

### Login.js
- Login form with email/password
- 2FA OTP verification flow
- Error handling and loading states
- Automatic redirect to dashboard

### Signup.js
- User registration form
- Form validation (password length, mobile format)
- Role selection (USER/ADMIN)
- Auto-login after successful registration

### Dashboard.js
- User profile display
- Security settings management
- 2FA enable/disable functionality
- Password reset with OTP verification
- Interactive dialogs for user actions

## Security Features

- **JWT Token Management**: Automatic token handling and refresh
- **Route Protection**: Protected routes require authentication
- **2FA Support**: Two-factor authentication with email OTP
- **Password Security**: Secure password reset flow
- **Role-based Access**: Different features for USER vs ADMIN

## Customization

### Styling
- Uses Material-UI theme system
- Custom colors and typography in App.js
- Responsive design for all screen sizes

### API Configuration
- Backend URL in `services/api.js`
- Easy to change for different environments

### Adding New Features
- Add new API functions in `services/api.js`
- Create new components in `components/` folder
- Add new routes in `App.js`

## Troubleshooting

### Common Issues
1. **CORS Errors**: Make sure your Spring Boot backend allows CORS from localhost:3000
2. **API Connection**: Verify backend is running on port 8080
3. **JWT Issues**: Check if JWT tokens are being stored in localStorage

### Development Tips
- Use browser developer tools to check network requests
- Check console for any JavaScript errors
- Verify API responses match expected format

## Next Steps

1. Install dependencies: `npm install`
2. Start your Spring Boot backend
3. Start the frontend: `npm start`
4. Test the complete authentication flow
5. Customize styling and add more features as needed