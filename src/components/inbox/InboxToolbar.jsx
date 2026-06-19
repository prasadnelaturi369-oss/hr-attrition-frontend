import React from "react";
import { Search, Trash2, Archive, AlertCircle } from "lucide-react";

const InboxToolbar = ({ searchTerm, setSearchTerm }) => {
  return (
    <div className="flex flex-col md:flex-row justify-between gap-4 mb-5">
      <div className="relative">
        <Search
          size={18}
          className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400"
        />

        <input
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          type="text"
          placeholder="Search mail..."
          className="pl-10 pr-4 py-2 border rounded-full w-full md:w-72 focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div className="flex">
        <button className="p-2 border rounded-l-lg hover:bg-gray-50">
          <Archive size={18} />
        </button>

        <button className="p-2 border hover:bg-gray-50">
          <AlertCircle size={18} />
        </button>

        <button className="p-2 border rounded-r-lg hover:bg-gray-50">
          <Trash2 size={18} />
        </button>
      </div>
    </div>
  );
};

export default InboxToolbar;
