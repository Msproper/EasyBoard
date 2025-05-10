

import Sidebar from "@/components/sidebar/sidebar";
import { Searcher } from "@/components/Search/Search";
import { LayoutList, Star, Search, Users} from 'lucide-react';
import { useState, useContext, useEffect } from "react";
import { AppContext } from "@/utils/context";
import { useSearchParams } from "react-router-dom";
import { motion, AnimatePresence } from "framer-motion";
import UserBoards from "@/components/UserBoards/UserBoards";
import { subscribeToChannel } from "@/api/socket/subscribeToChannel";
import { store } from "@/reduxStore";
import { useSelector } from "react-redux";


export const categories = [
  { title:"my", text: "Доски", icon: <LayoutList size={18} /> },
  { title:"likes", text: "Избранное", icon: <Star size={18} /> },
  { title:"search", text: "Найти", icon: <Search size={18} /> },
  { title:"groups", text: "Группы", icon: <Users size={18} /> },
  { title:"friends", text: "Друзья", icon: <Users size={18} /> },
];

const DashboardPage = () => {
  const connected = useSelector((state) => state.websocket.connected);
  const [searchParams, setSearchParams] = useSearchParams();
  const { isSidebarOpen, setSidebarOpen } = useContext(AppContext);
  const [select, onSelect] = useState(searchParams.get('category') || 'my')
  const {showAlert} = useContext(AppContext)

  // useEffect(() => {
  //   console.log(connected)
  //   if (connected){
  //     const onInviteResponse = (data) => {
        
  //     };
    
  //     const subscription = subscribeToChannel(store, '/user/queue/global/invites', onInviteResponse);
    
  //     return () => {
  //       subscription.unsubscribe();
  //     };
  //   }
  // }, [connected]);
  

  const handleCategoryChange = (newCategory) => {
    const newParams = new URLSearchParams(searchParams);
    newParams.set('category', newCategory);
    setSearchParams(newParams);
  };

  useEffect(()=>handleCategoryChange(select), [select])
  
  return (
    <div className="flex min-h-screen">

      <div className={`flex-1 transition-all duration-100 ${isSidebarOpen ? "ml-72" : "mr-0"}`}>
        {select === 'my' && <UserBoards />}
        {select === 'search' && <Searcher />}
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
