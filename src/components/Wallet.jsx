import React from "react";

function Wallet() {
  return (
    <div className="p-6 max-w-4xl mx-auto text-gray-800">
      {/* Title */}
      <h1 className="text-3xl font-bold mb-6 text-center">Wallet</h1>

      {/* Action buttons */}
      <div className="flex flex-col sm:flex-row flex-wrap justify-center gap-4 mb-6">
        <button className="bg-green-500 hover:bg-green-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Add Money (Deposit via Razorpay/Stripe)
        </button>
        <button className="bg-red-500 hover:bg-red-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Withdraw Money to Bank Account
        </button>
        <button className="bg-blue-500 hover:bg-blue-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Transfer Money (Wallet to Wallet)
        </button>
      </div>

      {/* Wallet information */}
      <div className="flex flex-col gap-2 text-center text-gray-700">
        <p>Track Wallet Transactions</p>
        <p>Handle Insufficient Balance Errors</p>
      </div>
    </div>
  );
}

export default Wallet;
