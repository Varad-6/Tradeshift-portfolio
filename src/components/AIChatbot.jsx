import React, { useState, useEffect } from "react";
import { LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, ResponsiveContainer } from "recharts";

function AIChatbot() {
  const [query, setQuery] = useState("");
  const [answer, setAnswer] = useState("");
  const [coins, setCoins] = useState([]);
  const [selectedCoin, setSelectedCoin] = useState("");
  const [chartData, setChartData] = useState([]);

  // Fetch top coins
  useEffect(() => {
    const fetchCoins = async () => {
      try {
        const res = await fetch(
          "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=20&page=1&sparkline=false"
        );
        const data = await res.json();
        setCoins(data);
      } catch (err) {
        console.error("Error fetching coins:", err);
      }
    };
    fetchCoins();
  }, []);

  const handleAsk = async (e) => {
    e.preventDefault();

    let coinId = selectedCoin;

    // If typed query
    if (!coinId && query) {
      coinId = query
        .toLowerCase()
        .replace(/current price of|price of|how much is|in usd|\?/gi, "")
        .trim()
        .replace(/\s+/g, "-");
    }

    if (!coinId) return;

    try {
      // Fetch current price
      const priceRes = await fetch(
        `https://api.coingecko.com/api/v3/simple/price?ids=${coinId}&vs_currencies=usd`
      );
      const priceData = await priceRes.json();

      if (priceData[coinId] && priceData[coinId].usd !== undefined) {
        setAnswer(`💰 Current price of ${coinId.replace("-", " ")}: $${priceData[coinId].usd}`);
      } else {
        setAnswer("❌ Sorry, no data available for that coin.");
      }

      // Fetch 7-day price history
      const historyRes = await fetch(
        `https://api.coingecko.com/api/v3/coins/${coinId}/market_chart?vs_currency=usd&days=7&interval=daily`
      );
      const historyData = await historyRes.json();

      if (historyData.prices) {
        const formattedData = historyData.prices.map(([timestamp, price]) => {
          const date = new Date(timestamp);
          return { date: `${date.getMonth() + 1}/${date.getDate()}`, price: price.toFixed(2) };
        });
        setChartData(formattedData);
      } else {
        setChartData([]);
      }
    } catch (error) {
      console.error(error);
      setAnswer("⚠️ Error fetching data. Try again later.");
      setChartData([]);
    }

    setQuery("");
    setSelectedCoin("");
  };

  return (
    <div className="p-6 max-w-lg mx-auto mt-10 bg-white rounded-lg shadow-md text-gray-800">
      <h1 className="text-2xl font-bold mb-4 text-center">AI Chatbot</h1>

      <div className="mb-4">
        <label className="block mb-1 font-medium">Select a coin:</label>
        <select
          value={selectedCoin}
          onChange={(e) => setSelectedCoin(e.target.value)}
          className="w-full px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
        >
          <option value="">-- Choose a coin --</option>
          {coins.map((coin) => (
            <option key={coin.id} value={coin.id}>
              {coin.name}
            </option>
          ))}
        </select>
      </div>

      <form className="flex flex-col gap-4 mb-4" onSubmit={handleAsk}>
        <input
          type="text"
          placeholder="Or type a crypto question (e.g., What is Bitcoin price?)"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          className="px-4 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-teal-500"
        />
        <button
          type="submit"
          className="bg-teal-500 hover:bg-teal-600 text-white font-medium px-4 py-2 rounded-md transition duration-200"
        >
          Ask
        </button>
      </form>

      {answer && <p className="text-center text-lg mt-4 font-medium">{answer}</p>}

      {chartData.length > 0 && (
        <div className="mt-6">
          <h2 className="text-center font-medium mb-2">7-Day Price Trend</h2>
          <ResponsiveContainer width="100%" height={250}>
            <LineChart data={chartData}>
              <CartesianGrid strokeDasharray="3 3" />
              <XAxis dataKey="date" />
              <YAxis />
              <Tooltip />
              <Line type="monotone" dataKey="price" stroke="#8884d8" />
            </LineChart>
          </ResponsiveContainer>
        </div>
      )}

      <p className="text-gray-600 text-center mt-6">
        Provides Real-Time Data via CoinGecko API
      </p>
    </div>
  );
}

export default AIChatbot;
