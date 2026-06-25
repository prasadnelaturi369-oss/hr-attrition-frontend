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
      <h1 className="text-2xl sm:text-3xl font-bold text-gray-800 mb-4 sm:mb-6">Products</h1>

      {/* Banner */}
      <div className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-blue-500 to-blue-600 text-white p-10 mb-8">
        <div className="ml-20">
          <p className="text-sm mb-3">September 12-22</p>

          <h2 className="text-5xl font-bold leading-tight max-w-xl">
            Enjoy free home delivery in this summer
          </h2>

          <p className="mt-4 text-blue-100">
            Designer Dresses - Pick from trendy Designer Dress.
          </p>

          <button className="mt-6 bg-orange-500 hover:bg-orange-600 transition px-6 py-3 rounded-lg font-medium">
            Get Started
          </button>
        </div>

        <button className="absolute left-6 top-1/2 -translate-y-1/2 w-10 h-10 rounded-full bg-white/30 hover:bg-white/40 transition">
          ❮
        </button>

        <button className="absolute right-6 top-1/2 -translate-y-1/2 w-10 h-10 rounded-full bg-white/30 hover:bg-white/40 transition">
          ❯
        </button>
      </div>

      {/* Products Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {products.map((product) => (
          <div
            key={product.id}
            className="bg-white rounded-2xl p-4 shadow-sm hover:shadow-lg transition duration-300"
          >
            <div className="relative">
              <img
                src={product.image}
                alt={product.name}
                className="w-full h-64 object-contain"
              />

              <button
                onClick={() => toggleFavorite(product.id)}
                className={`absolute top-3 right-3 text-2xl transition ${
                  favorites.includes(product.id)
                    ? "text-red-500"
                    : "text-gray-400 hover:text-red-500"
                }`}
              >
                {favorites.includes(product.id) ? "♥" : "♡"}
              </button>
            </div>

            <div className="mt-4">
              <h3 className="font-semibold text-lg text-gray-800">
                {product.name}
              </h3>

              <p className="text-blue-600 font-bold text-xl mt-1">
                ${product.price}
              </p>

              <div className="flex items-center mt-2">
                <span className="text-yellow-400">★★★★★</span>
                <span className="ml-2 text-sm text-gray-500">
                  ({product.reviews})
                </span>
              </div>

              <div className="flex items-center justify-between mt-3 text-sm text-gray-500">
                <span>{product.category}</span>
                <span>Stock: {product.stock}</span>
              </div>

              <button className="w-full mt-4 bg-gray-100 hover:bg-gray-200 transition py-2.5 rounded-lg font-medium text-gray-700">
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
