

import Sidebar from "@/components/Sidebar/Sidebar.jsx";
import { LayoutList, Star, Search, Users} from 'lucide-react';
import { useState, useContext, useEffect } from "react";
import { AppContext } from "@/utils/context";
import { useSearchParams } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import GroupsPage from "../groups/GroupsPage";
import MyBoards from "../SearchedPages/MyBoards";
import ExploreBoards from "../SearchedPages/ExploredBoards";


export const categories = [
  { title:"my", text: "Доски", icon: <LayoutList size={18} /> },
  { title:"likes", text: "Избранное", icon: <Star size={18} /> },
  { title:"search", text: "Найти", icon: <Search size={18} /> },
  { title:"groups", text: "Группы", icon: <Users size={18} /> },
  { title:"friends", text: "Друзья", icon: <Users size={18} /> },
];

const DashboardPage = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const { isSidebarOpen, setSidebarOpen } = useContext(AppContext);
  const [select, onSelect] = useState(searchParams.get('category') || 'my')

  const handleCategoryChange = (newCategory) => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('category', newCategory);
    setSearchParams(newParams);
  };

  useEffect(()=>handleCategoryChange(select), [select])
  
  return (
    <div className="flex min-h-screen">

      <div className={`flex-1 transition-all duration-100 ${isSidebarOpen ? "ml-72" : "mr-0"}`}>
        {select === 'my' && <MyBoards />}
        {select === 'search' && <ExploreBoards/>}
        {select === 'groups' && <GroupsPage />}
      </div>
      <AnimatePresence>
        {isSidebarOpen && (
          <>
            <div
              onClick={() => setSidebarOpen(false)}
              className="fixed inset-0 z-40"
            />
            <motion.aside
              initial={{ x: -100, opacity: 0 }}
              animate={{ x: 0, opacity: 1 }}
              exit={{ x: -100, opacity: 0 }}
              transition={{ type: "spring", stiffness: 1000, damping: 50 }}
              className="absolute left-0 top-18 w-72 bg-gradient-to-br from-blue-50 to-blue-100 h-full shadow-2xl z-50 p-2"
            >
              <Sidebar  selected={select} onSelect={onSelect} />
            </motion.aside>
          </>
        )}
      </AnimatePresence>
  </div>
  );
};

export default DashboardPage;
