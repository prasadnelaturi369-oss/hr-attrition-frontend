import React, { useState, useEffect, useCallback } from "react";
import {
  Plus,
  Star,
  Trash2,
  Check,
  X,
  Clock,
  AlertTriangle,
} from "lucide-react";

export default function TodoPage() {
  const [tasks, setTasks] = useState([
    { id: 1, text: "Meeting with CEO", completed: false, starred: false },
    {
      id: 2,
      text: "Pick up kids from school",
      completed: false,
      starred: true,
    },
    { id: 3, text: "Shopping with Brother", completed: false, starred: false },
    { id: 4, text: "Review with HR", completed: true, starred: false },
    { id: 5, text: "Going to Dia's School", completed: false, starred: false },
    { id: 6, text: "Check design files", completed: false, starred: true },
    { id: 7, text: "Update File", completed: false, starred: false },
  ]);

  const [newTask, setNewTask] = useState("");
  const [showAddTask, setShowAddTask] = useState(false);

  const [deleteWarning, setDeleteWarning] = useState(null);
  const [countdown, setCountdown] = useState(5);
  const [deleteTimer, setDeleteTimer] = useState(null);
  const [pendingDeleteId, setPendingDeleteId] = useState(null);

  const toggleStar = (id) => {
    setTasks(
      tasks.map((task) =>
        task.id === id ? { ...task, starred: !task.starred } : task,
      ),
    );
  };

  const toggleTask = (id) => {
    setTasks(
      tasks.map((task) =>
        task.id === id ? { ...task, completed: !task.completed } : task,
      ),
    );
  };

  const addTask = () => {
    if (newTask.trim()) {
      setTasks([
        ...tasks,
        {
          id: Date.now(),
          text: newTask.trim(),
          completed: false,
          starred: false,
        },
      ]);
      setNewTask("");
      setShowAddTask(false);
    }
  };

  const initiateDelete = (id) => {
    setPendingDeleteId(id);
    setCountdown(5);
    const task = tasks.find((t) => t.id === id);
    setDeleteWarning({ id, text: task?.text || "Task" });
  };

  const cancelDelete = () => {
    setDeleteWarning(null);
    setPendingDeleteId(null);
    setCountdown(5);
    if (deleteTimer) {
      clearInterval(deleteTimer);
      setDeleteTimer(null);
    }
  };

  const permanentDelete = useCallback((id) => {
    setTasks((prevTasks) => prevTasks.filter((task) => task.id !== id));
    setDeleteWarning(null);
    setPendingDeleteId(null);
    setCountdown(5);
    if (deleteTimer) {
      clearInterval(deleteTimer);
      setDeleteTimer(null);
    }
  }, [deleteTimer]);

  useEffect(() => {
    if (deleteWarning) {
      const timer = setInterval(() => {
        setCountdown((prev) => {
          if (prev <= 1) {
            permanentDelete(deleteWarning.id);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);

      setDeleteTimer(timer);

      return () => {
        clearInterval(timer);
      };
    }
  }, [deleteWarning, permanentDelete]);

  return (
    <div className="relative">
      <div className="flex sm:flex-row justify-between items-start sm:items-center gap-4 mb-6">
        <h2 className="text-2xl font-semibold text-gray-800">To-Do List</h2>
        <button
          onClick={() => setShowAddTask(true)}
          className="bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center space-x-2 text-sm hover:bg-blue-700 transition-colors whitespace-nowrap"
        >
          <Plus size={18} />
          <span>Add New Task</span>
        </button>
      </div>

      {showAddTask && (
        <div className="mb-6 bg-blue-50 p-4 rounded-lg border border-blue-200">
          <div className="flex gap-2">
            <input
              type="text"
              value={newTask}
              onChange={(e) => setNewTask(e.target.value)}
              placeholder="Enter task name..."
              className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              onKeyPress={(e) => e.key === "Enter" && addTask()}
              autoFocus
            />
            <button
              onClick={addTask}
              className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition-colors"
            >
              Add
            </button>
            <button
              onClick={() => {
                setShowAddTask(false);
                setNewTask("");
              }}
              className="px-4 py-2 bg-gray-200 text-gray-700 rounded-lg hover:bg-gray-300 transition-colors"
            >
              Cancel
            </button>
          </div>
        </div>
      )}

      <div className="space-y-5">
        {tasks.map((task) => {
          const isPendingDelete = pendingDeleteId === task.id;

          return (
            <div
              key={task.id}
              className={`rounded-2xl border transition-all duration-300 px-6 py-5 flex items-center justify-between
                ${
                  task.completed
                    ? "bg-blue-600 border-blue-600 text-white shadow-lg"
                    : "bg-white border-gray-200 hover:shadow-md"
                }
                ${isPendingDelete ? "opacity-40 blur-[1px] scale-[0.98]" : ""}
              `}
            >
              <div
                className="flex items-center gap-5 cursor-pointer flex-1"
                onClick={() => !isPendingDelete && toggleTask(task.id)}
              >
                <div
                  className={`w-6 h-6 rounded border flex items-center justify-center transition
                    ${
                      task.completed
                        ? "bg-blue-500 border-white"
                        : "border-gray-300 bg-white"
                    }
                    ${isPendingDelete ? "pointer-events-none" : ""}
                  `}
                >
                  {task.completed && <Check size={15} className="text-white" />}
                </div>

                <span
                  className={`text-[15px] font-medium ${
                    task.completed ? "text-white" : "text-gray-700"
                  }`}
                >
                  {task.text}
                </span>
              </div>

              <div className="flex items-center gap-5">
                {task.completed ? (
                  <button
                    onClick={() => initiateDelete(task.id)}
                    className="w-9 h-9 rounded-full border border-blue-500 bg-blue-500 flex items-center justify-center transition hover:bg-blue-700 text-white"
                  >
                    <Trash2 size={16} />
                  </button>
                ) : (
                  <>
                    <button
                      onClick={() => toggleStar(task.id)}
                      className="transition"
                    >
                      <Star
                        size={21}
                        className={
                          task.starred
                            ? "fill-yellow-400 text-yellow-400"
                            : "text-gray-300"
                        }
                      />
                    </button>

                    <button
                      onClick={() => initiateDelete(task.id)}
                      className="w-9 h-9 rounded-full border border-gray-300 flex items-center justify-center transition text-gray-400 hover:text-red-500"
                    >
                      <X size={16} />
                    </button>
                  </>
                )}
              </div>
            </div>
          );
        })}
      </div>

      {deleteWarning && (
        <>
          <div
            className="fixed inset-0 bg-black/30 backdrop-blur-sm z-40 animate-in fade-in duration-300"
            onClick={cancelDelete}
          />

          <div className="fixed inset-0 flex items-center justify-center z-50 p-4 animate-in zoom-in-95 duration-300">
            <div className="bg-white rounded-2xl shadow-2xl max-w-md w-full overflow-hidden">
              <div className="bg-gradient-to-r from-red-500 to-red-600 px-4 py-3">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-white/20 flex items-center justify-center backdrop-blur">
                    <AlertTriangle className="w-5 h-5 text-white" />
                  </div>
                  <div>
                    <h3 className="text-white font-semibold text-lg">
                      Delete Task?
                    </h3>
                    <p className="text-white/80 text-sm">
                      This action cannot be undone
                    </p>
                  </div>
                </div>
              </div>

              <div className="px-4 py-3">
                <div className="flex items-center gap-3 bg-gray-50 rounded-xl p-3 mb-4">
                  <div className="flex-1">
                    <p className="text-xs text-gray-500">Task to delete</p>
                    <p className="font-medium text-gray-800 truncate">
                      "{deleteWarning.text}"
                    </p>
                  </div>
                  <div className="flex items-center gap-2 bg-white px-3 py-1.5 rounded-lg border border-gray-200">
                    <Clock className="w-4 h-4 text-red-500 animate-pulse" />
                    <span className="font-mono font-bold text-red-600 text-lg">
                      {countdown}s
                    </span>
                  </div>
                </div>

                <div className="space-y-3">
                  <div className="w-full h-1.5 bg-gray-200 rounded-full overflow-hidden">
                    <div
                      className="h-full bg-gradient-to-r from-red-400 to-red-600 transition-all duration-1000 ease-linear rounded-full"
                      style={{ width: `${(countdown / 5) * 100}%` }}
                    />
                  </div>

                  <div className="flex items-center gap-2 text-xs text-gray-500">
                    <div className="flex-1">
                      {countdown > 3
                        ? "⏳ Waiting..."
                        : countdown > 1
                          ? "⚠️ Almost deleted!"
                          : "🗑️ Deleting..."}
                    </div>
                    <span className="font-mono">{countdown}/5</span>
                  </div>
                </div>
              </div>

              <div className="px-6 py-4 bg-gray-50 border-t border-gray-100 flex gap-3">
                <button
                  onClick={cancelDelete}
                  className="flex-1 px-2 py-1.5 bg-white border-2 border-gray-200 rounded-xl text-gray-700 font-medium hover:bg-gray-50 hover:border-gray-300 transition-all duration-200 flex items-center justify-center gap-2"
                >
                  <X className="w-4 h-4" />
                  Cancel
                </button>
                <button
                  onClick={() => permanentDelete(deleteWarning.id)}
                  className="flex-1 px-2 py-1.5 bg-gradient-to-r from-red-500 to-red-600 text-white rounded-xl font-medium hover:from-red-600 hover:to-red-700 transition-all duration-200 shadow-lg shadow-red-500/30 flex items-center justify-center gap-2"
                >
                  <Trash2 className="w-4 h-4" />
                  Delete Now
                </button>
              </div>
            </div>
          </div>

          <style jsx>{`
            @keyframes fade-in {
              from { opacity: 0; }
              to { opacity: 1; }
            }
            @keyframes zoom-in-95 {
              from { 
                opacity: 0;
                transform: scale(0.95);
              }
              to { 
                opacity: 1;
                transform: scale(1);
              }
            }
            .animate-in {
              animation-duration: 300ms;
              animation-fill-mode: both;
            }
            .fade-in {
              animation-name: fade-in;
            }
            .zoom-in-95 {
              animation-name: zoom-in-95;
            }
          `}</style>
        </>
      )}
    </div>
  );
}