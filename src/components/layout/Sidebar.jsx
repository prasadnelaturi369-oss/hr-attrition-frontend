import React, { useState } from "react";
import {
  Home,
  Package,
  Star,
  Inbox,
  ShoppingBag,
  DollarSign,
  Calendar,
  CheckSquare,
  Users,
  FileText,
  Grid,
  Settings,
  LogOut,
  LayoutDashboard,
  X,
} from "lucide-react";

const Sidebar = ({
  isOpen,
  setIsOpen,
  isCollapsed,
  onNavigate,
  currentPage,
}) => {
  const [activeItem, setActiveItem] = useState(currentPage || "Dashboard");

  const menuItems = [
    { name: "Dashboard", icon: Home },
    { name: "Products", icon: Package },
    { name: "Favorites", icon: Star },
    { name: "Inbox", icon: Inbox },
    { name: "Order Lists", icon: ShoppingBag },
    { name: "Product Stock", icon: Package },
  ];

  const pagesItems = [
    { name: "Pricing", icon: DollarSign },
    { name: "Calender", icon: Calendar },
    { name: "To-Do", icon: CheckSquare },
    { name: "Contact", icon: Users },
    { name: "Invoice", icon: FileText },
  ];

  const uiElements = [
    { name: "Team", icon: Users },
    { name: "Table", icon: Grid },
  ];

  const handleItemClick = (itemName) => {
    setActiveItem(itemName);
    if (onNavigate) {
      onNavigate(itemName);
    }
  };

  const renderMenuItem = (item, isActive) => (
    <div
      className={`relative flex items-center py-2.5 rounded-lg cursor-pointer transition-all ${
        isActive ? "text-blue-600" : "text-gray-600 hover:bg-gray-50"
      }`}
      onClick={() => handleItemClick(item.name)}
      style={{
        paddingLeft: isCollapsed ? "0px" : "20px",
        marginLeft: isCollapsed ? "0px" : "0px",
        marginRight: isCollapsed ? "0px" : "10px",
        justifyContent: isCollapsed ? "center" : "flex-start",
      }}
    >
      {isActive && (
        <div className="absolute left-0 top-0 h-full w-[4px] bg-blue-600 rounded-r"></div>
      )}

      {isActive && (
        <div
          className="absolute inset-0 rounded-lg bg-blue-50"
          style={{
            left: isCollapsed ? "4px" : "16px",
            right: isCollapsed ? "4px" : "0px",
            top: "0px",
            bottom: "0px",
          }}
        ></div>
      )}

      <item.icon
        size={20}
        className={`relative z-10 ${isActive ? "text-blue-600" : ""} ${
          isCollapsed ? "" : "mr-3"
        }`}
        style={{
          marginLeft: isCollapsed ? "0px" : "4px",
        }}
      />
      {!isCollapsed && (
        <span
          className={`relative z-10 text-sm font-medium ${isActive ? "text-blue-600" : ""}`}
        >
          {item.name}
        </span>
      )}
    </div>
  );

  return (
    <>
      {isOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-40 lg:hidden"
          onClick={() => setIsOpen(false)}
        />
      )}

      <div
        className={`fixed lg:static inset-y-0 left-0 z-50 bg-white transform transition-all duration-300 ease-in-out ${
          isOpen ? "translate-x-0" : "-translate-x-full"
        } lg:translate-x-0 flex flex-col h-full ${
          isCollapsed ? "w-16" : "w-64"
        }`}
      >
        <div className="flex items-center justify-center px-4 py-5 relative">
          <div className="flex items-center">
            {!isCollapsed && (
              <span className="text-lg font-bold">
                <span className="text-blue-600">Dash</span>
                <span className="text-gray-800">Stack</span>
              </span>
            )}
            {isCollapsed && (
              <div className="w-8 h-8 bg-gradient-to-br from-blue-600 to-indigo-600 rounded-lg flex items-center justify-center flex-shrink-0">
                <span className="text-white font-bold text-sm">DS</span>
              </div>
            )}
          </div>

          {isOpen && (
            <button
              onClick={() => setIsOpen(false)}
              className="absolute right-3 lg:hidden text-gray-500 hover:text-gray-700 hover:bg-gray-100 p-2 rounded-lg transition-colors"
              aria-label="Close sidebar"
            >
              <X size={22} />
            </button>
          )}
        </div>

        <nav className="flex-1 overflow-y-auto py-4">
          {isCollapsed && (
            <div className="mb-4">
              {renderMenuItem(
                { name: "Dashboard", icon: LayoutDashboard },
                activeItem === "Dashboard",
              )}
            </div>
          )}

          {!isCollapsed && (
            <div className="mb-6">
              <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-3 px-4">
                Menu
              </p>
              {menuItems.map((item) =>
                renderMenuItem(item, activeItem === item.name),
              )}
            </div>
          )}

          {isCollapsed && (
            <div className="mb-4">
              {menuItems.map((item) =>
                renderMenuItem(item, activeItem === item.name),
              )}
            </div>
          )}

          {!isCollapsed && (
            <div className="mb-6">
              <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-3 px-4">
                PAGES
              </p>
              {pagesItems.map((item) =>
                renderMenuItem(item, activeItem === item.name),
              )}
            </div>
          )}

          {isCollapsed && (
            <div className="mb-4">
              {pagesItems.map((item) =>
                renderMenuItem(item, activeItem === item.name),
              )}
            </div>
          )}

          {!isCollapsed && (
            <div className="mb-6">
              <p className="text-xs font-semibold text-gray-400 uppercase tracking-wider mb-3 px-4">
                UI Elements
              </p>
              {uiElements.map((item) =>
                renderMenuItem(item, activeItem === item.name),
              )}
            </div>
          )}

          {isCollapsed && (
            <div className="mb-4">
              {uiElements.map((item) =>
                renderMenuItem(item, activeItem === item.name),
              )}
            </div>
          )}

          <div className="pt-4 border-t border-gray-200">
            <div
              className={`flex items-center py-2.5 rounded-lg cursor-pointer text-gray-600 hover:bg-gray-50`}
              style={{
                paddingLeft: isCollapsed ? "0px" : "20px",
                marginLeft: isCollapsed ? "8px" : "0px",
                marginRight: isCollapsed ? "8px" : "10px",
                justifyContent: isCollapsed ? "center" : "flex-start",
              }}
            >
              <Settings
                size={20}
                className={isCollapsed ? "" : "mr-3"}
                style={{
                  marginLeft: isCollapsed ? "0px" : "4px",
                }}
              />
              {!isCollapsed && (
                <span className="text-sm font-medium">Settings</span>
              )}
            </div>
            <div
              className={`flex items-center py-2.5 rounded-lg cursor-pointer text-red-600 hover:bg-red-50`}
              style={{
                paddingLeft: isCollapsed ? "0px" : "20px",
                marginLeft: isCollapsed ? "8px" : "0px",
                marginRight: isCollapsed ? "8px" : "10px",
                justifyContent: isCollapsed ? "center" : "flex-start",
              }}
            >
              <LogOut
                size={20}
                className={isCollapsed ? "" : "mr-3"}
                style={{
                  marginLeft: isCollapsed ? "0px" : "4px",
                }}
              />
              {!isCollapsed && (
                <span className="text-sm font-medium">Logout</span>
              )}
            </div>
          </div>
        </nav>
      </div>
    </>
  );
};

export default Sidebar;
