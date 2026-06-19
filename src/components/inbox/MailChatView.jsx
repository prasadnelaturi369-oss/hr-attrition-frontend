import React from "react";
import {
  ArrowLeft,
  MoreVertical,
  Send,
  Paperclip,
  ChevronLeft,
} from "lucide-react";

const MailChatView = ({ email, onBack }) => {
  if (!email) return null;

  return (
    <div className="h-[700px] flex flex-col bg-white">
      <div className="border-b px-6 py-4 flex items-center">
        {/* Left */}
        <button onClick={onBack}>
          <ChevronLeft size={20} />
        </button>

        {/* Center */}
        <div className="pl-4 flex items-center justify-center gap-3">
          <h3 className="font-semibold text-lg">{email.sender}</h3>

          <span className="px-2 py-1 text-xs rounded bg-purple-100 text-purple-600">
            {email.label}
          </span>
        </div>

        {/* Right (optional actions) */}
        <div className="w-5"></div>
      </div>

      {/* Messages */}
      <div className="flex-1 overflow-y-auto py-4 space-y-4">
        {/* Left Message */}
        <div className="flex items-end gap-3">
          <img
            src={`https://ui-avatars.com/api/?name=${email.sender}&background=random`}
            alt={email.sender}
            className="w-10 h-10 rounded-full object-cover"
          />

          <div className="bg-white max-w-lg p-4 rounded-2xl shadow-sm">
            Hi, regarding your request, we have reviewed the details and will
            proceed shortly.
          </div>
        </div>

        {/* Right Message */}
        <div className="flex justify-end">
          <div className="bg-blue-600 text-white max-w-lg p-4 rounded-2xl">
            Thank you. Please keep me updated.
          </div>
        </div>

        {/* Left Message */}
        <div className="flex items-end gap-3">
          <img
            src={`https://ui-avatars.com/api/?name=${email.sender}&background=random`}
            alt={email.sender}
            className="w-10 h-10 rounded-full object-cover"
          />

          <div className="bg-white max-w-lg p-4 rounded-2xl shadow-sm">
            Sure. We will share the next update soon.
          </div>
        </div>
      </div>

      {/* Footer */}
      <div className="flex items-center gap-3 border-t-2 py-2">
        <input
          type="text"
          placeholder="Write message..."
          className="flex-1 outline-none"
        />

        <Paperclip size={18} className="text-gray-500 cursor-pointer" />

        <button className="bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center gap-2">
          <Send size={16} />
          Send
        </button>
      </div>
    </div>
  );
};

export default MailChatView;
