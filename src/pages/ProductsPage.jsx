import React, { useState } from "react";

const ProductsPage = () => {
  const [favorites, setFavorites] = useState([]);

  const products = [
    {
      id: 1,
      name: "Apple Watch Series 4",
      price: 120.0,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
    {
      id: 2,
      name: "Apple Watch Series 4",
      price: 120.0,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
    {
      id: 3,
      name: "Apple Watch Series 4",
      price: 120.0,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
    {
      id: 4,
      name: "Apple Watch Series 4",
      price: 120.0,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
    {
      id: 5,
      name: "Apple Watch Series 4",
      price: 120.0,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
    {
      id: 6,
      name: "Apple Watch Series 4",
      price: 120.0,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
  ];

  const toggleFavorite = (productId) => {
    setFavorites((prev) =>
      prev.includes(productId)
        ? prev.filter((id) => id !== productId)
        : [...prev, productId],
    );
  };

  return (
    <div>
      <h1 className="text-2xl sm:text-3xl font-bold text-gray-800 mb-4 sm:mb-6">
        Products
      </h1>

      <div className="relative overflow-hidden rounded-xl sm:rounded-2xl bg-gradient-to-r from-blue-500 to-blue-600 text-white p-4 sm:p-6 md:p-8 lg:p-10 mb-6 sm:mb-8">
        <div className="ml-0 sm:ml-10 md:ml-16 lg:ml-20 text-center sm:text-left">
          <p className="text-xs sm:text-sm mb-2 sm:mb-3 opacity-90">
            September 12-22
          </p>

          <h2 className="text-xl sm:text-3xl md:text-4xl lg:text-5xl font-bold leading-tight max-w-full sm:max-w-xl">
            Enjoy free home delivery in this summer
          </h2>

          <p className="mt-2 sm:mt-3 md:mt-4 text-xs sm:text-sm md:text-base text-blue-100 max-w-full sm:max-w-lg">
            Designer Dresses - Pick from trendy Designer Dress.
          </p>

          <button className="mt-3 sm:mt-4 md:mt-5 lg:mt-6 bg-orange-500 hover:bg-orange-600 transition px-4 sm:px-5 md:px-6 py-2 sm:py-2.5 md:py-3 rounded-lg font-medium text-sm sm:text-base">
            Get Started
          </button>
        </div>

        <button className="absolute left-2 sm:left-4 md:left-6 top-1/2 -translate-y-1/2 w-8 h-8 sm:w-9 sm:h-9 md:w-10 md:h-10 rounded-full bg-white/20 hover:bg-white/30 transition flex items-center justify-center text-sm sm:text-base">
          ❮
        </button>

        <button className="absolute right-2 sm:right-4 md:right-6 top-1/2 -translate-y-1/2 w-8 h-8 sm:w-9 sm:h-9 md:w-10 md:h-10 rounded-full bg-white/20 hover:bg-white/30 transition flex items-center justify-center text-sm sm:text-base">
          ❯
        </button>
      </div>

      <div className="grid grid-cols-2 md:grid-cols-2 xl:grid-cols-3 gap-3 sm:gap-4 md:gap-6">
        {products.map((product) => (
          <div
            key={product.id}
            className="bg-white rounded-xl sm:rounded-2xl p-2 sm:p-3 md:p-4 shadow-sm hover:shadow-lg transition duration-300"
          >
            <div className="relative">
              <img
                src={product.image}
                alt={product.name}
                className="w-full h-32 sm:h-48 md:h-64 object-contain"
              />

              <button
                onClick={() => toggleFavorite(product.id)}
                className={`absolute top-1.5 sm:top-2 md:top-3 right-1.5 sm:right-2 md:right-3 text-lg sm:text-xl md:text-2xl transition ${
                  favorites.includes(product.id)
                    ? "text-red-500"
                    : "text-gray-400 hover:text-red-500"
                }`}
              >
                {favorites.includes(product.id) ? "♥" : "♡"}
              </button>
            </div>

            <div className="mt-2 sm:mt-3 md:mt-4">
              <h3 className="font-semibold text-sm sm:text-base md:text-lg text-gray-800 truncate">
                {product.name}
              </h3>

              <p className="text-blue-600 font-bold text-sm sm:text-base md:text-xl mt-0.5 sm:mt-1">
                ${product.price}
              </p>

              <div className="flex items-center mt-1 sm:mt-2">
                <span className="text-yellow-400 text-xs sm:text-sm md:text-base">
                  ★★★★★
                </span>
                <span className="ml-1 sm:ml-2 text-[10px] sm:text-xs md:text-sm text-gray-500">
                  ({product.reviews})
                </span>
              </div>

              <div className="flex items-center justify-between mt-1.5 sm:mt-2 md:mt-3 text-[10px] sm:text-xs md:text-sm text-gray-500">
                <span>{product.category}</span>
                <span>Stock: {product.stock}</span>
              </div>

              <button className="w-full mt-2 sm:mt-3 md:mt-4 bg-gray-100 hover:bg-gray-200 transition py-1.5 sm:py-2 md:py-2.5 rounded-lg font-medium text-xs sm:text-sm md:text-base text-gray-700">
                Edit Product
              </button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ProductsPage;
