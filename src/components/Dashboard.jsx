import React from "react";
import {
  PieChart, Pie, Cell,
  LineChart, Line, XAxis, YAxis, Tooltip, CartesianGrid, Legend
} from "recharts";

const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042"];

const mockUserData = {
  portfolioValue: 15000,
  holdings: [
    { name: "Stocks", value: 70 },
    { name: "Bonds", value: 20 },
    { name: "Cash", value: 10 }
  ],
  performance: [
    { month: "Jan", value: 10000 },
    { month: "Feb", value: 12000 },
    { month: "Mar", value: 11000 },
    { month: "Apr", value: 15000 }
  ]
};

const Dashboard = () => {
  return (
    <div style={{ padding: "30px" }}>
      <h2>Portfolio Analytics Dashboard</h2>
      <h3>Total Portfolio Value: ₹{mockUserData.portfolioValue}</h3>

      <div style={{ display: "flex", gap: "50px", marginTop: "30px" }}>
        {/* Pie Chart */}
        <PieChart width={400} height={300}>
          <Pie
            data={mockUserData.holdings}
            cx="50%"
            cy="50%"
            outerRadius={100}
            fill="#8884d8"
            dataKey="value"
            label
          >
            {mockUserData.holdings.map((entry, index) => (
              <Cell key={index} fill={COLORS[index]} />
            ))}
          </Pie>
          <Tooltip />
        </PieChart>

        {/* Line Chart */}
        <LineChart width={500} height={300} data={mockUserData.performance}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="month" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="value" stroke="#8884d8" />
        </LineChart>
      </div>
    </div>
  );
};

export default Dashboard;
