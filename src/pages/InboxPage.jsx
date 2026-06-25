import React, { useState } from "react";
import InboxSidebar from "../components/inbox/InboxSidebar";
import InboxToolbar from "../components/inbox/InboxToolbar";
import MailList from "../components/inbox/MailList";
import MailChatView from "../components/inbox/MailChatView";

const InboxPage = () => {
  const [currentView, setCurrentView] = useState("inbox");
  const [searchTerm, setSearchTerm] = useState("");
  const [starredEmails, setStarredEmails] = useState([]);
  const [selectedEmail, setSelectedEmail] = useState(null);

  return (
    <div>
      <h1 className="text-2xl sm:text-3xl font-bold text-gray-800 mb-4 sm:mb-6">
        {currentView}
      </h1>

      <div className="flex flex-col xl:flex-row gap-6">
        <InboxSidebar currentView={currentView} onNavigate={setCurrentView} />

        <div className="bg-white rounded-2xl p-4 md:p-6 shadow-sm flex-1">
          {selectedEmail ? (
            <MailChatView
              email={selectedEmail}
              onBack={() => setSelectedEmail(null)}
            />
          ) : (
            <>
              <InboxToolbar
                searchTerm={searchTerm}
                setSearchTerm={setSearchTerm}
              />

              <MailList
                currentView={currentView}
                searchTerm={searchTerm}
                starredEmails={starredEmails}
                setStarredEmails={setStarredEmails}
                setSelectedEmail={setSelectedEmail}
              />
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default InboxPage;
