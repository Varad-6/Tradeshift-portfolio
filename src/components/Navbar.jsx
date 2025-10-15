// src/components/Navbar.jsx
import React from 'react';
import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="bg-gray-800 text-white shadow-md">
      <div className="max-w-6xl mx-auto px-4 py-3 flex justify-between items-center">
        {/* Logo / Title */}
        <Link to="/" className="text-2xl font-bold hover:text-teal-300 transition">
          Crypto Trading Platform
        </Link>

        {/* Navigation Links */}
        <div className="flex space-x-4">
          <Link
            to="/portfolio"
            className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 transition"
          >
            Portfolio
          </Link>
          <Link
            to="/wallet"
            className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 transition"
          >
            Wallet
          </Link>
          <Link
            to="/watchlist"
            className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 transition"
          >
            Watchlist
          </Link>
          <Link
            to="/login"
            className="px-3 py-2 rounded-md text-sm font-medium hover:bg-gray-700 transition"
          >
            Login
          </Link>
          <Link
            to="/signup"
            className="bg-teal-500 hover:bg-teal-600 text-white px-3 py-2 rounded-md text-sm font-medium transition"
          >
            Sign Up
          </Link>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
