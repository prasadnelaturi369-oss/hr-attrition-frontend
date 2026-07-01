import React from "react";
import { Plus } from "lucide-react";

const events = [
  {
    id: 1,
    title: "Design Conference",
    date: "Today 07:19 AM",
    address: "56 Davion Mission Suite 157",
    location: "Meaghanberg",
    image:
      "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=100&h=100&fit=crop",
    members: [
      "https://randomuser.me/api/portraits/women/44.jpg",
      "https://randomuser.me/api/portraits/men/32.jpg",
      "https://randomuser.me/api/portraits/women/68.jpg",
    ],
    more: 15,
  },
  {
    id: 2,
    title: "Weekend Festival",
    date: "16 October 2019 at 5:00 PM",
    address: "853 Moore Flats Suite 158",
    location: "Sweden",
    image:
      "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=100&h=100&fit=crop",
    members: [
      "https://randomuser.me/api/portraits/men/21.jpg",
      "https://randomuser.me/api/portraits/women/12.jpg",
      "https://randomuser.me/api/portraits/men/65.jpg",
    ],
    more: 20,
  },
  {
    id: 3,
    title: "Glastonbury Festival",
    date: "20-22 October 2019 at 8:00 PM",
    address: "646 Walter Road Apt. 571",
    location: "Turks and Caicos Islands",
    image:
      "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=100&h=100&fit=crop",
    members: [
      "https://randomuser.me/api/portraits/women/55.jpg",
      "https://randomuser.me/api/portraits/men/45.jpg",
      "https://randomuser.me/api/portraits/men/52.jpg",
    ],
    more: 14,
  },
  {
    id: 4,
    title: "Ultra Europe 2019",
    date: "25 October 2019 at 10:00 PM",
    address: "506 Satterfield Tunnel Apt. 963",
    location: "San Marino",
    image:
      "https://images.unsplash.com/photo-1506157786151-b8491531f063?w=100&h=100&fit=crop",
    members: [
      "https://randomuser.me/api/portraits/women/25.jpg",
      "https://randomuser.me/api/portraits/men/11.jpg",
      "https://randomuser.me/api/portraits/women/32.jpg",
    ],
    more: 42,
  },
];

export default function EventSidebar() {
  return (
    <div className="w-full lg:w-[290px] bg-white border border-gray-200 rounded-2xl overflow-hidden shadow-sm">
      <div className="p-5 border-b border-gray-100">
        <button className="w-full h-12 rounded-xl bg-[#4F7DF3] hover:bg-[#3d6df0] text-white font-semibold flex items-center justify-center gap-2 transition">
          <Plus size={18} />
          Add New Event
        </button>
      </div>

      <div className="px-5 pt-5">
        <h2 className="text-xl font-bold text-gray-800">You are going to</h2>
      </div>

      <div className="px-5 py-4 space-y-7">
        {events.map((event) => (
          <div key={event.id} className="flex gap-4">
            <img
              src={event.image}
              alt={event.title}
              className="w-12 h-12 rounded-full object-cover flex-shrink-0"
            />

            <div className="flex-1">
              <h3 className="text-[15px] font-semibold text-gray-800">
                {event.title}
              </h3>

              <p className="text-xs text-gray-500 mt-1">{event.date}</p>

              <p className="text-xs text-gray-400 mt-2">{event.address}</p>

              <p className="text-xs text-gray-400 mt-1">{event.location}</p>

              <div className="flex items-center mt-3">
                <div className="flex">
                  {event.members.map((member, index) => (
                    <img
                      key={index}
                      src={member}
                      alt=""
                      className="w-7 h-7 rounded-full border-2 border-white object-cover -ml-2 first:ml-0"
                    />
                  ))}
                </div>

                <div className="ml-2 w-7 h-7 rounded-full border border-blue-400 text-blue-500 text-[11px] font-semibold flex items-center justify-center bg-blue-50">
                  {event.more}+
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>

      <div className="px-5 pb-5">
        <button className="w-full h-11 rounded-xl bg-gray-100 hover:bg-gray-200 text-gray-700 font-medium transition">
          See More
        </button>
      </div>
    </div>
  );
}
