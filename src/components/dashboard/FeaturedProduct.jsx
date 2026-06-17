import React from "react";
import {
  ChevronLeft,
  ChevronRight,
  Headphones,
} from "lucide-react";

const FeaturedProduct = () => {
  return (
    <div className="bg-white rounded-xl p-4 shadow-sm border border-gray-100 h-full">
      {/* Header */}
      <div className="flex items-center justify-between mb-3">
        <h3 className="text-lg font-semibold text-gray-800">
          Featured Product
        </h3>
      </div>

      {/* Product Area - Reduced Height */}
      <div className="relative flex items-center justify-center py-6 min-h-[160px] sm:min-h-[180px] md:min-h-[200px]">
        {/* Left Arrow */}
        <button className="absolute left-0 top-1/2 -translate-y-1/2 w-8 h-8 flex items-center justify-center bg-white rounded-full shadow-md border border-gray-100 hover:bg-gray-50 transition">
          <ChevronLeft size={16} className="text-gray-600" />
        </button>

        {/* Product */}
        <div className="flex flex-col items-center">
          <div className="w-28 h-28 sm:w-32 sm:h-32 md:w-36 md:h-36 rounded-full bg-blue-50 flex items-center justify-center">
            <Headphones
              size={60}
              className="text-blue-600"
              strokeWidth={1.5}
            />
          </div>
        </div>

        {/* Right Arrow */}
        <button className="absolute right-0 top-1/2 -translate-y-1/2 w-8 h-8 flex items-center justify-center bg-white rounded-full shadow-md border border-gray-100 hover:bg-gray-50 transition">
          <ChevronRight size={16} className="text-gray-600" />
        </button>
      </div>

      {/* Product Details */}
      <div className="text-center border-t border-gray-100 pt-3">
        <h4 className="text-sm font-semibold text-gray-900">
          Sony WH-1000XM5
        </h4>

        <div className="mt-2">
          <span className="text-xl font-bold text-blue-600">
            $399
          </span>

          <span className="ml-2 text-sm text-gray-400 line-through">
            $449
          </span>
        </div>
      </div>
    </div>
  );
};

export default FeaturedProduct;