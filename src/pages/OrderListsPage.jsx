import React, { useMemo, useState } from "react";
import {
  format,
  startOfMonth,
  endOfMonth,
  startOfWeek,
  endOfWeek,
  addDays,
  addMonths,
  subMonths,
  isSameMonth,
  isSameDay,
} from "date-fns";
import {
  Filter,
  ChevronDown,
  RotateCcw,
  ChevronLeft,
  ChevronRight,
  X,
} from "lucide-react";

const orders = [
  {
    id: "00001",
    name: "Christine Brooks",
    address: "089 Kutch Green Apt. 448",
    date: "04 Sep 2019",
    type: "Electric",
    status: "Completed",
  },
  {
    id: "00002",
    name: "Rosie Pearson",
    address: "979 Immanuel Ferry Suite 526",
    date: "28 May 2019",
    type: "Book",
    status: "Processing",
  },
  {
    id: "00003",
    name: "Darrell Caldwell",
    address: "8587 Frida Ports",
    date: "23 Nov 2019",
    type: "Medicine",
    status: "Rejected",
  },
  {
    id: "00004",
    name: "Gilbert Johnston",
    address: "768 Destiny Lake Suite 600",
    date: "05 Feb 2019",
    type: "Mobile",
    status: "Completed",
  },
  {
    id: "00005",
    name: "Alan Cain",
    address: "042 Mylene Throughway",
    date: "29 Jul 2019",
    type: "Watch",
    status: "Processing",
  },
  {
    id: "00006",
    name: "Alfred Murray",
    address: "543 Weimann Mountain",
    date: "15 Aug 2019",
    type: "Medicine",
    status: "Completed",
  },
  {
    id: "00007",
    name: "Maggie Sullivan",
    address: "New Scottieberg",
    date: "21 Dec 2019",
    type: "Watch",
    status: "Processing",
  },
  {
    id: "00008",
    name: "Rosie Todd",
    address: "New Jon",
    date: "30 Apr 2019",
    type: "Medicine",
    status: "On Hold",
  },
  {
    id: "00009",
    name: "Dollie Hines",
    address: "124 Lyla Forge Suite 975",
    date: "09 Jan 2019",
    type: "Book",
    status: "In Transit",
  },
  {
    id: "00010",
    name: "Jenny Wilson",
    address: "Grand Street",
    date: "18 Jun 2019",
    type: "Mobile",
    status: "Completed",
  },
  {
    id: "00011",
    name: "Robert Fox",
    address: "Palm Avenue",
    date: "22 Mar 2019",
    type: "Electric",
    status: "Processing",
  },
  {
    id: "00012",
    name: "Kristin Watson",
    address: "Ocean View",
    date: "11 Jul 2019",
    type: "Watch",
    status: "Rejected",
  },
];

