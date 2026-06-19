import React, { useMemo, useState, useEffect } from "react";
import { ChevronLeft, ChevronRight, Star } from "lucide-react";

const emails = Array.from({ length: 26 }, (_, index) => ({
  id: index + 1,
  sender: [
    "Jullu Jalal",
    "Minerva Barnett",
    "Peter Lewis",
    "Ralph Edwards",
    "Arlene McCoy",
    "Leslie Alexander",
    "Jenny Wilson",
    "Robert Fox",
    "Kristin Watson",
    "Cameron Williamson",
  ][index % 10],
  label: ["Primary", "Work", "Friends", "Social"][index % 4],
  title: `Email Subject ${index + 1} - Important business update and notification`,
  time: `${8 + (index % 12)}:${String((index * 7) % 60).padStart(2, "0")}`,
}));

const MailList = ({
  currentView,
  searchTerm,
  starredEmails,
  setStarredEmails,
  setSelectedEmail,
}) => {
  const [selectedEmails, setSelectedEmails] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);

  const emailsPerPage = 10;

  const toggleCheckbox = (id) => {
    setSelectedEmails((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id],
    );
  };

  const toggleStar = (id) => {
    setStarredEmails((prev) =>
      prev.includes(id) ? prev.filter((item) => item !== id) : [...prev, id],
    );
  };

  const filteredEmails = useMemo(() => {
    let result = [...emails];

    if (currentView === "starred") {
      result = result.filter((mail) => starredEmails.includes(mail.id));
    }

    if (searchTerm.trim()) {
      const search = searchTerm.toLowerCase();

      result = result.filter(
        (mail) =>
          mail.sender.toLowerCase().includes(search) ||
          mail.title.toLowerCase().includes(search) ||
          mail.label.toLowerCase().includes(search),
      );
    }

    return result;
  }, [currentView, searchTerm, starredEmails]);

  useEffect(() => {
    setCurrentPage(1);
  }, [searchTerm, currentView]);

  const totalPages = Math.max(
    1,
    Math.ceil(filteredEmails.length / emailsPerPage),
  );

  const startIndex = (currentPage - 1) * emailsPerPage;

  const currentEmails = filteredEmails.slice(
    startIndex,
    startIndex + emailsPerPage,
  );

  const getLabelColor = (label) => {
    switch (label) {
      case "Primary":
        return "bg-green-100 text-green-600";
      case "Work":
        return "bg-orange-100 text-orange-600";
      case "Friends":
        return "bg-purple-100 text-purple-600";
      case "Social":
        return "bg-blue-100 text-blue-600";
      default:
        return "bg-gray-100 text-gray-600";
    }
  };

  return (
    <>
      <div className="bg-white border rounded-2xl overflow-hidden">
        {currentEmails.length === 0 ? (
          <div className="p-10 text-center text-gray-500">No emails found</div>
        ) : (
          currentEmails.map((mail) => (
            <div
              key={mail.id}
              onClick={() => setSelectedEmail(mail)}
              className={`cursor-pointer
  grid grid-cols-[25px_25px_140px_80px_1fr_70px]
  items-center gap-3 px-4 py-4 border-b
  hover:bg-gray-50 transition-all duration-200
  ${
    selectedEmails.includes(mail.id)
      ? "bg-blue-50 border-l-4 border-l-blue-500"
      : ""
  }`}
            >
              <input
                type="checkbox"
                checked={selectedEmails.includes(mail.id)}
                onChange={() => toggleCheckbox(mail.id)}
                onClick={(e) => e.stopPropagation()}
                className="accent-blue-500"
              />

              <Star
                size={18}
                fill={starredEmails.includes(mail.id) ? "currentColor" : "none"}
                onClick={(e) => {
                  e.stopPropagation();
                  toggleStar(mail.id);
                }}
                className={`cursor-pointer transition-colors ${
                  starredEmails.includes(mail.id)
                    ? "text-yellow-400"
                    : "text-gray-300 hover:text-yellow-400"
                }`}
              />

              <div className="truncate font-medium text-gray-800">
                {mail.sender}
              </div>

              <span
                className={`px-2 py-1 rounded-full text-xs font-medium w-fit ${getLabelColor(
                  mail.label,
                )}`}
              >
                {mail.label}
              </span>

              <div className="truncate text-gray-600">{mail.title}</div>

              <div className="text-right text-sm text-gray-500">
                {mail.time}
              </div>
            </div>
          ))
        )}
      </div>

      {filteredEmails.length > 0 && (
        <div className="flex flex-col md:flex-row items-center justify-between gap-4 mt-5">
          <span className="text-sm text-gray-500">
            {startIndex + 1} -
            {Math.min(startIndex + emailsPerPage, filteredEmails.length)} of{" "}
            {filteredEmails.length}
          </span>

          <div className="flex items-center gap-1 flex-wrap">
            <button
              disabled={currentPage === 1}
              onClick={() => setCurrentPage((prev) => prev - 1)}
              className="w-8 h-8 border rounded-lg flex items-center justify-center disabled:opacity-40 hover:bg-gray-50"
            >
              <ChevronLeft size={18} />
            </button>

            {Array.from({ length: totalPages }, (_, index) => index + 1).map(
              (page) => (
                <button
                  key={page}
                  onClick={() => setCurrentPage(page)}
                  className={`w-8 h-8 rounded-lg text-sm font-medium transition-all
            ${
              currentPage === page
                ? "bg-blue-600 text-white shadow"
                : "border text-gray-700 hover:bg-gray-50"
            }
          `}
                >
                  {page}
                </button>
              ),
            )}

            <button
              disabled={currentPage === totalPages}
              onClick={() => setCurrentPage((prev) => prev + 1)}
              className="w-8 h-8 border rounded-lg flex items-center justify-center disabled:opacity-40 hover:bg-gray-50"
            >
              <ChevronRight size={18} />
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default MailList;
