import React, { useState } from "react";

function ForgotPassword() {
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");

  const handleSendOTP = (e) => {
    e.preventDefault();
    // Handle sending OTP logic here
    console.log("Email for OTP:", email);
  };

  const handleVerifyOTP = (e) => {
    e.preventDefault();
    // Handle OTP verification and password reset here
    console.log("OTP entered:", otp);
  };

  return (
    <div className="p-6 max-w-md mx-auto mt-10 bg-white rounded-lg shadow-md">
      {/* Heading */}
      <h1 className="text-2xl font-bold mb-6 text-center">Forgot Password</h1>

      {/* Email input & send OTP */}
      <form className="flex flex-col gap-4 mb-4" onSubmit={handleSendOTP}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          required
        />
        <button
          type="submit"
          className="bg-teal-500 hover:bg-teal-600 text-white font-medium px-4 py-2 rounded-md transition duration-200"
        >
          Send OTP via Email
        </button>
      </form>

      {/* OTP input & verify */}
      <form className="flex flex-col gap-4" onSubmit={handleVerifyOTP}>
        <input
          type="text"
          placeholder="OTP"
          value={otp}
          onChange={(e) => setOtp(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          required
        />
        <button
          type="submit"
          className="bg-blue-500 hover:bg-blue-600 text-white font-medium px-4 py-2 rounded-md transition duration-200"
        >
          Verify OTP & Reset Password
        </button>
      </form>
    </div>
  );
}

export default ForgotPassword;
