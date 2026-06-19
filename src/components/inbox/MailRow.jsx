import React from "react";
import { Star } from "lucide-react";

const MailRow = ({ sender, label, title, time }) => {
  const labelColors = {
    Primary: "bg-green-100 text-green-600",
    Social: "bg-blue-100 text-blue-600",
    Work: "bg-orange-100 text-orange-600",
    Friends: "bg-purple-100 text-purple-600",
  };

  return (
    <div className="grid grid-cols-[22px_22px_130px_60px_1fr_80px] items-center gap-2 px-4 py-3 border-b hover:bg-gray-50">
      <input type="checkbox" />

      <Star
        size={16}
        className="text-gray-300 cursor-pointer hover:text-yellow-400"
      />

      <span className="font-medium text-gray-700">{sender}</span>

      <span
        className={`px-2 py-1 rounded text-xs font-medium ${
          labelColors[label] || "bg-gray-100 text-gray-600"
        }`}
      >
        {label}
      </span>

      <span className="truncate text-gray-600">{title}</span>

      <span className="text-sm text-gray-500 text-right">{time}</span>
    </div>
  );
};

export default MailRow;
