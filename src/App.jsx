import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Navbar from './components/Navbar';
import Homepage from './components/Homepage';
import CoinDetails from './components/CoinDetails';
import Portfolio from './components/Portfolio';
import Watchlist from './components/Watchlist';
import ActivityHistory from './components/ActivityHistory';
import Wallet from './components/Wallet';
import PaymentDetails from './components/PaymentDetails';
import SignUp from './components/auth/SignUp';
import Login from './components/auth/Login';
import ForgotPassword from './components/auth/ForgotPassword';
import Profile from './components/auth/Profile';
import AIChatbot from './components/AIChatbot';

// ✅ Correct import for your setup
import Dashboard from './components/Dashboard';

function App() {
  return (
    <Router>
      <Navbar />
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/coin/:id" element={<CoinDetails />} />
        <Route path="/portfolio" element={<Portfolio />} />
        <Route path="/watchlist" element={<Watchlist />} />
        <Route path="/activity" element={<ActivityHistory />} />
        <Route path="/wallet" element={<Wallet />} />
        <Route path="/payment-details" element={<PaymentDetails />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/login" element={<Login />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="/chatbot" element={<AIChatbot />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
