import { useState, useRef, useEffect } from "react";
import CalendarCell from "./CalendarCell";
import { calendarDays } from "../../data/calendarData";
import EventPopup from "./EventPopup";

const weekDays = ["MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"];

export default function CalendarGrid({ currentDate }) {
  const [activeEvent, setActiveEvent] = useState(null);
  const [popupPosition, setPopupPosition] = useState({ top: 0, left: 0 });
  const popupRef = useRef(null);
  const calendarRef = useRef(null);

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth();

  const firstDay = new Date(year, month, 1);
  const lastDay = new Date(year, month + 1, 0);

  let startDay = firstDay.getDay();
  startDay = startDay === 0 ? 6 : startDay - 1;

  const previousMonthDays = new Date(year, month, 0).getDate();

  const days = [];

  for (let i = startDay; i > 0; i--) {
    days.push({
      date: previousMonthDays - i + 1,
      currentMonth: false,
      events: [],
    });
  }

  for (let i = 1; i <= lastDay.getDate(); i++) {
    const event = calendarDays.find((item) => item.date === i)?.events || [];
    days.push({
      date: i,
      currentMonth: true,
      events: event,
    });
  }

  let next = 1;
  while (days.length < 42) {
    days.push({
      date: next++,
      currentMonth: false,
      events: [],
    });
  }

  const calculatePopupPosition = (badgeElement) => {
    if (!badgeElement) return { top: 0, left: 0 };

    const rect = badgeElement.getBoundingClientRect();
    const popupWidth = Math.min(280, window.innerWidth - 24);
    const popupHeight = 380;
    const padding = 12;

    // Start with position below the badge
    let left = rect.left + rect.width / 2 - popupWidth / 2;
    let top = rect.bottom + 8;

    // Horizontal bounds
    if (left < padding) {
      left = padding;
    } else if (left + popupWidth > window.innerWidth - padding) {
      left = window.innerWidth - popupWidth - padding;
    }

    // Vertical bounds - prefer below, then above
    const spaceBelow = window.innerHeight - rect.bottom;
    const spaceAbove = rect.top;

    if (spaceBelow < popupHeight + 8 && spaceAbove > popupHeight + 8) {
      // Show above
      top = rect.top - popupHeight - 8;
    } else if (spaceBelow < popupHeight + 8 && spaceAbove < popupHeight + 8) {
      // Center in viewport
      top = (window.innerHeight - popupHeight) / 2;
    }

    // Ensure top doesn't go above viewport
    if (top < padding) {
      top = padding;
    }

    // Ensure bottom doesn't go below viewport
    if (top + popupHeight > window.innerHeight - padding) {
      top = window.innerHeight - popupHeight - padding;
    }

    return { top, left };
  };

  const handleEventClick = (cellIndex, eventIndex, badgeElement) => {
    const eventKey = `${cellIndex}-${eventIndex}`;

    if (activeEvent === eventKey) {
      setActiveEvent(null);
      return;
    }

    if (badgeElement) {
      const position = calculatePopupPosition(badgeElement);
      setPopupPosition(position);
      setActiveEvent(eventKey);
    }
  };

  // Close popup on outside click
  useEffect(() => {
    if (activeEvent === null) return;

    const handleClickOutside = (e) => {
      const popupElement = document.getElementById("global-popup");
      const badgeElements = document.querySelectorAll("[data-badge='true']");

      if (popupElement && popupElement.contains(e.target)) {
        return;
      }

      let clickedBadge = false;
      badgeElements.forEach((badge) => {
        if (badge.contains(e.target)) {
          clickedBadge = true;
        }
      });

      if (!clickedBadge) {
        setActiveEvent(null);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [activeEvent]);

  // Update position on scroll/resize
  useEffect(() => {
    if (activeEvent === null) return;

    const updatePosition = () => {
      const [cellIndex, eventIndex] = activeEvent.split("-").map(Number);
      const cellElements = document.querySelectorAll(".calendar-cell");

      if (cellElements[cellIndex]) {
        const badges = cellElements[cellIndex].querySelectorAll(
          "[data-badge='true']",
        );
        if (badges[eventIndex]) {
          const position = calculatePopupPosition(badges[eventIndex]);
          setPopupPosition(position);
        }
      }
    };

    window.addEventListener("scroll", updatePosition, { passive: true });
    window.addEventListener("resize", updatePosition, { passive: true });

    return () => {
      window.removeEventListener("scroll", updatePosition);
      window.removeEventListener("resize", updatePosition);
    };
  }, [activeEvent]);

  const getActiveEventData = () => {
    if (!activeEvent) return null;
    const [cellIndex, eventIndex] = activeEvent.split("-").map(Number);
    const events = days[cellIndex]?.events || [];
    return events[eventIndex] || null;
  };

  const handleClosePopup = () => {
    setActiveEvent(null);
  };

  return (
    <div ref={calendarRef} className="w-full overflow-x-auto relative">
      <div className="min-w-[320px] max-w-full rounded-xl border border-gray-200 overflow-hidden">
        <div className="grid grid-cols-7 bg-gray-50">
          {weekDays.map((day) => (
            <div
              key={day}
              className="h-10 sm:h-12 flex items-center justify-center text-[10px] sm:text-xs font-semibold text-gray-500 border-r last:border-r-0 border-b"
            >
              {day}
            </div>
          ))}
        </div>

        <div className="grid grid-cols-7">
          {days.map((day, index) => (
            <CalendarCell
              key={index}
              index={index}
              date={day.date}
              faded={!day.currentMonth}
              events={day.events}
              onEventClick={handleEventClick}
              isActive={
                activeEvent !== null && activeEvent.startsWith(`${index}-`)
              }
            />
          ))}
        </div>
      </div>

      {activeEvent !== null && (
        <div
          id="global-popup"
          ref={popupRef}
          className="fixed z-[9999]"
          style={{
            position: "fixed",
            top: `${popupPosition.top}px`,
            left: `${popupPosition.left}px`,
            width: Math.min(280, window.innerWidth - 24),
            maxWidth: "calc(100vw - 24px)",
            pointerEvents: "auto",
            animation: "fadeIn 0.15s ease-out",
          }}
          onClick={(e) => e.stopPropagation()}
        >
          <EventPopup
            open={true}
            event={getActiveEventData()}
            onClose={handleClosePopup}
          />
        </div>
      )}

      <style jsx>{`
        @keyframes fadeIn {
          from {
            opacity: 0;
            transform: translateY(-8px) scale(0.96);
          }
          to {
            opacity: 1;
            transform: translateY(0) scale(1);
          }
        }
      `}</style>
    </div>
  );
}
