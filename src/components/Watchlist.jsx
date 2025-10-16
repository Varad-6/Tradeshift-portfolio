import React from "react";

function Watchlist() {
  return (
    <div className="p-6 max-w-4xl mx-auto text-gray-800">
      {/* Title */}
      <h1 className="text-3xl font-bold mb-4 text-center">Watchlist</h1>

      {/* Tracked coins */}
      <p className="text-lg mb-6 text-gray-700 text-center">
        Track: <span className="font-semibold">Bitcoin, Ethereum</span>
      </p>

      {/* Action buttons */}
      <div className="flex flex-wrap justify-center gap-4">
        <button className="bg-blue-500 hover:bg-blue-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Add Coin
        </button>
        <button className="bg-red-500 hover:bg-red-600 text-white px-5 py-2 rounded-md font-medium transition duration-200">
          Remove Coin
        </button>
      </div>
    </div>
  );
}

export default Watchlist;
