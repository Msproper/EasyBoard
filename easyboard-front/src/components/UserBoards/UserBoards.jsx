
import CreateBoardDialog from "../Dialogs/CreateBoardDialog";
import { useGetBoardsQuery } from "@/api/board/boardApi";
import {Spinner} from "@/components/Utils/Spinner";
import BoardsGrid from "../BoardsGrid/BoardsGrid";


const UserBoards = () => {
  
  const {data:boards, isLoading, error} = useGetBoardsQuery()
  if (isLoading) return (<Spinner className="absolute top-[50%]"/>);
  return (
    <div className="p-6 ml-5 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <CreateBoardDialog/>
      <BoardsGrid boards={boards}></BoardsGrid>
    </div>
    );
  };
export default UserBoards