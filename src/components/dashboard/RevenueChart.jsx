import React, { useState } from "react";
import {
  ResponsiveContainer,
  AreaChart,
  Area,
  XAxis,
  YAxis,
  Tooltip,
  CartesianGrid,
} from "recharts";

const data = [
  { value: "5K", sales: 35, profit: 20 },
  { value: "8K", sales: 42, profit: 28 },
  { value: "10K", sales: 55, profit: 35 },
  { value: "12K", sales: 62, profit: 40 },
  { value: "15K", sales: 48, profit: 32 },
  { value: "18K", sales: 70, profit: 50 },
  { value: "20K", sales: 82, profit: 58 },
  { value: "22K", sales: 75, profit: 62 },
  { value: "25K", sales: 68, profit: 54 },
  { value: "28K", sales: 88, profit: 65 },
  { value: "30K", sales: 96, profit: 76 },
];

const CustomTooltip = ({ active, payload }) => {
  if (!active || !payload || !payload.length) return null;

  return (
    <div className="bg-white p-2 rounded-xl shadow-lg border">
      <p className="font-semibold">{payload[0].payload.value}</p>

      <p className="text-orange-500">Sales: {payload[0].value}</p>

      <p className="text-purple-500">Profile: {payload[1]?.value}</p>
    </div>
  );
};

export default function RevenueChart() {
  const [month, setMonth] = useState("January");

  return (
    <div className="bg-white rounded-xl p-4 shadow-sm">
      <div className="flex justify-between items-center mb-6">
        <div>
          <h2 className="text-xl font-semibold">Revenue</h2>
        </div>

        <select
          value={month}
          onChange={(e) => setMonth(e.target.value)}
          className="px-4 py-1      text-sm bg-gray-100 rounded-xl outline-none"
        >
          <option>January</option>
          <option>February</option>
          <option>March</option>
          <option>April</option>
          <option>May</option>
          <option>June</option>
          <option>July</option>
          <option>August</option>
          <option>September</option>
          <option>October</option>
          <option>November</option>
          <option>December</option>
        </select>
      </div>

      <div style={{ width: "100%", height: 350 }}>
        <ResponsiveContainer>
          <AreaChart data={data}>
            <defs>
              <linearGradient id="sales" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#FB923C" stopOpacity={0.7} />
                <stop offset="95%" stopColor="#FB923C" stopOpacity={0.05} />
              </linearGradient>

              <linearGradient id="profit" x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor="#8B5CF6" stopOpacity={0.7} />
                <stop offset="95%" stopColor="#8B5CF6" stopOpacity={0.05} />
              </linearGradient>
            </defs>

            <CartesianGrid strokeDasharray="3 3" vertical={false} />

            <XAxis dataKey="value" axisLine={false} tickLine={false} />

            <YAxis axisLine={false} tickLine={false} domain={[0, 100]} />

            <Tooltip content={<CustomTooltip />} />

            <Area
              type="natural"
              dataKey="sales"
              stroke="transparent"
              strokeWidth={0}
              fill="url(#sales)"
              fillOpacity={1}
              activeDot={{
                r: 5,
                fill: "#FB923C",
                stroke: "#fff",
                strokeWidth: 2,
              }}
            />

            <Area
              type="natural"
              dataKey="profit"
              stroke="transparent"
              strokeWidth={0}
              fill="url(#profit)"
              fillOpacity={1}
              activeDot={{
                r: 5,
                fill: "#8B5CF6",
                stroke: "#fff",
                strokeWidth: 2,
              }}
            />
          </AreaChart>
        </ResponsiveContainer>
      </div>
      <div className="flex justify-center gap-8 mt-6">
        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded-full bg-orange-400"></span>
          <span className="text-sm text-gray-600">Sales</span>
        </div>

        <div className="flex items-center gap-2">
          <span className="w-3 h-3 rounded-full bg-purple-500"></span>
          <span className="text-sm text-gray-600">Profit</span>
        </div>
      </div>
    </div>
  );
}
