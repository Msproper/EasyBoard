import { motion } from "framer-motion";
import { Menu, X } from "lucide-react";

export const MenuButton = ({ isOpen, onClick }) => {
    const variants = {
      open: { rotate: 180 },
      closed: { rotate: 0 },
    };
  
    return (
      <motion.button
        onClick={onClick}
        className="p-2 rounded-full bg-white shadow-lg z-0"
        whileHover={{ scale: 1.05 }}
        whileTap={{ scale: 0.95 }}
      >
        <motion.div
          animate={isOpen ? "open" : "closed"}
          variants={variants}
          transition={{ duration: 0.3 }}
        >
          {isOpen ? (
            <X size={24} className="text-gray-800" />
          ) : (
            <Menu size={24} className="text-gray-800" />
          )}
        </motion.div>
      </motion.button>
    );
  };