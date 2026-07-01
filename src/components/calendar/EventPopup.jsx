import { MapPin, CalendarDays, X } from "lucide-react";

export default function EventPopup({ open, events = [], onClose }) {
  if (!open) return null;

  const eventData = events.length > 0 ? events[0] : { title: "Event" };

  return (
    <div className="relative w-full max-w-[280px] p-4 rounded-2xl bg-white shadow-2xl border border-gray-100 overflow-hidden">
      {onClose && (
        <button
          onClick={onClose}
          className="absolute top-2 right-2 p-1 rounded-full hover:bg-gray-100 transition-colors z-10"
        >
          <X className="w-4 h-4 text-gray-500" />
        </button>
      )}

      <img
        src="https://images.unsplash.com/photo-1492684223066-81342ee5ff30?w=600"
        alt="event"
        className="h-28 sm:h-36 w-full object-cover rounded-xl"
      />

      <div className="mt-3">
        <h3 className="text-base sm:text-lg font-bold text-gray-800 truncate">
          {eventData.title || "Design Conference"}
        </h3>

        <div className="mt-2 space-y-1.5">
          <div className="flex items-center gap-2 text-xs sm:text-sm text-gray-600">
            <CalendarDays className="w-3.5 h-3.5 sm:w-4 sm:h-4 flex-shrink-0" />
            <span>16 October 2019 · 5:00 PM</span>
          </div>

          <div className="flex items-center gap-2 text-xs sm:text-sm text-gray-600">
            <MapPin className="w-3.5 h-3.5 sm:w-4 sm:h-4 flex-shrink-0" />
            <span className="truncate">853 Moore Flats Suite 158</span>
          </div>
        </div>

        <div className="mt-4 flex items-center">
          <img
            src="https://randomuser.me/api/portraits/women/44.jpg"
            className="h-7 w-7 sm:h-8 sm:w-8 rounded-full border-2 border-white"
            alt=""
          />
          <img
            src="https://randomuser.me/api/portraits/men/32.jpg"
            className="-ml-2 sm:-ml-3 h-7 w-7 sm:h-8 sm:w-8 rounded-full border-2 border-white"
            alt=""
          />
          <img
            src="https://randomuser.me/api/portraits/women/68.jpg"
            className="-ml-2 sm:-ml-3 h-7 w-7 sm:h-8 sm:w-8 rounded-full border-2 border-white"
            alt=""
          />

          <div className="-ml-2 sm:-ml-3 flex h-7 w-7 sm:h-8 sm:w-8 items-center justify-center rounded-full border-2 border-white bg-blue-500 text-[10px] sm:text-xs text-white">
            +15
          </div>
        </div>
      </div>
    </div>
  );
}
