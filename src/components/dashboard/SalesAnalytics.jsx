import React, { useEffect, useState } from "react";
import {
  ResponsiveContainer,
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

const SalesAnalytics = () => {
  const [data, setData] = useState([
    { month: "Jan", sales: 4200, profit: 2100 },
    { month: "Feb", sales: 5800, profit: 3200 },
    { month: "Mar", sales: 4900, profit: 2800 },
    { month: "Apr", sales: 7600, profit: 4200 },
    { month: "May", sales: 8900, profit: 5100 },
    { month: "Jun", sales: 10500, profit: 6200 },
  ]);

  // Simulated real-time updates
  useEffect(() => {
    const interval = setInterval(() => {
      setData((prev) =>
        prev.map((item) => ({
          ...item,
          sales: Math.max(
            1000,
            item.sales + Math.floor(Math.random() * 500 - 250)
          ),
          profit: Math.max(
            500,
            item.profit + Math.floor(Math.random() * 300 - 150)
          ),
        }))
      );
    }, 4000);

    return () => clearInterval(interval);
  }, []);

  return (
    <div className="bg-white rounded-xl border border-gray-100 shadow-sm p-4 w-full h-full">
      {/* Header */}
      <div className="flex items-center justify-between mb-5">
        <div>
          <h3 className="text-lg font-semibold text-gray-900">
            Sales Analytics
          </h3>
        </div>
      </div>

      {/* Graph - Reduced Height */}
      <div className="w-full h-[180px] sm:h-[200px] md:h-[220px]">
        <ResponsiveContainer width="100%" height="100%">
          <LineChart
            data={data}
            margin={{
              top: 10,
              right: 15,
              left: -20,
              bottom: 0,
            }}
          >
            <CartesianGrid
              strokeDasharray="3 3"
              vertical={false}
              stroke="#E5E7EB"
            />

            <XAxis
              dataKey="month"
              axisLine={false}
              tickLine={false}
              tick={{ fill: "#94A3B8", fontSize: 12 }}
            />

            <YAxis
              axisLine={false}
              tickLine={false}
              tick={{ fill: "#94A3B8", fontSize: 12 }}
            />

            <Tooltip
              contentStyle={{
                border: "none",
                borderRadius: "12px",
                boxShadow: "0 10px 30px rgba(0,0,0,0.08)",
              }}
            />

            {/* Sales */}
            <Line
              type="monotone"
              dataKey="sales"
              stroke="#3B82F6"
              strokeWidth={3}
              dot={{
                r: 3, // Reduced dot size
                fill: "#3B82F6",
                stroke: "#fff",
                strokeWidth: 2,
              }}
              activeDot={{
                r: 5, // Reduced active dot size
                fill: "#3B82F6",
              }}
            />

            {/* Profit */}
            <Line
              type="monotone"
              dataKey="profit"
              stroke="#10B981"
              strokeWidth={3}
              dot={{
                r: 3, // Reduced dot size
                fill: "#10B981",
                stroke: "#fff",
                strokeWidth: 2,
              }}
              activeDot={{
                r: 5, // Reduced active dot size
                fill: "#10B981",
              }}
            />
          </LineChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default SalesAnalytics;