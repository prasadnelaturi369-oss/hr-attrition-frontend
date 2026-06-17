import React, { useState } from "react";
import { Menu, Bell, Search, ChevronDown } from "lucide-react";

const Header = ({ onMenuClick, isCollapsed }) => {
  const [isLanguageOpen, setIsLanguageOpen] = useState(false);
  const [isUserOpen, setIsUserOpen] = useState(false);

  return (
    <header className="bg-white px-4 py-3 flex items-center justify-between gap-4">
      <div className="flex items-center gap-3 flex-1">
        <button
          onClick={onMenuClick}
          className="text-gray-600 hover:text-gray-800 hover:bg-gray-100 p-2 rounded-lg transition-colors"
          aria-label="Toggle sidebar"
          title={isCollapsed ? "Expand sidebar" : "Collapse sidebar"}
        >
          <Menu size={22} />
        </button>

        <div className="relative flex-1 max-w-xs">
          <input
            type="text"
            placeholder="Search..."
            className="w-full pl-9 pr-4 py-1.5 bg-gray-50 border border-gray-200 rounded-2xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all"
          />
          <Search className="absolute left-2.5 top-2 h-4 w-4 text-gray-400" />
        </div>
      </div>

      <div className="flex items-center gap-1 md:gap-2">
        <button className="relative p-2 hover:bg-gray-100 rounded-full transition-colors">
  <Bell size={22} className="text-blue-600 fill-blue-600" />
  <span className="absolute top-1 right-1 w-2.5 h-2.5 bg-red-500 rounded-full border-2 border-white"></span>
</button>

        <div className="relative">
          <button
            onClick={() => setIsLanguageOpen(!isLanguageOpen)}
            className="flex items-center gap-2 px-3 py-1 bg-gray-50 hover:bg-gray-100 rounded-xl border border-gray-200 transition-all"
          >
            <img
              src="https://flagcdn.com/w40/us.png"
              alt="English"
              className="w-5 h-5 rounded-full object-cover"
            />
            <span className="font-medium text-sm">EN</span>
            <ChevronDown size={14} />
          </button>

          {isLanguageOpen && (
            <div className="absolute right-0 mt-2 w-52 bg-white rounded-xl shadow-xl border border-gray-100 py-2 z-50">
              <button className="flex items-center gap-3 w-full px-4 py-2 hover:bg-gray-50">
                <img
                  src="https://flagcdn.com/w40/us.png"
                  alt=""
                  className="w-5 h-5 rounded-full"
                />
                English
              </button>

              <button className="flex items-center gap-3 w-full px-4 py-2 hover:bg-gray-50">
                <img
                  src="https://flagcdn.com/w40/in.png"
                  alt=""
                  className="w-5 h-5 rounded-full"
                />
                Hindi
              </button>

              <button className="flex items-center gap-3 w-full px-4 py-2 hover:bg-gray-50">
                <img
                  src="https://flagcdn.com/w40/fr.png"
                  alt=""
                  className="w-5 h-5 rounded-full"
                />
                French
              </button>

              <button className="flex items-center gap-3 w-full px-4 py-2 hover:bg-gray-50">
                <img
                  src="https://flagcdn.com/w40/de.png"
                  alt=""
                  className="w-5 h-5 rounded-full"
                />
                German
              </button>
            </div>
          )}
        </div>

        <div className="relative">
          <button
            onClick={() => setIsUserOpen(!isUserOpen)}
            className="flex items-center gap-3 px-2 py-1 hover:bg-gray-50 rounded-xl transition-all"
          >
            <div className="w-10 h-10 rounded-full overflow-hidden border-2 border-blue-500">
              <img
                src="https://i.pravatar.cc/150?img=12"
                alt="Admin"
                className="w-full h-full object-cover"
              />
            </div>

            <div className="hidden md:block text-left">
              <p className="font-semibold text-gray-800 text-sm">
                Prasad Nelaturi
              </p>
              <p className="text-xs text-blue-600 font-medium">Super Admin</p>
            </div>

            <ChevronDown size={16} className="text-gray-500 hidden md:block" />
          </button>

          {isUserOpen && (
            <div className="absolute right-0 mt-2 w-56 bg-white rounded-lg shadow-lg border border-gray-200 py-2 z-50">
              <div className="px-4 py-3 border-b border-gray-100">
                <p className="text-sm font-medium text-gray-800">
                  Prasad Nelaturi
                </p>
                <p className="text-xs text-gray-500">prasad@example.com</p>
              </div>
              <button className="flex items-center gap-3 px-4 py-2 w-full hover:bg-gray-50 text-sm text-gray-700">
                <span>👤</span> My Profile
              </button>
              <button className="flex items-center gap-3 px-4 py-2 w-full hover:bg-gray-50 text-sm text-gray-700">
                <span>⚙️</span> Settings
              </button>
              <button className="flex items-center gap-3 px-4 py-2 w-full hover:bg-gray-50 text-sm text-gray-700">
                <span>❓</span> Help
              </button>
              <div className="border-t border-gray-100 mt-1 pt-1">
                <button className="flex items-center gap-3 px-4 py-2 w-full hover:bg-red-50 text-sm text-red-600">
                  <span>🚪</span> Logout
                </button>
              </div>
            </div>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
