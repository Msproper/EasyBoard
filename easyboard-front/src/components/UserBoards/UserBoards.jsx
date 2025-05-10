
import { Plus } from "lucide-react";
import CreateBoardDialog from "../dialogs/CreateBoardDialog";
import { useGetBoardsQuery } from "@/api/board/boardApi";
import {Spinner} from "@/components/Utils/Spinner";
import BoardsGrid from "../BoardsGrid/BoardsGrid";
import { useNavigate } from "react-router-dom";


const UserBoards = () => {
  
  const {data:boards, isLoading, error} = useGetBoardsQuery()
  const navigate = useNavigate()
  if (isLoading) return (<Spinner className="absolute top-[50%]"/>);
  return (
    <div className="p-6 ml-5 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <CreateBoardDialog
        triggerLabel={
          <div className="flex flex-col items-center justify-center cursor-pointer hover:shadow-lg transition-shadow p-4">
            <Plus size={48} className="text-gray-400" />
            <p className="mt-2 text-lg font-medium text-gray-600">Новая доска</p>
          </div>
        }
      />


      <BoardsGrid boards={boards}></BoardsGrid>
    </div>
    );
  };
export default UserBoards