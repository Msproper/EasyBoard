import { motion } from 'framer-motion';

export const Spinner = (className) => {
  return (
    <motion.div
      style={{
        width: 50,
        height: 50,
        borderRadius: '50%',
        border: '5px solid #eee',
        borderTop: '5px solid #3498db',
      }}
      className={className}
      animate={{ rotate: 360 }}
      transition={{ duration: 1, repeat: Infinity, ease: 'linear' }}
    />
  );
};