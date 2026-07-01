import { useState } from "react";
import CalendarHeader from "../components/calendar/CalendarHeader";
import CalendarGrid from "../components/calendar/CalendarGrid";
import EventSidebar from "../components/calendar/EventSidebar";

export default function CalendarPage() {
  const [currentDate, setCurrentDate] = useState(new Date());

  return (
    <div className="w-full">
      {/* Page Title */}
      <h1 className="text-3xl font-bold text-gray-700 mb-6">Calendar</h1>

      {/* Main Card */}
      <div className="rounded-2xl border border-gray-200 shadow-sm">
        {/* Two Columns */}
        <div className="flex flex-col lg:flex-row gap-10">
          {/* LEFT SIDEBAR */}
          <div className="rounded-2xl w-full lg:w-[270px] flex-shrink-0">
            <EventSidebar />
          </div>

          {/* RIGHT SECTION */}
          <div className="bg-white rounded-2xl p-5 flex-1 min-w-0">
            <CalendarHeader
              currentDate={currentDate}
              setCurrentDate={setCurrentDate}
            />

            <div className="mt-5">
              <CalendarGrid currentDate={currentDate} />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
