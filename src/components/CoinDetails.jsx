import React from "react";
import { useParams } from "react-router-dom";

function CoinDetails() {
  // useParams is the modern way to access route params (replaces match.params)
  const { id } = useParams();

  return (
    <div className="p-6 max-w-4xl mx-auto text-gray-800">
      {/* Heading */}
      <h1 className="text-3xl font-bold mb-4 text-center">
        {id} Details
      </h1>

      {/* Description */}
      <p className="text-lg text-gray-700 mb-4 text-center">
        Current Price, Market Cap, and Change Percentages
      </p>

      {/* Placeholder for detailed coin data */}
      <div className="mt-6 p-6 bg-gray-100 rounded-lg shadow-inner border border-gray-200">
        <div className="h-48 flex items-center justify-center text-gray-400 border-2 border-dashed border-gray-300 rounded-lg">
          Detailed coin data will appear here
        </div>
      </div>
    </div>
  );
}

export default CoinDetails;
