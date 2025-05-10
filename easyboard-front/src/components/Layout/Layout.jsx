import { useState } from 'react';
import { Header } from '../header/header';
import { Outlet } from 'react-router-dom';
import { AppContext } from '@/utils/context';
import { Notification } from '../Utils/Notification';
import { NotificationStack } from '../Utils/InviteNotification';

const Layout = () => {
  const [isSidebarOpen, setSidebarOpen] = useState(false);
  const [showNotification, setShowNotification] = useState(false); 
  const [notificationType, setNotificationType] = useState(null);
  const [notificationMessage, setNotificationMessage] = useState('');

  const showAlert = (type, message)=>{
      setNotificationType(type)
      setNotificationMessage(message)
      setShowNotification(true)

    }

  
  

  return (
    <div className="flex flex-col min-h-screen">
      <AppContext.Provider value={{ isSidebarOpen, setSidebarOpen, showAlert}}>
      <NotificationStack
      />
        {showNotification && (
            <Notification
              type={notificationType}
              message={notificationMessage}
              onClose={() => setShowNotification(false)}
            />
          )}
        <Header />
        <Outlet/>
      </AppContext.Provider>
    </div>
  );
};

export default Layout