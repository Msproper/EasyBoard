// BoardSearch.tsx
import { useState } from 'react';
import { useSearchBoardsQuery } from '@/api/board/boardApi';
import BoardsGrid from '../BoardsGrid/BoardsGrid';
import { useDebounce } from 'use-debounce';
import { Search, ChevronLeft, ChevronRight, Loader2 } from 'lucide-react';
import { Spinner } from '../Utils/Spinner';

export const Searcher = () => {
  const [query, setQuery] = useState('');
  const [page, setPage] = useState(0);
  const [sort, setSort] = useState('TITLE_ASC');
  const [debouncedQuery] = useDebounce(query, 300)

  // Запрос данных
  const {
    data: boards,
    isLoading,
    isFetching,
    error,
  } = useSearchBoardsQuery({ query:debouncedQuery, page, sort });

  const handleNextPage = () => setPage((prev) => prev + 1);
  const handlePrevPage = () => setPage((prev) => Math.max(prev - 1, 0));

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      {/* Поисковая строка и фильтры */}
      <div className="flex flex-col sm:flex-row gap-4 mb-8">
        <div className="relative flex-1">
          <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
            <Search className="h-5 w-5 text-gray-400" />
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
          <option value="CREATEDAT_ASC">По дате (сначала старые)</option>
          <option value="CREATEDAT_DESC">По дате (сначала новые)</option>
        </select>
      </div>

      {/* Состояние загрузки/ошибки */}
      {isLoading ? (
        <Spinner className={"flex justify-center items-center h-64"}></Spinner>
      ) : error ? (
        <div className="bg-red-50 border-l-4 border-red-500 p-4 mb-6">
          <div className="flex">
            <div className="flex-shrink-0">
              <svg className="h-5 w-5 text-red-500" viewBox="0 0 20 20" fill="currentColor">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
              </svg>
            </div>
            <div className="ml-3">
              <p className="text-sm text-red-700">
                Ошибка загрузки: {(error)?.data?.message || 'Неизвестная ошибка'}
              </p>
            </div>
          </div>
        </div>
      ) : (
        <>
          {/* Сетка досок */}
          <div className="p-6 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            <BoardsGrid boards={boards} />
          </div>

          {/* Пагинация */}
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