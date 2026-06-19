import React, { useState } from "react";
import {
  Search,
  Plus,
  Edit,
  Trash2,
  ChevronLeft,
  ChevronRight,
} from "lucide-react";

const Favorites = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const productsPerPage = 6;
  const [favorites, setFavorites] = useState([]);

  const products = [
    {
      id: 1,
      name: "Apple Watch Series 4",
      price: 120.0,
      rating: 5,
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
      rating: 5,
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
      rating: 5,
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
      rating: 5,
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
      rating: 5,
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
      rating: 5,
      reviews: 131,
      image:
        "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=200&h=200&fit=crop",
      category: "Electronics",
      stock: 45,
    },
  ];

  const filteredProducts = products.filter(
    (product) =>
      product.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
      product.category.toLowerCase().includes(searchTerm.toLowerCase()),
  );

  // Pagination
  const indexOfLastProduct = currentPage * productsPerPage;
  const indexOfFirstProduct = indexOfLastProduct - productsPerPage;
  const currentProducts = filteredProducts.slice(
    indexOfFirstProduct,
    indexOfLastProduct,
  );
  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);

  const renderStars = (rating) => {
    return (
      <div className="flex items-center gap-0.5">
        {[...Array(5)].map((_, i) => (
          <svg
            key={i}
            className={`w-4 h-4 ${i < rating ? "text-yellow-400 fill-yellow-400" : "text-gray-300"}`}
            fill="currentColor"
            viewBox="0 0 20 20"
          >
            <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
          </svg>
        ))}
      </div>
    );
  };

  const toggleFavorite = (productId) => {
    setFavorites((prev) =>
      prev.includes(productId)
        ? prev.filter((id) => id !== productId)
        : [...prev, productId],
    );
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6 text-gray-700">Favorites</h1>
      {/* Products */}
      <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
        {products.map((product) => (
          <div key={product.id} className="bg-white rounded-2xl p-4 shadow-sm">
            <div className="relative">
              <img
                src={product.image}
                alt=""
                className="w-full h-64 object-contain"
              />
            </div>

            <div className="flex items-center justify-between">
              <h3 className="font-semibold text-lg">{product.name}</h3>

              <button
                onClick={() => toggleFavorite(product.id)}
                className={`text-2xl transition duration-200 ${
                  favorites.includes(product.id)
                    ? "text-red-500"
                    : "text-gray-400 hover:text-red-500"
                }`}
              >
                {favorites.includes(product.id) ? "♥" : "♡"}
              </button>
            </div>

            <p className="text-blue-500 font-bold">${product.price}</p>

            <div className="flex items-center mt-2">
              ⭐⭐⭐⭐⭐
              <span className="ml-2 text-gray-400 text-sm">
                ({product.reviews})
              </span>
            </div>

            <button className="mt-4 bg-gray-100 px-5 py-2 rounded-lg text-sm font-medium">
              Edit Product
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Favorites;
