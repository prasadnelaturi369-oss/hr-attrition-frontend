import React, { useState } from "react";
import Sidebar from "../components/layout/Sidebar";
import Header from "../components/layout/Header";
import RevenueChart from "../components/dashboard/RevenueChart";
import SalesAnalytics from "../components/dashboard/SalesAnalytics";
import FeaturedProduct from "../components/dashboard/FeaturedProduct";
import CustomersCard from "../components/dashboard/CustomersCard";
import ProductsPage from "./ProductsPage";
import FavoritesPage from "./FavoritesPage";
import InboxPage from "./InboxPage";

const Dashboard = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isOpen, setIsOpen] = useState(false);
  const [currentPage, setCurrentPage] = useState("Dashboard");

  const toggleSidebar = () => {
    if (window.innerWidth < 1024) {
      setIsOpen(!isOpen);
    } else {
      setIsCollapsed(!isCollapsed);
    }
  };

  const handleNavigate = (page) => {
    setCurrentPage(page);
    if (window.innerWidth < 1024) {
      setIsOpen(false);
    }
  };

  const renderContent = () => {
    switch (currentPage) {
      case "Products":
        return <ProductsPage />;

      case "Favorites":
        return <FavoritesPage />;

      case "Inbox":
        return <InboxPage />;

      default:
        return (
          <>
            {/* Dashboard Heading */}
            <h1 className="text-3xl font-bold mb-6 text-gray-700">Dashboard</h1>

            {/* Revenue Chart */}
            <div className="gap-4 md:gap-6 mb-6">
              <RevenueChart />
            </div>

            {/* Bottom Row */}
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-4 md:gap-6">
              <div className="lg:col-span-1">
                <CustomersCard />
              </div>
              <div className="lg:col-span-1">
                <FeaturedProduct />
              </div>
              <div className="lg:col-span-1">
                <SalesAnalytics />
              </div>
            </div>
          </>
        );
    }
  };

  return (
    <div className="flex h-screen bg-gray-200 overflow-hidden">
      <Sidebar
        isOpen={isOpen}
        setIsOpen={setIsOpen}
        isCollapsed={isCollapsed}
        onNavigate={handleNavigate}
        currentPage={currentPage}
      />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header onMenuClick={toggleSidebar} isCollapsed={isCollapsed} />
        <main className="flex-1 overflow-y-auto p-4 md:p-6">
          {renderContent()}
        </main>
      </div>
    </div>
  );
};

export default Dashboard;
