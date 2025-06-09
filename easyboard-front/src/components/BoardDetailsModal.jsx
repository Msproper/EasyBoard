import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription } from "@/components/ui/dialog";
import { useGetPhotoQuery } from "@/api/board/boardApi";
import { useState, useEffect } from "react";
import { getAccessStatus } from "./Utils/getAccessStatus";
import { motion, AnimatePresence } from "framer-motion";
import defaultImage from '../assets/template_photo.jpg'
import { CalendarDays } from "lucide-react";

const modalVariants = {
hidden: { opacity: 0, scale: 0.95, y: 20 },
visible: { opacity: 1, scale: 1, y: 0, transition: { duration: 0.25 } },
exit: { opacity: 0, scale: 0.95, y: 20, transition: { duration: 0.2 } },
};

export const BoardDetailsModal = ({ board, open, onClose, onGoToUser, onJoinBoard }) => {
    const { data: imageBlob } = useGetPhotoQuery(board?.imageUrl, {
      skip: !open || !board?.imageUrl,
    });
  
    const [imageUrl, setImageUrl] = useState(null);
  
    useEffect(() => {
      if (imageBlob) {
        const url = URL.createObjectURL(imageBlob);
        setImageUrl(url);
        return () => URL.revokeObjectURL(url);
      }
    }, [imageBlob]);
  
    if (!board) return null;
  
    const access = getAccessStatus(board);
    const imageToUse = imageUrl || defaultImage;
  
    return (
      <AnimatePresence>
        {open && (
          <Dialog open={open} onOpenChange={onClose}>
            <DialogContent className="sm:max-w-[70%] md:max-w-[55%] lg:max-w-5xl border-none p-0 bg-transparent shadow-none">
              <motion.div
                key="board-modal"
                variants={modalVariants}
                initial="hidden"
                animate="visible"
                exit="exit"
                className="w-full bg-white dark:bg-zinc-900 p-6 md:p-8 rounded-2xl shadow-xl space-y-6"
              >
                <DialogTitle className="text-2xl md:text-3xl font-bold text-zinc-900 dark:text-white ">
                    {board.title}
                </DialogTitle>
                <div className="w-full h-64 md:h-72 rounded-xl overflow-hidden border aspect-[1/1]">
                  <img
                    src={imageToUse}
                    className="w-full h-full object-cover"
                  />
                </div>
  
                <div className={`flex items-center gap-1 text-xs ${access.color}`}>
                    {access.icon}
                    <span>{access.text}</span>
                </div>
  
                {board.description?.trim() && (
                  <div className="bg-zinc-100 dark:bg-zinc-800 p-4 rounded-lg text-sm md:text-base leading-relaxed text-zinc-700 dark:text-zinc-200">
                    {board.description}
                  </div>
                )}
  
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm text-zinc-600 dark:text-zinc-300">
                  <div className="flex items-center gap-2">
                    <CalendarDays className="w-5 h-5" />
                    <span><strong>Создана:</strong> {board.createAt}</span>
                  </div>
                  <div className="flex items-center gap-2">
                    <CalendarDays className="w-5 h-5" />
                    <span><strong>Обновлена:</strong> {board.updateAt}</span>
                  </div>
                </div>
  
                <div className="text-s md:text-base">
                  <span className="font-semibold">Создатель: </span>
                  <span
                    onClick={() => onGoToUser?.(board.owner)}
                    className="text-blue-600 border rounded-2xl p-2 dark:text-blue-400 cursor-pointer hover:text-blue-800 dark:hover:text-blue-300 transition"
                  >
                    {board.owner}
                  </span>
                </div>
  
                {/* Кнопки */}
                <div className="flex justify-end gap-3 mt-6">
                  <button
                    onClick={onClose}
                    className="px-4 py-2 rounded-lg bg-zinc-200 dark:bg-zinc-700 text-zinc-800 dark:text-white hover:bg-zinc-300 dark:hover:bg-zinc-600 transition"
                  >
                    Закрыть
                  </button>
                  
                  <button
                    onClick={() => onJoinBoard?.(board.id)}
                    className={`px-4 py-2 rounded-lg ${board.isAccess ? 'bg-green-500' : 'bg-blue-500'} text-white hover:bg-blue-700 transition`}
                  >{board.isAccess ? "Войти" : "Запросить доступ"}
                  </button>
                </div>
              </motion.div>
            </DialogContent>
          </Dialog>
        )}
      </AnimatePresence>
    );
  };

export default BoardDetailsModal
