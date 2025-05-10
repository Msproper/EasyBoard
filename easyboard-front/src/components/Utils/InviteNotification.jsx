import { useEffect, useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { X, Check, Ban, ShieldAlert } from 'lucide-react';
import { useDispatch } from 'react-redux';
import { deleteIncomingInvite } from '@/api/invites/inviteSlice';
import { inviteStatusTypes } from '@/const/inviteStatusTypes';
import { sendMessage } from '@/api/socket/boardSocket';
import { useSelector } from 'react-redux';
import { useSendInviteResponseMutation } from '@/api/invites/inviteApi';
import { notificationTypesClasses } from '@/const/notificationTypesClasses';


const InviteNotification = ({
  id,
  username,
  boardName,
  callback,
  notification,
  onClose,
}) => {

  const [progress, setProgress] = useState(100);
  const [isVisible, setIsVisible] = useState(true);

  useEffect(() => {
    const timer = setInterval(() => {
      setProgress((prev) => {
        if (prev <= 0) {
          clearInterval(timer);
          handleClose();
          return 0;
        }
        return prev - 100 / 30;
      });
    }, 1000);

    return () => clearInterval(timer);
  }, []);

  const handleAction = (type) => {
    callback(notification, type)
    handleClose();
  };

  const handleClose = () => {
    setIsVisible(false);
    setTimeout(() => onClose(id), 300); // Даем время для анимации
  };

  return (
    <motion.div
      layout
      initial={{ opacity: 0, y: -20 }}
      animate={{ opacity: 1, y: 0 }}
      exit={{ opacity: 0, x: 100 }}
      transition={{ duration: 0.3 }}
      className="w-80 bg-white rounded-lg shadow-lg overflow-hidden border mb-2"
    >
      <div className="h-1 bg-gray-200">
        <motion.div
          className="h-full bg-blue-500"
          initial={{ width: '100%' }}
          animate={{ width: `${progress}%` }}
          transition={{ duration: 1, ease: 'linear' }}
        />
      </div>

      <div className="p-4">
        <div className="flex justify-between items-start">
          <div className="flex items-center gap-2 mb-3">
            <ShieldAlert className="text-yellow-500" size={20} />
            <h3 className="font-medium">Запрос доступа</h3>
          </div>
          <button 
            onClick={handleClose}
            className="text-gray-400 hover:text-gray-600"
          >
            <X size={18} />
          </button>
        </div>

        <p className="text-sm text-gray-600 mb-4">
          Пользователь <span className="font-semibold">{username}</span> 
          {" "}просит доступ к доске{" "}
          <span className="font-semibold">{boardName}</span>
        </p>

        <div className="flex gap-2">
          <button
            onClick={() => handleAction(inviteStatusTypes.ACCEPTED)}
            className="flex-1 flex items-center justify-center gap-1 py-2 px-3 rounded bg-green-600 text-white text-sm font-medium hover:bg-green-700 transition"
          >
            <Check size={16} /> Принять
          </button>
          <button
            onClick={() => handleAction(inviteStatusTypes.DECLINED)}
            className="flex-1 flex items-center justify-center gap-1 py-2 px-3 rounded border border-red-500 text-red-500 bg-white text-sm font-medium hover:bg-red-50 transition"
          >
            <X size={16} /> Отклонить
          </button>
          <button
            onClick={() => handleAction(inviteStatusTypes.BANNED)}
            className="flex-1 flex items-center justify-center gap-1 py-2 px-3 rounded border border-black text-black bg-white text-sm font-medium hover:bg-gray-100 transition"
          >
            <Ban size={16} /> Забанить
          </button>
        </div>
      </div>
    </motion.div>
  );
};

export const NotificationStack = ({
}) => {
  const [sendInviteResponse] = useSendInviteResponseMutation()
  const dispatch = useDispatch()
  const incomingInvites = useSelector((state)=>state.invites.incomingInvites)

  const returnResponse = (invite, status)=>{
    const updatedInvite = {
      ...invite, status:status
    }
    console.log(updatedInvite)
    sendInviteResponse(updatedInvite)
  }

  const handleClose = (id) => {
    dispatch(deleteIncomingInvite(id))
  };

  return (
    <div className="fixed bottom-4 right-4 z-50 flex flex-col-reverse">
      <AnimatePresence>
        {Object.values(incomingInvites).map((notification) => (
          <InviteNotification
            key={notification.id}
            id={notification.id}
            username={notification.sender}
            boardName={notification.boardTitle}
            notification={notification}
            callback={returnResponse}
            onClose={() => handleClose(notification.id)}
          />
        ))}
      </AnimatePresence>
    </div>
  );
};