import React from "react";

const CustomersCard = () => {
  const totalCustomers = "35K";
  const progress = 75; // Percentage

  const radius = 55;
  const circumference = 2 * Math.PI * radius;
  const strokeDashoffset =
    circumference - (progress / 100) * circumference;

  return (
    <div className="bg-white rounded-xl p-4 shadow-sm border border-gray-100">
      {/* Header */}
      <div className="flex items-center justify-between mb-6">
        <h3 className="text-lg font-semibold text-gray-800">
          Customers
        </h3>
      </div>

      {/* Donut Chart */}
      <div className="flex flex-col items-center">
        <div className="relative w-44 h-44">
          <svg
            className="w-full h-full -rotate-90"
            viewBox="0 0 160 160"
          >
            {/* Background Ring */}
            <circle
              cx="80"
              cy="80"
              r={radius}
              fill="none"
              stroke="#F1F5F9"
              strokeWidth="10"
            />

            {/* Progress Ring */}
            <circle
              cx="80"
              cy="80"
              r={radius}
              fill="none"
              stroke="#3B82F6"
              strokeWidth="10"
              strokeLinecap="round"
              strokeDasharray={circumference}
              strokeDashoffset={strokeDashoffset}
            />

            {/* Dots ON Border */}
            <circle cx="80" cy="25" r="4" fill="#3B82F6" />
            <circle cx="135" cy="80" r="4" fill="#3B82F6" />
            <circle cx="80" cy="135" r="4" fill="#3B82F6" />
            <circle cx="25" cy="80" r="4" fill="#3B82F6" />
          </svg>

          {/* Center Text */}
          <div className="absolute inset-0 flex flex-col items-center justify-center">
            <h2 className="text-3xl font-bold text-gray-800">
              {totalCustomers}
            </h2>

            <p className="text-xs text-gray-400 mt-1">
              Total Customers
            </p>
          </div>
        </div>

        {/* Statistics */}
        <div className="flex justify-center gap-12 mt-6 w-full">
          <div className="text-center">
            <h4 className="text-2xl font-bold text-gray-800">
              34.2K
            </h4>

            <div className="flex items-center justify-center gap-2 mt-2">
              <span className="w-2.5 h-2.5 rounded-full bg-blue-500"></span>

              <span className="text-sm text-gray-500">
                New Customers
              </span>
            </div>
          </div>

          <div className="text-center">
            <h4 className="text-2xl font-bold text-gray-800">
              1.4K
            </h4>

            <div className="flex items-center justify-center gap-2 mt-2">
              <span className="w-2.5 h-2.5 rounded-full bg-blue-500"></span>

              <span className="text-sm text-gray-500">
                Repeated
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CustomersCard;