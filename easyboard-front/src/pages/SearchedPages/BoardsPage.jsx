// BoardSearch.tsx
import { useState, useEffect } from 'react';
import { useSearchBoardsQuery } from '@/api/board/boardApi';
import BoardsGrid from '@/components/BoardsGrid/BoardsGrid';
import { useDebounce } from 'use-debounce';
import { Search, ChevronLeft, ChevronRight, Loader2 } from 'lucide-react';
import { Spinner } from '@/components/Utils/Spinner';
import { useGetBoardsQuery } from '@/api/board/boardApi';
import CreateBoardDialog from '@/components/Dialogs/CreateBoardDialog';
import { useDispatch } from 'react-redux';
import { Key, Link2 } from 'lucide-react';
import { setBoard } from '@/api/board/boardSlice';
import { showNotification } from '@/api/notification/notificationSlice';
import { notificationTypesClasses } from '@/const/notificationTypesClasses';
import { useSendAccessInviteCodeMutation } from '@/api/invites/inviteApi';
import { useNavigate } from 'react-router-dom';
export const BoardsPage = ({ mode }) => {
    const [query, setQuery] = useState('');
    const [page, setPage] = useState(0);
    const [sort, setSort] = useState('TITLE_ASC');
    const [debouncedQuery] = useDebounce(query, 300);
    const dispatch = useDispatch()
    const [inviteCode, setInviteCode] = useState('');
    const params = { query: debouncedQuery, page, sort };
    const [sendAccessInviteCode] = useSendAccessInviteCodeMutation()
    const navigate = useNavigate();
  
    const {
      data: boards,
      isLoading,
      isFetching,
      error,
    } = mode === 'user'
      ? useGetBoardsQuery(params)
      : useSearchBoardsQuery(params);
  
    const handleNextPage = () => setPage((prev) => prev + 1);
    const handlePrevPage = () => setPage((prev) => Math.max(prev - 1, 0));

    useEffect(() => {      
      setQuery("")
    }, [dispatch, mode]); 

    const handleFetchInvite = async () => {
      try {
        const data = await sendAccessInviteCode(inviteCode.trim()).unwrap();
        dispatch(setBoard({ boardId: null, boardUuid: data.uuid }));
        navigate("/board", { replace: true });
      } catch (e) {
        console.log(e)
        dispatch(showNotification({type:notificationTypesClasses.ERROR, message:"Неверный код"}))
      }
    };
  
    return (
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Строка поиска и сортировки */}
        <div className="flex flex-col sm:flex-row gap-4 mb-8">
          <div className="relative flex-1">
            <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none ">
              <Search className="h-5 w-5 text-gray-700" />
            </div>
            <input
              type="text"
              placeholder="Поиск досок..."
              value={query}
              onChange={(e) => {
                setQuery(e.target.value);
                setPage(0);
              }}
              className="block w-full pl-10 pr-3 py-2 border border-gray-300 rounded-lg bg-white shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-gray-700"
            />
          </div>
          <select
            value={sort}
            onChange={(e) => setSort(e.target.value)}
            className="block w-full sm:w-64 px-3 py-2 border border-gray-300 rounded-lg bg-white shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-gray-700"
          >
            <option value="TITLE_ASC">По названию (А-Я)</option>
            <option value="TITLE_DESC">По названию (Я-А)</option>
            <option value="CREATEDAT_ASC">По дате (старые)</option>
            <option value="CREATEDAT_DESC">По дате (новые)</option>
          </select>
        </div>
        {mode === 'explore' && (
          <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-200 max-m-sm">
            <div className="flex flex-col sm:flex-row gap-3 items-center  max-m-sm">
              <div className="relative flex-1 w-full">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Key className="h-4 w-4 text-gray-400" />
                </div>
                <input
                  type="text"
                  placeholder="Введите код доски"
                  value={inviteCode}
                  onChange={(e) => setInviteCode(e.target.value)}
                  className="block w-full pl-9 pr-3 py-2 text-sm border border-gray-300 rounded-lg bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 text-gray-800 font-mono"
                />
              </div>
              <button
                onClick={handleFetchInvite}
                disabled={!inviteCode.trim()}
                className={`w-full sm:w-auto px-4 py-2 rounded-lg transition-colors flex items-center justify-center gap-2 ${
                  !inviteCode.trim()
                    ? 'bg-gray-200 text-gray-500 cursor-not-allowed'
                    : 'bg-blue-500 hover:bg-blue-600 text-white'
                }`}
              >
                <Link2 className="w-4 h-4" />
                <span>Подключиться</span>
              </button>
            </div>
            <p className="text-xs text-gray-500 mt-1 text-center">
              Получите код доски у владельца для подключения
            </p>
          </div>
        )}
        {isLoading ? (
          <Spinner className="flex justify-center items-center h-64" />
        ) : error ? (
          <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-6">Ошибка</div>
        ) : (
          <>
            <div className="p-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {mode === 'user' && <CreateBoardDialog />}
              <BoardsGrid boards={boards} />
            </div>
  
            <div className="flex justify-center mt-8">
            <button
              onClick={handlePrevPage}
              disabled={page === 0 || isFetching}
              className={`inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md ${
                page === 0 || isFetching
                  ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
              }`}
            >
              <ChevronLeft className="h-5 w-5" />
              Назад
            </button>
            <span className="mx-4 flex items-center text-gray-700">
              Страница {page + 1}
            </span>
            <button
              onClick={handleNextPage}
              disabled={boards?.length < 10 || isFetching}
              className={`inline-flex items-center px-4 py-2 border border-gray-300 text-sm font-medium rounded-md ${
                boards?.length < 10 || isFetching
                  ? 'bg-gray-100 text-gray-400 cursor-not-allowed'
                  : 'bg-white text-gray-700 hover:bg-gray-50'
              }`}
            >
              Вперед
              <ChevronRight className="h-5 w-5" />
            </button>
          </div>
          </>
        )}
      </div>
    );
  };
  