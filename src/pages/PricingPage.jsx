import React from "react";

const plans = [
  { name: "Basic", price: "$14.99", active: false },
  { name: "Standard", price: "$49.99", active: false },
  { name: "Premium", price: "$89.99", active: true },
];

const features = [
  "Free Setup",
  "Bandwidth Limit 10 GB",
  "20 User Connection",
  "Analytics Report",
  "Public API Access",
  "Plugins Integration",
  "Custom Content Management",
];

export default function PricingPage() {
  return (
    <div>
      <h1 className="text-3xl font-bold text-gray-700 mb-6">
        Pricing
      </h1>

      <div className="grid gap-8 md:grid-cols-2 xl:grid-cols-3">
        {plans.map((plan) => (
          <div
            key={plan.name}
            className="bg-white rounded-3xl shadow-sm border overflow-hidden"
          >
            {/* Top */}
            <div className="p-8 text-center border-b">
              <h3 className="text-3xl font-semibold">
                {plan.name}
              </h3>

              <p className="text-gray-400 mt-2">
                Monthly Charge
              </p>

              <h2 className="text-5xl font-bold text-blue-500 mt-4">
                {plan.price}
              </h2>
            </div>

            {/* Features */}
            <div className="px-8 py-8 space-y-5 text-center">
              {features.map((item, i) => (
                <p
                  key={i}
                  className={`${
                    i >= 3 && !plan.active
                      ? "text-gray-300"
                      : "text-gray-700"
                  }`}
                >
                  {item}
                </p>
              ))}
            </div>

            {/* Footer */}
            <div className="p-8 border-t text-center">
              <button
                className={`w-full rounded-full py-3 font-medium transition
                ${
                  plan.active
                    ? "bg-blue-500 text-white"
                    : "border-2 border-blue-500 text-blue-500 hover:bg-blue-50"
                }`}
              >
                Get Started
              </button>

              <p className="mt-5 font-semibold underline">
                Start Your 30 Day Free Trial
              </p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}