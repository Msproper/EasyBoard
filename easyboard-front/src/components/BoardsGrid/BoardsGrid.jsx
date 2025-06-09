import { useCreateInviteMutation } from "@/api/invites/inviteApi";
import { Card, CardContent } from "@/components/ui/card";
import { useNavigate } from "react-router-dom";
import { notificationTypesClasses } from "@/const/notificationTypesClasses";
import { useDispatch } from "react-redux";
import { showNotification } from "@/api/notification/notificationSlice";
import { useGetPhotoQuery } from "@/api/board/boardApi";
import { useState, useEffect } from "react";
import { ImageIcon, UsersIcon } from "lucide-react";
import { getAccessStatus } from "../Utils/getAccessStatus";
import BoardDetailsModal from "../BoardDetailsModal";

const BoardCard = ({ board, onClick }) => {
  const { data: imageBlob, isLoading } = useGetPhotoQuery(board.imageUrl, {
    skip: !board.imageUrl,
  });

  const access = getAccessStatus(board);

  const [imageUrl, setImageUrl] = useState(null);

  useEffect(() => {
    if (imageBlob) {
      const url = URL.createObjectURL(imageBlob);
      setImageUrl(url);
      return () => URL.revokeObjectURL(url);
    }
  }, [imageBlob]);

  return (
    <Card
      onClick={() => onClick(board.id)}
      className="group cursor-pointer rounded-2xl overflow-hidden bg-white shadow-sm hover:shadow-lg transition-shadow duration-300"
    >
      <div className="relative aspect-video w-full overflow-hidden bg-gray-100">
        {isLoading ? (
          <div className="absolute inset-0 animate-pulse bg-gray-200" />
        ) : imageUrl ? (
          <img
            src={imageUrl}
            alt={board.title}
            className="absolute inset-0 w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
          />
        ) : (
          <div className="absolute inset-0 flex items-center justify-center">
            <ImageIcon className="w-10 h-10 text-gray-400" />
          </div>
        )}
      </div>

      <CardContent className="p-4 space-y-2">
        <div className="flex items-center justify-between">
          <h3 className="text-lg font-semibold truncate">{board.title}</h3>
        </div>
        <div className={`flex items-center gap-1 text-xs ${access.color}`}>
            {access.icon}
            <span>{access.text}</span>
        </div>
        {board.private ? (
          <div className="flex items-center gap-1 text-xs text-gray-400">
            <LockIcon className="w-4 h-4" />
            <span>Private</span>
          </div>
        ) : (
          <div className="flex items-center gap-1 text-xs text-gray-400">
            <UsersIcon className="w-4 h-4" />
            <span>Shared</span>
          </div>
        )}
      </CardContent>
    </Card>
  );
};
const BoardsGrid = ({ boards }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [createInvite] = useCreateInviteMutation();

  const [selectedBoard, setSelectedBoard] = useState(null);

  const handleClick = (id) => {
    const board = boards.find((b) => b.id === id);
    setSelectedBoard(board);
  };

  const handleModalClose = () => setSelectedBoard(null);

  const handleJoinBoard = async (id) => {
    try {
      const data = await createInvite(id).unwrap();
      if (data.status === "ACCEPTED") {
        dispatch(showNotification({ type: notificationTypesClasses.SUCCESS, message: "Успешно" }));
        navigate("/boards/" + data.uuid, { replace: true });
      } else if (data.status === "PENDING") {
        dispatch(showNotification({ type: notificationTypesClasses.SUCCESS, message: "Приглашение отправлено, ожидайте" }));
      }
    } catch (e) {
      console.error(e);
      dispatch(showNotification({ type: notificationTypesClasses.ERROR, message: e.data?.message || "Ошибка подключения" }));
    }
    handleModalClose()
  };

  return (
    <>
        {boards.map((board) => (
          <BoardCard key={board.id} board={board} onClick={handleClick} />
        ))}

      <BoardDetailsModal board={selectedBoard} open={!!selectedBoard} onClose={handleModalClose} onJoinBoard={handleJoinBoard}/>
    </>
  );
};

export default BoardsGrid;
