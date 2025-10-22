import React, { useState } from "react";

function AIChatbot() {
  const [query, setQuery] = useState("");

  const handleAsk = (e) => {
    e.preventDefault();
    // Handle API query logic here
    console.log("User query:", query);
  };

  return (
    <div className="p-6 max-w-md mx-auto mt-10 bg-white rounded-lg shadow-md text-gray-800">
      {/* Heading */}
      <h1 className="text-2xl font-bold mb-4 text-center">AI Chatbot</h1>

      {/* Input and Ask button */}
      <form className="flex flex-col gap-4 mb-4" onSubmit={handleAsk}>
        <input
          type="text"
          placeholder="Ask Crypto Questions (e.g., Bitcoin Price)"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
          required
        />
        <button
          type="submit"
          className="bg-teal-500 hover:bg-teal-600 text-white font-medium px-4 py-2 rounded-md transition duration-200"
        >
          Ask
        </button>
      </form>

      {/* Info text */}
      <p className="text-gray-600 text-center">
        Provides Real-Time Data via CoinGecko API
      </p>
    </div>
  );
}

export default AIChatbot;
