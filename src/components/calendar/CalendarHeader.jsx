import { ChevronLeft, ChevronRight } from "lucide-react";

export default function CalendarHeader({
  currentDate = new Date(),
  setCurrentDate = () => {},
}) {
  const handlePrevious = () => {
    setCurrentDate(
      new Date(currentDate.getFullYear(), currentDate.getMonth() - 1, 1),
    );
  };

  const handleNext = () => {
    setCurrentDate(
      new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 1),
    );
  };

  const handleToday = () => {
    setCurrentDate(new Date());
  };

  const monthYear = currentDate.toLocaleDateString("en-US", {
    month: "long",
    year: "numeric",
  });

  return (
    <div className="flex flex-col lg:flex-row lg:items-center lg:justify-between gap-5 mb-6">
      <div className="flex items-center gap-6 flex-wrap">
        <button
          onClick={handleToday}
          className="text-sm font-medium text-gray-500 hover:text-blue-600 transition"
        >
          Today
        </button>

        <div className="flex items-center gap-1">
          <button
            onClick={handlePrevious}
            className="w-9 h-9 hover:bg-gray-100 flex items-center justify-center transition"
          >
            <ChevronLeft size={18} />
          </button>

          <h2 className="text-2xl font-bold text-gray-800 min-w-[180px] text-center">
            {monthYear}
          </h2>

          <button
            onClick={handleNext}
            className="w-9 h-9 hover:bg-gray-100 flex items-center justify-center transition"
          >
            <ChevronRight size={18} />
          </button>
        </div>
      </div>

      <div className="inline-flex rounded-lg border border-gray-200 overflow-hidden self-start lg:self-auto">
        <button className="px-5 py-2 text-sm border-r border-gray-200 hover:bg-gray-50">
          Day
        </button>

        <button className="px-5 py-2 text-sm border-r border-gray-200 hover:bg-gray-50">
          Week
        </button>

        <button className="px-5 py-2 text-sm bg-blue-500 text-white font-medium">
          Month
        </button>
      </div>
    </div>
  );
}
