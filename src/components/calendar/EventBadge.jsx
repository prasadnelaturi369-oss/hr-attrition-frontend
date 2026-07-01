export default function EventBadge({
  title,
  color = "blue",
  isClickable = false,
}) {
  const colors = {
    blue: {
      bg: "bg-blue-50",
      border: "border-l-2 sm:border-l-4 border-blue-600",
      text: "text-blue-700",
      hover: "hover:bg-blue-100",
    },
    purple: {
      bg: "bg-purple-50",
      border: "border-l-2 sm:border-l-4 border-purple-600",
      text: "text-purple-700",
      hover: "hover:bg-purple-100",
    },
    pink: {
      bg: "bg-pink-50",
      border: "border-l-2 sm:border-l-4 border-pink-600",
      text: "text-pink-700",
      hover: "hover:bg-pink-100",
    },
    orange: {
      bg: "bg-orange-50",
      border: "border-l-2 sm:border-l-4 border-orange-500",
      text: "text-orange-700",
      hover: "hover:bg-orange-100",
    },
    green: {
      bg: "bg-green-50",
      border: "border-l-2 sm:border-l-4 border-green-600",
      text: "text-green-700",
      hover: "hover:bg-green-100",
    },
    red: {
      bg: "bg-red-50",
      border: "border-l-2 sm:border-l-4 border-red-600",
      text: "text-red-700",
      hover: "hover:bg-red-100",
    },
  };

  const style = colors[color] || colors.blue;

  return (
    <div
      className={`
        event-badge
        w-full
        ${style.bg}
        ${style.border}
        ${style.text}
        px-1 sm:px-2
        py-0.5 sm:py-1
        text-[8px] sm:text-[11px]
        font-medium
        rounded-none
        truncate
        leading-tight
        transition-colors
        ${isClickable ? `${style.hover} cursor-pointer` : "cursor-default"}
      `}
      data-badge="true"
    >
      {title}
    </div>
  );
}
