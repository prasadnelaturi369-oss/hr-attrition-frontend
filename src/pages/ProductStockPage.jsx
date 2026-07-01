import React, { useState } from "react";
import { Search, Pencil, Trash2 } from "lucide-react";

const initialProducts = [
  {
    id: 1,
    image: "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=100",
    name: "Apple Watch Series 4",
    category: "Digital Product",
    price: "$690.00",
    piece: 63,
    colors: ["#000", "#BDBDBD", "#F28B82"],
  },
  {
    id: 2,
    image: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=100",
    name: "Microsoft Headsquare",
    category: "Digital Product",
    price: "$190.00",
    piece: 13,
    colors: ["#000", "#FF7675", "#4D7CFE", "#F4C542"],
  },
  {
    id: 3,
    image: "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=100",
    name: "Women's Dress",
    category: "Fashion",
    price: "$640.00",
    piece: 635,
    colors: ["#8E2456", "#89BFFF", "#171A4A", "#4A46E8"],
  },
  {
    id: 4,
    image: "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=100",
    name: "Samsung A50",
    category: "Mobile",
    price: "$400.00",
    piece: 67,
    colors: ["#2E3E97", "#000", "#A51E52"],
  },
];

export default function ProductStockPage() {
  const [searchTerm, setSearchTerm] = useState("");
  const [products] = useState(initialProducts);

  // Filter products based on search term
  const filteredProducts = products.filter((product) =>
    product.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Handle search input change
  const handleSearchChange = (e) => {
    setSearchTerm(e.target.value);
  };

  // Clear search
  const clearSearch = () => {
    setSearchTerm("");
  };

  return (
    <div className="max-w-7xl mx-auto px-2 sm:px-4">

      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center mb-6 gap-4">
        <h1 className="text-2xl sm:text-3xl font-bold text-gray-800">Product Stock</h1>

        <div className="relative w-full sm:w-64 md:w-72">
          <Search className="absolute left-3 top-2.5 text-gray-400" size={18} />
          <input
            placeholder="Search product name"
            value={searchTerm}
            onChange={handleSearchChange}
            className="w-full pl-10 pr-10 py-2 rounded-full border border-gray-200 bg-white outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-sm"
          />
          {searchTerm && (
            <button
              onClick={clearSearch}
              className="absolute right-3 top-2.5 text-gray-400 hover:text-gray-600"
            >
              ✕
            </button>
          )}
        </div>
      </div>

      {filteredProducts.length > 0 && (
        <div className="mb-3 text-xs sm:text-sm text-gray-500">
          Showing {filteredProducts.length} of {products.length} products
        </div>
      )}

      <div className="bg-white rounded-xl sm:rounded-2xl border border-gray-200 overflow-hidden shadow-sm">
        <div className="overflow-x-auto">
          <table className="w-full text-xs sm:text-sm">
            <thead className="bg-gray-50 border-b border-gray-200">
              <tr className="text-left text-gray-600 font-semibold">
                <th className="px-3 py-3 sm:px-4 sm:py-4 whitespace-nowrap min-w-[60px]">Image</th>
                <th className="px-3 py-3 sm:px-4 sm:py-4 whitespace-nowrap min-w-[120px]">Product Name</th>
                <th className="px-3 py-3 sm:px-4 sm:py-4 whitespace-nowrap min-w-[100px]">Category</th>
                <th className="px-3 py-3 sm:px-4 sm:py-4 whitespace-nowrap min-w-[80px]">Price</th>
                <th className="px-3 py-3 sm:px-4 sm:py-4 whitespace-nowrap min-w-[60px]">Piece</th>
                <th className="px-3 py-3 sm:px-4 sm:py-4 whitespace-nowrap min-w-[120px]">Available Color</th>
                <th className="px-3 py-3 sm:px-4 sm:py-4 text-center whitespace-nowrap min-w-[80px]">Action</th>
              </tr>
            </thead>

            <tbody>
              {filteredProducts.length > 0 ? (
                filteredProducts.map((item) => (
                  <tr key={item.id} className="border-b border-gray-100 hover:bg-gray-50 transition-colors">
                    <td className="px-3 py-2 sm:px-4 sm:py-3">
                      <img
                        src={item.image}
                        alt={item.name}
                        className="w-10 h-10 sm:w-12 sm:h-12 rounded-lg object-cover border border-gray-100"
                      />
                    </td>

                    <td className="px-3 py-2 sm:px-4 sm:py-3 font-medium text-gray-800 whitespace-nowrap">
                      {item.name}
                    </td>

                    <td className="px-3 py-2 sm:px-4 sm:py-3 text-gray-600 whitespace-nowrap">
                      <span className="bg-gray-100 px-2 py-0.5 rounded-full text-xs">
                        {item.category}
                      </span>
                    </td>

                    <td className="px-3 py-2 sm:px-4 sm:py-3 font-semibold text-blue-600 whitespace-nowrap">
                      {item.price}
                    </td>

                    <td className="px-3 py-2 sm:px-4 sm:py-3 text-gray-600 whitespace-nowrap">
                      {item.piece}
                    </td>

                    <td className="px-3 py-2 sm:px-4 sm:py-3">
                      <div className="flex gap-1.5 flex-wrap">
                        {item.colors.map((c, i) => (
                          <span
                            key={i}
                            className="w-4 h-4 sm:w-5 sm:h-5 rounded-full border border-gray-200 flex-shrink-0"
                            style={{ background: c }}
                            title={`Color ${i + 1}`}
                          />
                        ))}
                      </div>
                    </td>

                    <td className="px-3 py-2 sm:px-4 sm:py-3">
                      <div className="flex justify-center gap-1.5 sm:gap-2">
                        <button className="w-8 h-8 sm:w-9 sm:h-9 border border-gray-200 rounded-lg flex items-center justify-center hover:bg-gray-100 transition text-gray-600 hover:text-blue-600">
                          <Pencil size={15} className="sm:w-4 sm:h-4" />
                        </button>

                        <button className="w-8 h-8 sm:w-9 sm:h-9 border border-gray-200 rounded-lg flex items-center justify-center hover:bg-red-50 transition text-gray-600 hover:text-red-500">
                          <Trash2 size={15} className="sm:w-4 sm:h-4" />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="7" className="px-4 py-12 text-center text-gray-500">
                    <div className="flex flex-col items-center gap-3">
                      <div className="w-16 h-16 bg-gray-100 rounded-full flex items-center justify-center">
                        <Search size={32} className="text-gray-400" />
                      </div>
                      <p className="text-base font-medium text-gray-600">No products found</p>
                      <p className="text-sm text-gray-400">Try adjusting your search term</p>
                      {searchTerm && (
                        <button
                          onClick={clearSearch}
                          className="mt-2 text-blue-600 hover:text-blue-700 font-medium text-sm"
                        >
                          Clear search
                        </button>
                      )}
                    </div>
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}