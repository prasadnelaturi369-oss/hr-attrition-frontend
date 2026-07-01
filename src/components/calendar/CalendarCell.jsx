import { useRef } from "react";
import EventBadge from "./EventBadge";

export default function CalendarCell({
  index,
  date,
  faded = false,
  events = [],
  onEventClick,
  isActive,
}) {
  const cellRef = useRef(null);

  const handleEventClick = (eventIndex, e) => {
    e.stopPropagation();
    // Pass the badge element for positioning
    const badgeElement =
      e.currentTarget.querySelector(".event-badge") || e.currentTarget;
    onEventClick(index, eventIndex, badgeElement);
  };

  return (
    <div
      ref={cellRef}
      className={`
        calendar-cell
        relative
        h-20 sm:h-28
        border-r
        border-b
        border-gray-200
        bg-white
        overflow-hidden
        ${faded ? "bg-gray-50" : ""}
        ${isActive ? "ring-2 ring-blue-500 ring-inset z-10" : ""}
      `}
    >
      <span
        className={`absolute top-1 right-2 text-[10px] sm:text-sm font-medium ${
          faded ? "text-gray-400" : "text-gray-700"
        }`}
      >
        {date}
      </span>

      <div className="mt-6 sm:mt-8 space-y-0.5 sm:space-y-1 px-0.5 sm:px-1">
        {events.slice(0, 3).map((event, eventIndex) => (
          <div
            key={eventIndex}
            onClick={(e) => handleEventClick(eventIndex, e)}
            className="cursor-pointer w-full"
          >
            <EventBadge
              title={event.title}
              color={event.color}
              isClickable={true}
            />
          </div>
        ))}
        {events.length > 3 && (
          <div className="text-[8px] sm:text-[10px] text-gray-500 font-medium px-1">
            +{events.length - 3} more
          </div>
        )}
      </div>
    </div>
  );
}
