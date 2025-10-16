import React, { useState } from "react";
import { Link } from "react-router-dom";
import { FaCommentDots } from "react-icons/fa";
import BTC from '../assets/img/BTC.png';
import ETH from '../assets/img/ETH.png';
import Tether from '../assets/img/Tether.webp';
import BNB from '../assets/img/BNB.webp';
import Cardano from '../assets/img/Cardano.webp';
import Doge from '../assets/img/Doge.webp';
import Solana from '../assets/img/Solana.webp';
import tron from '../assets/img/tron.webp';
import USDC from '../assets/img/USDC.webp';
import XRP from '../assets/img/XRP.webp';

const coinsData = [
  { name: "Bitcoin", symbol: "BTC", volume: "26,406,060,693", marketCap: "13,250,820,740", change: "-3.0802%", changeColor: "text-red-500", price: "67,163", icon: BTC },
  { name: "Ethereum", symbol: "ETH", volume: "1,502,819,489", marketCap: "42,588,154,065", change: "-4.0887%", changeColor: "text-red-500", price: "3,539.74", icon: ETH },
  { name: "Tether", symbol: "USDT", volume: "45,670,914,886", marketCap: "11,246,138,288", change: "0.2398%", changeColor: "text-green-500", price: "0.999928", icon: Tether },
  { name: "BNB", symbol: "BNB", volume: "7,890,000,000", marketCap: "164,140,000,000", change: "+1.31%", changeColor: "text-green-500", price: "1,179.30", icon: BNB },
  { name: "XRP", symbol: "XRP", volume: "6,080,000,000", marketCap: "149,020,000,000", change: "+2.45%", changeColor: "text-green-500", price: "2.4872", icon: XRP },
  { name: "Solana", symbol: "SOL", volume: "10,810,000,000", marketCap: "110,550,000,000", change: "+4.23%", changeColor: "text-green-500", price: "202.21", icon: Solana },
  { name: "USDC", symbol: "USDC", volume: "24,760,000,000", marketCap: "76,200,000,000", change: "-0.01%", changeColor: "text-red-500", price: "0.9998", icon: USDC },
  { name: "Dogecoin", symbol: "DOGE", volume: "3,220,000,000", marketCap: "30,370,000,000", change: "+1.71%", changeColor: "text-green-500", price: "0.20064", icon: Doge },
  { name: "TRON", symbol: "TRX", volume: "1,250,000,000", marketCap: "30,050,000,000", change: "+2.11%", changeColor: "text-green-500", price: "0.31745", icon: tron },
  { name: "Cardano", symbol: "ADA", volume: "1,450,000,000", marketCap: "24,660,000,000", change: "+2.02%", changeColor: "text-green-500", price: "0.68827", icon: Cardano },
];

const ITEMS_PER_PAGE = 5;

function Homepage() {
  const [currentPage, setCurrentPage] = useState(1);
  const [selectedCoin, setSelectedCoin] = useState(coinsData[0]);

  // Pagination
  const totalPages = Math.ceil(coinsData.length / ITEMS_PER_PAGE);
  const startIndex = (currentPage - 1) * ITEMS_PER_PAGE;
  const currentCoins = coinsData.slice(startIndex, startIndex + ITEMS_PER_PAGE);

  const handleCoinClick = (coin) => {
    setSelectedCoin(coin);
  };

  const handlePageChange = (newPage) => {
    if (newPage < 1 || newPage > totalPages) return;
    setCurrentPage(newPage);
    setSelectedCoin(coinsData[(newPage - 1) * ITEMS_PER_PAGE]); // First coin of the new page
  };

  return (
    <div className="bg-[#1A1A2E] text-white min-h-screen p-6">
      {/* Tabs + Search */}
      <div className="flex flex-col md:flex-row justify-between items-center mb-4 max-w-full mx-auto gap-4">
        <div className="flex flex-wrap gap-2">
          {["Top 50", "Top Gainers", "Top Losers", "All"].map((tab) => (
            <button
              key={tab}
              className="bg-[#2E2E3D] border border-[#3A3A4C] px-3 py-1 rounded hover:bg-[#3A3A4C] transition"
            >
              {tab}
            </button>
          ))}
        </div>
        <input
          type="text"
          placeholder="Search"
          className="bg-[#2E2E3D] border-none px-3 py-2 rounded text-white focus:outline-none focus:ring-2 focus:ring-teal-500"
        />
      </div>

      {/* Main Content: Coins List Left, Chart Right */}
      <div className="flex flex-col md:flex-row gap-4 max-w-8xl mx-auto">
        {/* Coin List */}
        <div className="flex-1 bg-[#2E2E3D] p-4 rounded-md overflow-x-auto">
          <table className="min-w-full text-left">
            <thead className="sticky top-0 bg-[#2E2E3D] z-10">
              <tr className="border-b border-gray-600">
                {["Coin", "Symbol", "Volume", "Market Cap", "24h", "Price"].map((head) => (
                  <th key={head} className="pb-2">{head}</th>
                ))}
              </tr>
            </thead>
            <tbody className="h-[500px] overflow-y-auto">
              {currentCoins.map((coin) => (
                <tr
                  key={coin.name}
                  className={`border-b border-gray-700 cursor-pointer hover:bg-[#3A3A4C] transition ${
                    selectedCoin.name === coin.name ? "bg-[#3A3A4C]" : ""
                  }`}
                  onClick={() => handleCoinClick(coin)}
                >
                  <td className="py-2 flex items-center gap-2">
                    <img src={coin.icon} alt={coin.name} className="w-4" />
                    {coin.name}
                  </td>
                  <td>{coin.symbol}</td>
                  <td>{coin.volume}</td>
                  <td>{coin.marketCap}</td>
                  <td className={coin.changeColor}>{coin.change}</td>
                  <td>{coin.price}</td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Pagination */}
          <div className="flex justify-between text-gray-400 mt-2 text-sm">
            <button
              className="text-red-500 cursor-pointer"
              onClick={() => handlePageChange(currentPage - 1)}
            >
              Previous
            </button>
            <span>Page {currentPage} of {totalPages}</span>
            <button
              className="text-red-500 cursor-pointer"
              onClick={() => handlePageChange(currentPage + 1)}
            >
              Next
            </button>
          </div>
        </div>

        {/* Chart */}
        <div className="w-full md:w-1/3 bg-[#2E2E3D] p-4 rounded-md">
          <div className="flex justify-between mb-2 text-sm">
            {["1 Day", "1 Week", "1 Month"].map((period) => (
              <span key={period}>{period}</span>
            ))}
          </div>
          <div className="bg-[#3A3A4C] h-64 rounded flex items-center justify-center text-gray-400 mb-4">
            <p>{selectedCoin.name} Chart Placeholder</p>
          </div>
          <div className="bg-[#FF9505] p-2 rounded text-white">
            <p className="font-bold">{selectedCoin.symbol} • {selectedCoin.name}</p>
            <p>{selectedCoin.price} ({selectedCoin.change})</p>
          </div>
        </div>
      </div>

      {/* Floating Chat Button */}
      <Link
        to="/chatbot"
        className="fixed bottom-5 right-5 w-14 h-14 rounded-full bg-[#2E2E3D] flex items-center justify-center text-white text-2xl hover:bg-[#3A3A4C] transition"
      >
        <FaCommentDots />
      </Link>
    </div>
  );
}

export default Homepage;