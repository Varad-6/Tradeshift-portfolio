import React from "react";

function Portfolio() {
  return (
    <div className="p-6 max-w-4xl mx-auto text-gray-800">
      {/* Title */}
      <h1 className="text-3xl font-bold mb-4 text-center">Portfolio</h1>

      {/* Assets list */}
      <p className="text-lg mb-6 text-gray-700 text-center">
        Assets: <span className="font-semibold">Bitcoin, Ethereum, Tether, BNB</span>
      </p>

      {/* Buttons */}
      <div className="flex flex-wrap justify-center gap-4 mb-6">
        <button className="bg-green-500 hover:bg-green-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Buy Cryptocurrency
        </button>
        <button className="bg-red-500 hover:bg-red-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Sell Cryptocurrency
        </button>
      </div>

      {/* Validation note */}
      <p className="text-gray-600 italic text-center">
        Validation for Insufficient Balance
      </p>
    </div>
  );
}

export default Portfolio;
