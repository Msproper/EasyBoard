import React, { useEffect } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X } from 'lucide-react';


export const Notification = ({
  message,
  type,
  onClose,
  duration = 10000,
}) => {
  useEffect(() => {
    const timer = setTimeout(handleClose, duration);
    return () => clearTimeout(timer);
  }, [onClose, duration]);

  const handleClose = () => {
    setTimeout(() => onClose(), 300); // Даем время для анимации
  };


  return (
    <AnimatePresence>
      <motion.div
      layout
        initial={{ opacity: 0, y: -50 }}
        animate={{ opacity: 1, y: 0 }}
        exit={{ opacity: 0, y: -50 }}
        transition={{ type: 'spring', damping: 25 , duration:300}}
        className={`fixed top-4 left-[35%] z-50 rounded-lg ${type} max-w-md w-full`}
        role="alert"
      >
        <div className="flex items-center justify-between p-4">
          <div className="flex items-center">
            <p className="text-white font-medium">{message}</p>
          </div>
          <button
            onClick={handleClose}
            className="text-white hover:text-gray-600 transition-colors"
            aria-label="Close"
          >
            <X ></X>
          </button>
        </div>
      </motion.div>
    </AnimatePresence>
  );
};