const OrderListsPage = () => {
  const [currentPage, setCurrentPage] = useState(1);
  const [showDate, setShowDate] = useState(false);
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const [selectedDate, setSelectedDate] = useState(null);
  const [showType, setShowType] = useState(false);
  const [showStatus, setShowStatus] = useState(false);
  const [selectedTypes, setSelectedTypes] = useState([]);
  const [selectedStatuses, setSelectedStatuses] = useState([]);
  const [isMobileFilterOpen, setIsMobileFilterOpen] = useState(false);

  const perPage = 9;

  const orderTypes = useMemo(() => {
    const types = [...new Set(orders.map((order) => order.type))];
    return types;
  }, []);

  const statuses = useMemo(() => {
    const statuses = [...new Set(orders.map((order) => order.status))];
    return statuses;
  }, []);

  const filteredOrders = useMemo(() => {
    let filtered = [...orders];

    // Filter by date
    if (selectedDate) {
      const formattedDate = format(selectedDate, "dd MMM yyyy");
      filtered = filtered.filter((order) => order.date === formattedDate);
    }

    // Filter by type
    if (selectedTypes.length > 0) {
      filtered = filtered.filter((order) => selectedTypes.includes(order.type));
    }

    // Filter by status
    if (selectedStatuses.length > 0) {
      filtered = filtered.filter((order) =>
        selectedStatuses.includes(order.status),
      );
    }

    return filtered;
  }, [selectedDate, selectedTypes, selectedStatuses]);

  const totalPages = Math.ceil(filteredOrders.length / perPage);

  useMemo(() => {
    if (currentPage > totalPages && totalPages > 0) {
      setCurrentPage(1);
    }
  }, [filteredOrders, totalPages, currentPage]);

  const currentOrders = useMemo(() => {
    const start = (currentPage - 1) * perPage;
    return filteredOrders.slice(start, start + perPage);
  }, [filteredOrders, currentPage]);

  const statusStyle = (status) => {
    switch (status) {
      case "Completed":
        return "bg-emerald-100 text-emerald-600";
      case "Processing":
        return "bg-violet-100 text-violet-600";
      case "Rejected":
        return "bg-red-100 text-red-500";
      case "On Hold":
        return "bg-orange-100 text-orange-500";
      case "In Transit":
        return "bg-pink-100 text-pink-500";
      default:
        return "bg-gray-100 text-gray-500";
    }
  };

  const toggleType = (type) => {
    setSelectedTypes((prev) =>
      prev.includes(type) ? prev.filter((t) => t !== type) : [...prev, type],
    );
  };

  const toggleStatus = (status) => {
    setSelectedStatuses((prev) =>
      prev.includes(status)
        ? prev.filter((s) => s !== status)
        : [...prev, status],
    );
  };

  const resetFilters = () => {
    setSelectedDate(null);
    setSelectedTypes([]);
    setSelectedStatuses([]);
    setCurrentPage(1);
    setIsMobileFilterOpen(false);
  };

  const applyDateFilter = () => {
    setShowDate(false);
  };

  const applyTypeFilter = () => {
    setShowType(false);
  };

  const applyStatusFilter = () => {
    setShowStatus(false);
  };

  const getActiveFilterCount = () => {
    let count = 0;
    if (selectedDate) count++;
    if (selectedTypes.length > 0) count++;
    if (selectedStatuses.length > 0) count++;
    return count;
  };

  // Mobile Filter Modal
  const MobileFilterModal = () => (
    <div className="fixed inset-0 z-50 bg-black bg-opacity-50 md:hidden">
      <div className="absolute bottom-0 left-0 right-0 bg-white rounded-t-3xl max-h-[90vh] overflow-y-auto">
        <div className="sticky top-0 bg-white border-b px-4 py-4 flex justify-between items-center">
          <h3 className="text-lg font-semibold">Filters</h3>
          <button
            onClick={() => setIsMobileFilterOpen(false)}
            className="p-2 hover:bg-gray-100 rounded-full"
          >
            <X size={20} />
          </button>
        </div>

        <div className="p-4 space-y-6">
          {/* Date Filter */}
          <div>
            <label className="block text-sm font-medium mb-2">Date</label>
            <div className="relative">
              <button
                onClick={() => setShowDate(!showDate)}
                className="w-full h-[42px] border rounded-lg px-4 flex items-center justify-between text-sm bg-white"
              >
                <span>
                  {selectedDate
                    ? format(selectedDate, "dd MMM yyyy")
                    : "Select Date"}
                </span>
                <ChevronDown size={14} />
              </button>

              {showDate && (
                <div className="absolute top-[48px] left-0 z-50 w-full bg-white rounded-xl shadow-xl border">
                  <div className="p-4">
                    <div className="flex items-center justify-between mb-4">
                      <h3 className="font-semibold text-sm">
                        {format(currentMonth, "MMMM yyyy")}
                      </h3>
                      <div className="flex gap-2">
                        <button
                          onClick={() =>
                            setCurrentMonth(subMonths(currentMonth, 1))
                          }
                          className="w-7 h-7 rounded bg-gray-100 flex items-center justify-center"
                        >
                          <ChevronLeft size={14} />
                        </button>
                        <button
                          onClick={() =>
                            setCurrentMonth(addMonths(currentMonth, 1))
                          }
                          className="w-7 h-7 rounded bg-gray-100 flex items-center justify-center"
                        >
                          <ChevronRight size={14} />
                        </button>
                      </div>
                    </div>

                    <div className="grid grid-cols-7 text-center text-gray-500 text-xs mb-2">
                      {["S", "M", "T", "W", "T", "F", "S"].map((day) => (
                        <div key={day} className="py-1">
                          {day}
                        </div>
                      ))}
                    </div>

                    <div className="grid grid-cols-7 gap-1 text-center">
                      {(() => {
                        const monthStart = startOfMonth(currentMonth);
                        const monthEnd = endOfMonth(monthStart);
                        const startDate = startOfWeek(monthStart);
                        const endDate = endOfWeek(monthEnd);
                        const rows = [];
                        let day = startDate;
                        while (day <= endDate) {
                          rows.push(day);
                          day = addDays(day, 1);
                        }
                        return rows.map((date, idx) => (
                          <button
                            key={idx}
                            onClick={() => setSelectedDate(date)}
                            className={`w-8 h-8 mx-auto rounded-lg text-sm transition
                              ${
                                isSameDay(date, selectedDate)
                                  ? "bg-blue-500 text-white"
                                  : isSameMonth(date, currentMonth)
                                    ? "text-gray-700 hover:bg-gray-100"
                                    : "text-gray-300"
                              }
                            `}
                          >
                            {format(date, "d")}
                          </button>
                        ));
                      })()}
                    </div>

                    <button
                      onClick={applyDateFilter}
                      className="w-full mt-4 bg-blue-500 text-white py-2 rounded-md text-sm"
                    >
                      Apply Date
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Type Filter */}
          <div>
            <label className="block text-sm font-medium mb-2">Order Type</label>
            <div className="flex flex-wrap gap-2">
              {orderTypes.map((type) => (
                <button
                  key={type}
                  onClick={() => toggleType(type)}
                  className={`px-4 py-2 rounded-full text-sm border transition ${
                    selectedTypes.includes(type)
                      ? "bg-blue-500 text-white border-blue-500"
                      : "border-gray-300 hover:border-blue-400"
                  }`}
                >
                  {type}
                </button>
              ))}
            </div>
          </div>

          {/* Status Filter */}
          <div>
            <label className="block text-sm font-medium mb-2">
              Order Status
            </label>
            <div className="flex flex-wrap gap-2">
              {statuses.map((status) => (
                <button
                  key={status}
                  onClick={() => toggleStatus(status)}
                  className={`px-4 py-2 rounded-full text-sm border transition ${
                    selectedStatuses.includes(status)
                      ? "bg-blue-500 text-white border-blue-500"
                      : "border-gray-300 hover:border-blue-400"
                  }`}
                >
                  {status}
                </button>
              ))}
            </div>
          </div>

          {/* Apply & Reset Buttons */}
          <div className="flex gap-3 pt-4 border-t">
            <button
              onClick={resetFilters}
              className="flex-1 py-3 border rounded-lg text-red-500 font-medium"
            >
              Reset All
            </button>
            <button
              onClick={() => setIsMobileFilterOpen(false)}
              className="flex-1 py-3 bg-blue-500 text-white rounded-lg font-medium"
            >
              Apply Filters
            </button>
          </div>
        </div>
      </div>
    </div>
  );

  return (
    <div>
      <h1 className="text-2xl sm:text-3xl font-bold text-gray-800 mb-4 sm:mb-6">
        Order Lists
      </h1>

      {/* Desktop Filters */}
      <div className="hidden md:block relative bg-white border border-gray-200 rounded-xl mb-6 overflow-visible max-w-[720px] w-full">
        <div className="flex flex-wrap items-center h-[52px]">
          <div className="w-[50px] h-full border-r flex items-center justify-center flex-shrink-0">
            <Filter size={16} />
          </div>

          <div className="w-[100px] h-full border-r flex items-center px-5 text-sm font-medium flex-shrink-0">
            Filter By
          </div>

          {/* Date */}
          <div className="relative flex-shrink-0">
            <button
              onClick={() => setShowDate(!showDate)}
              className="w-[150px] h-full border-r px-4 flex items-center justify-between text-sm"
            >
              <span>
                {selectedDate ? format(selectedDate, "dd MMM yyyy") : "Date"}
              </span>
              <ChevronDown size={14} />
            </button>

            {showDate && (
              <div className="absolute top-[58px] left-0 z-50 w-[295px] bg-white rounded-3xl shadow-xl border overflow-hidden">
                <div className="flex items-center justify-between px-6 py-3">
                  <h3 className="font-semibold text-sm">
                    {format(currentMonth, "MMMM yyyy")}
                  </h3>
                  <div className="flex gap-2">
                    <button
                      onClick={() =>
                        setCurrentMonth(subMonths(currentMonth, 1))
                      }
                      className="w-7 h-7 rounded bg-gray-100 flex items-center justify-center"
                    >
                      <ChevronLeft size={14} />
                    </button>
                    <button
                      onClick={() =>
                        setCurrentMonth(addMonths(currentMonth, 1))
                      }
                      className="w-7 h-7 rounded bg-gray-100 flex items-center justify-center"
                    >
                      <ChevronRight size={14} />
                    </button>
                  </div>
                </div>

                <div className="grid grid-cols-7 px-6 text-center text-gray-500 text-sm">
                  {["S", "M", "T", "W", "T", "F", "S"].map((day) => (
                    <div key={day} className="py-2">
                      {day}
                    </div>
                  ))}
                </div>

                <div className="px-6 pb-1">
                  <div className="grid grid-cols-7 gap-y-2 text-center">
                    {(() => {
                      const monthStart = startOfMonth(currentMonth);
                      const monthEnd = endOfMonth(monthStart);
                      const startDate = startOfWeek(monthStart);
                      const endDate = endOfWeek(monthEnd);
                      const rows = [];
                      let day = startDate;
                      while (day <= endDate) {
                        rows.push(day);
                        day = addDays(day, 1);
                      }
                      return rows.map((date, idx) => (
                        <button
                          key={idx}
                          onClick={() => setSelectedDate(date)}
                          className={`w-8 h-8 mx-auto rounded-lg text-sm transition
                            ${
                              isSameDay(date, selectedDate)
                                ? "bg-blue-500 text-white"
                                : isSameMonth(date, currentMonth)
                                  ? "text-gray-700 hover:bg-gray-100"
                                  : "text-gray-300"
                            }
                          `}
                        >
                          {format(date, "d")}
                        </button>
                      ));
                    })()}
                  </div>
                </div>

                <div className="border-t px-6 py-4">
                  <p className="text-xs text-gray-500 mb-4">
                    *You can choose multiple date
                  </p>
                  <div className="flex justify-center">
                    <button
                      onClick={applyDateFilter}
                      className="bg-blue-500 text-white px-8 py-2 rounded-md text-sm"
                    >
                      Apply Now
                    </button>
                  </div>
                </div>
              </div>
            )}
          </div>

          {/* Order Type */}
          <div className="relative flex-shrink-0">
            <button
              onClick={() => {
                setShowType(!showType);
                setShowStatus(false);
              }}
              className="w-[140px] h-full border-r px-5 flex items-center justify-between text-sm"
            >
              Order Type
              <ChevronDown size={14} />
            </button>

            {showType && (
              <div className="absolute top-[58px] left-0 z-50 w-[390px] bg-white rounded-3xl shadow-xl border">
                <div className="p-5">
                  <h3 className="font-semibold text-lg mb-5">
                    Select Order Type
                  </h3>
                  <div className="grid grid-cols-3 gap-2">
                    {orderTypes.map((type) => (
                      <button
                        key={type}
                        onClick={() => toggleType(type)}
                        className={`w-full h-7 rounded-full text-xs border transition ${
                          selectedTypes.includes(type)
                            ? "bg-blue-500 text-white border-blue-500"
                            : "border-gray-300 hover:border-blue-400"
                        }`}
                      >
                        {type}
                      </button>
                    ))}
                  </div>
                </div>
                <div className="border-t p-5">
                  <h3 className="text-xs text-gray-500 mb-5">
                    *You can choose multiple order types
                  </h3>
                  <button
                    onClick={applyTypeFilter}
                    className="mx-auto block bg-blue-500 text-white px-8 py-2 rounded-md"
                  >
                    Apply Now
                  </button>
                </div>
              </div>
            )}
          </div>

          {/* Status */}
          <div className="relative flex-shrink-0">
            <button
              onClick={() => {
                setShowStatus(!showStatus);
                setShowType(false);
              }}
              className="w-[140px] h-full border-r px-5 flex items-center justify-between text-sm"
            >
              Order Status
              <ChevronDown size={14} />
            </button>

            {showStatus && (
              <div className="absolute top-[58px] left-0 z-50 w-[280px] bg-white rounded-2xl shadow-xl border overflow-hidden">
                <div className="p-5">
                  <h3 className="font-semibold text-lg mb-5">Select Status</h3>
                  <div className="grid grid-cols-3 gap-2">
                    {statuses.map((status) => (
                      <button
                        key={status}
                        onClick={() => toggleStatus(status)}
                        className={`w-full h-8 rounded-full text-xs border transition ${
                          selectedStatuses.includes(status)
                            ? "bg-blue-500 text-white border-blue-500"
                            : "border-gray-300 hover:border-blue-400"
                        }`}
                      >
                        {status}
                      </button>
                    ))}
                  </div>
                </div>
                <div className="border-t p-5">
                  <p className="text-xs text-gray-500 mb-5">
                    *You can choose multiple statuses
                  </p>
                  <button
                    onClick={applyStatusFilter}
                    className="mx-auto block bg-blue-500 text-white px-8 py-2 rounded-md text-sm"
                  >
                    Apply Now
                  </button>
                </div>
              </div>
            )}
          </div>

          {/* Reset */}
          <button
            onClick={resetFilters}
            className="flex-1 min-w-[120px] h-full flex items-center justify-center gap-2 text-red-500 text-sm font-medium"
          >
            <RotateCcw size={14} />
            Reset Filter
          </button>
        </div>
      </div>

      {/* Mobile Filter Button */}
      <div className="md:hidden flex items-center justify-between gap-3 mb-4">
        <button
          onClick={() => setIsMobileFilterOpen(true)}
          className="flex-1 h-[42px] bg-white border rounded-lg px-4 flex items-center justify-between text-sm"
        >
          <div className="flex items-center gap-2">
            <Filter size={16} />
            <span>Filters</span>
            {getActiveFilterCount() > 0 && (
              <span className="bg-blue-500 text-white text-xs w-5 h-5 rounded-full flex items-center justify-center">
                {getActiveFilterCount()}
              </span>
            )}
          </div>
          <ChevronDown size={14} />
        </button>

        {getActiveFilterCount() > 0 && (
          <button
            onClick={resetFilters}
            className="h-[42px] px-4 border rounded-lg text-red-500 text-sm flex items-center gap-1"
          >
            <RotateCcw size={14} />
            Reset
          </button>
        )}
      </div>

      {/* Active Filters Display Mobile */}
      <div className="md:hidden flex flex-wrap gap-2 mb-4">
        {selectedDate && (
          <span className="bg-blue-50 text-blue-700 text-xs px-3 py-1 rounded-full flex items-center gap-1">
            {format(selectedDate, "dd MMM yyyy")}
            <button onClick={() => setSelectedDate(null)} className="ml-1">
              <X size={12} />
            </button>
          </span>
        )}
        {selectedTypes.map((type) => (
          <span
            key={type}
            className="bg-blue-50 text-blue-700 text-xs px-3 py-1 rounded-full flex items-center gap-1"
          >
            {type}
            <button onClick={() => toggleType(type)} className="ml-1">
              <X size={12} />
            </button>
          </span>
        ))}
        {selectedStatuses.map((status) => (
          <span
            key={status}
            className="bg-blue-50 text-blue-700 text-xs px-3 py-1 rounded-full flex items-center gap-1"
          >
            {status}
            <button onClick={() => toggleStatus(status)} className="ml-1">
              <X size={12} />
            </button>
          </span>
        ))}
      </div>

      {/* Mobile Filter Modal */}
      {isMobileFilterOpen && <MobileFilterModal />}

      {/* Table */}
      <div className="bg-white border rounded-2xl overflow-hidden">
        <div className="overflow-x-auto">
          <table className="w-full min-w-[900px]">
            <thead className="text-sm bg-gray-50 border-b">
              <tr>
                <th className="text-left px-6 py-2">ID</th>
                <th className="text-left px-6 py-2">NAME</th>
                <th className="text-left px-6 py-2">ADDRESS</th>
                <th className="text-left px-6 py-2">DATE</th>
                <th className="text-left px-6 py-2">TYPE</th>
                <th className="text-left px-6 py-2">STATUS</th>
              </tr>
            </thead>

            <tbody>
              {currentOrders.length > 0 ? (
                currentOrders.map((order) => (
                  <tr
                    key={order.id}
                    className="border-b hover:bg-gray-50 transition"
                  >
                    <td className="px-6 py-4">{order.id}</td>
                    <td className="px-6 py-4">{order.name}</td>
                    <td className="px-6 py-4">{order.address}</td>
                    <td className="px-6 py-4">{order.date}</td>
                    <td className="px-6 py-4">{order.type}</td>
                    <td className="px-6 py-4">
                      <span
                        className={`inline-flex items-center justify-center min-w-[102px] h-[24px] rounded-md text-xs font-semibold ${statusStyle(
                          order.status,
                        )}`}
                      >
                        {order.status}
                      </span>
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="6" className="text-center py-8 text-gray-500">
                    No orders found matching the selected filters
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Pagination */}
      {filteredOrders.length > 0 && (
        <div className="flex flex-col md:flex-row justify-between items-center mt-5 gap-4">
          <span className="text-sm text-gray-500">
            Showing {(currentPage - 1) * perPage + 1}-
            {Math.min(currentPage * perPage, filteredOrders.length)} of{" "}
            {filteredOrders.length}
          </span>

          <div className="flex items-center gap-1 flex-wrap justify-center">
            <button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((p) => p - 1)}
              className="w-9 h-9 border rounded-lg flex items-center justify-center disabled:opacity-40"
            >
              <ChevronLeft size={18} />
            </button>

            {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
              <button
                key={page}
                onClick={() => setCurrentPage(page)}
                className={`w-9 h-9 rounded-lg ${
                  currentPage === page
                    ? "bg-blue-600 text-white"
                    : "border hover:bg-gray-50"
                }`}
              >
                {page}
              </button>
            ))}

            <button
              disabled={currentPage === totalPages}
              onClick={() => setCurrentPage((p) => p + 1)}
              className="w-9 h-9 border rounded-lg flex items-center justify-center disabled:opacity-40"
            >
              <ChevronRight size={18} />
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default OrderListsPage;
