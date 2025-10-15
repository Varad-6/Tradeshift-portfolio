import React, { useState } from "react";

function SignUp() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSignUp = (e) => {
    e.preventDefault();
    // Handle signup logic here
    console.log("Email:", email, "Password:", password);
  };

  return (
    <div className="p-6 max-w-md mx-auto mt-10 bg-white rounded-lg shadow-md">
      {/* Heading */}
      <h1 className="text-2xl font-bold mb-6 text-center">Register</h1>

      {/* Form */}
      <form className="flex flex-col gap-4" onSubmit={handleSignUp}>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          required
        />

        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          required
        />

        <button
          type="submit"
          className="bg-teal-500 hover:bg-teal-600 text-white font-medium px-4 py-2 rounded-md transition duration-200"
        >
          Sign Up
        </button>
      </form>
    </div>
  );
}

export default SignUp;
