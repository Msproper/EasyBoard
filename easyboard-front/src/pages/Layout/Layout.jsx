import { useState } from 'react';
import { Header } from '../../components/Header/Header';
import { Outlet } from 'react-router-dom';
import { AppContext } from '@/utils/context';
import { Notification } from '@/components/Utils/Notification';
import { NotificationStack } from '../../components/Utils/InviteNotification';

const Layout = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  return (
    <div className="flex flex-col min-h-screen">
      <AppContext.Provider value={{ isSidebarOpen, setSidebarOpen}}>
        <NotificationStack/>
        <Notification></Notification>
        <Header />
        <Outlet/>
      </AppContext.Provider>
    </div>
  );
};

export default Layout