import React, { useState } from "react";
import {
  Mail,
  Star,
  Send,
  FileText,
  AlertTriangle,
  ShieldAlert,
  Trash2,
} from "lucide-react";

const InboxSidebar = ({ onNavigate, currentView }) => {
  const [activeView, setActiveView] = useState(currentView || "inbox");

  const menuItems = [
    {
      id: "inbox",
      label: "Inbox",
      icon: Mail,
      count: "1253",
      color: "blue",
    },
    {
      id: "starred",
      label: "Starred",
      icon: Star,
      count: "245",
      color: "yellow",
    },
    {
      id: "sent",
      label: "Sent",
      icon: Send,
      count: "24,532",
      color: "green",
    },
    {
      id: "draft",
      label: "Draft",
      icon: FileText,
      count: "09",
      color: "gray",
    },
    {
      id: "spam",
      label: "Spam",
      icon: AlertTriangle,
      count: "14",
      color: "red",
    },
    {
      id: "important",
      label: "Important",
      icon: ShieldAlert,
      count: "18",
      color: "orange",
    },
    {
      id: "bin",
      label: "Bin",
      icon: Trash2,
      count: "9",
      color: "gray",
    },
  ];

  const handleNavigation = (viewId) => {
    setActiveView(viewId);
    if (onNavigate) {
      onNavigate(viewId);
    }
  };

  return (
    <div className="bg-white w-full xl:w-60 border rounded-2xl p-4">
      <button className="w-full bg-blue-500 text-white py-2 rounded-xl font-medium mb-6 hover:bg-blue-600 transition-colors">
        + Compose
      </button>

      <h3 className="font-semibold mb-4 text-gray-700">My Email</h3>

      <div className="space-y-1">
        {menuItems.map((item) => {
          const isActive = activeView === item.id;
          const bgColor = isActive ? "bg-blue-50" : "";
          const textColor = isActive ? "text-blue-600" : "text-gray-600";
          const hoverBg = isActive ? "" : "hover:bg-gray-50";

          return (
            <div
              key={item.id}
              onClick={() => handleNavigation(item.id)}
              className={`flex items-center justify-between p-3 rounded-lg cursor-pointer transition-all ${bgColor} ${hoverBg}`}
            >
              <div className={`flex items-center gap-3 ${textColor}`}>
                <item.icon size={16} />
                <span className="font-medium">{item.label}</span>
              </div>
              <span
                className={`text-sm ${
                  isActive ? "text-blue-600 font-semibold" : "text-gray-400"
                }`}
              >
                {item.count}
              </span>
            </div>
          );
        })}
      </div>

      <div className="mt-8">
        <h3 className="font-semibold mb-4 text-gray-700">Label</h3>

        <div className="space-y-3">
          <div className="flex items-center gap-3 text-gray-600 hover:text-gray-900 cursor-pointer transition-colors">
            <span className="w-3 h-3 bg-green-400 rounded-sm"></span>
            <span>Primary</span>
          </div>

          <div className="flex items-center gap-3 text-gray-600 hover:text-gray-900 cursor-pointer transition-colors">
            <span className="w-3 h-3 bg-blue-400 rounded-sm"></span>
            <span>Social</span>
          </div>

          <div className="flex items-center gap-3 text-gray-600 hover:text-gray-900 cursor-pointer transition-colors">
            <span className="w-3 h-3 bg-orange-400 rounded-sm"></span>
            <span>Work</span>
          </div>

          <div className="flex items-center gap-3 text-gray-600 hover:text-gray-900 cursor-pointer transition-colors">
            <span className="w-3 h-3 bg-purple-400 rounded-sm"></span>
            <span>Friends</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default InboxSidebar;
