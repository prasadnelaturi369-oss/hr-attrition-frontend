import React, { useState } from "react";

const Favorites = () => {
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
      <h1 className="text-2xl sm:text-3xl font-bold text-gray-800 mb-4 sm:mb-6">Favorites</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {products.map((product) => (
          <div
            key={product.id}
            className="bg-white rounded-2xl p-4 shadow-sm hover:shadow-md transition"
          >
            <div className="relative">
              <img
                src={product.image}
                alt={product.name}
                className="w-full h-64 object-contain"
              />

              <button
                onClick={() => toggleFavorite(product.id)}
                className={`absolute top-3 right-3 text-2xl ${
                  favorites.includes(product.id)
                    ? "text-red-500"
                    : "text-gray-400 hover:text-red-500"
                }`}
              >
                {favorites.includes(product.id) ? "♥" : "♡"}
              </button>
            </div>

            <h3 className="mt-4 font-semibold text-lg">{product.name}</h3>

            <p className="text-blue-600 font-bold text-lg mt-1">
              ${product.price}
            </p>

            <div className="flex items-center mt-2">
              <span className="text-yellow-400">★★★★★</span>
              <span className="ml-2 text-sm text-gray-500">
                ({product.reviews})
              </span>
            </div>

            <button className="mt-4 w-full py-2 rounded-lg bg-gray-100 hover:bg-gray-200 font-medium transition">
              Edit Product
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Favorites;
