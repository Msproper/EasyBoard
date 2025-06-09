import { useSelector, useDispatch } from 'react-redux';
import { AnimatePresence, motion } from 'framer-motion';
import { removeNotification } from '@/api/notification/notificationSlice';
import { X } from 'lucide-react';
import { useEffect } from 'react';

export const Notification = () => {
  const notifications = useSelector((state) => state.notifications);
  const dispatch = useDispatch();
  
  useEffect(() => {
    const timers = notifications.map((n) =>
      setTimeout(() => dispatch(removeNotification(n.id)), 4000)
    );
    return () => timers.forEach(clearTimeout);
  }, [notifications, dispatch]);

  if (notifications == null) return <></>
  return (
    <div className="fixed top-4 left-1/2 -translate-x-1/2 z-50 flex flex-col gap-2">
      <AnimatePresence>
        {notifications.map((n) => (
          <motion.div
            key={n.id}
            initial={{ opacity: 0, y: -50 }}
            animate={{ opacity: 1, y: 0 }}
            exit={{ opacity: 0, y: -50 }}
            transition={{ type: 'spring', damping: 25 }}
            className={`rounded-lg ${n.type} max-w-md w-full shadow-lg`}
            role="alert"
          >
            <div className="flex items-center justify-between p-4">
              <p className="text-white font-medium">{n.message}</p>
              <button
                onClick={() => dispatch(removeNotification(n.id))}
                className="text-white hover:text-gray-300 transition-colors"
                aria-label="Close"
              >
                <X />
              </button>
            </div>
          </motion.div>
        ))}
      </AnimatePresence>
    </div>
  );
};

