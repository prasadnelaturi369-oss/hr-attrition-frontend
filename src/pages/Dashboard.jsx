import React, { useState } from "react";
import Sidebar from "../components/layout/Sidebar";
import Header from "../components/layout/Header";
import RevenueChart from "../components/dashboard/RevenueChart";
import SalesAnalytics from "../components/dashboard/SalesAnalytics";
import FeaturedProduct from "../components/dashboard/FeaturedProduct";
import CustomersCard from "../components/dashboard/CustomersCard";

const Dashboard = () => {
  const [isCollapsed, setIsCollapsed] = useState(false);
  const [isOpen, setIsOpen] = useState(false);

  const toggleSidebar = () => {
    if (window.innerWidth < 1024) {
      setIsOpen(!isOpen);
    } else {
      setIsCollapsed(!isCollapsed);
    }
  };

  return (
    <div className="flex h-screen bg-gray-200 overflow-hidden">
      <Sidebar
        isOpen={isOpen}
        setIsOpen={setIsOpen}
        isCollapsed={isCollapsed}
      />
      <div className="flex-1 flex flex-col overflow-hidden">
        <Header onMenuClick={toggleSidebar} isCollapsed={isCollapsed} />
        <main className="flex-1 overflow-y-auto p-4 md:p-6">
          {/* Dashboard Heading */}
          <div className="mb-6">
            <h1 className="text-2xl md:text-3xl font-bold text-gray-700">
              Dashboard
            </h1>
          </div>

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
        </main>
      </div>
    </div>
  );
};

export default Dashboard;
