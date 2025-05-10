import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithReauth } from '../baseQuery';


export const boardApi = createApi({
  reducerPath: 'boardApi',
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({
    createBoard: builder.mutation({
      query: (boardData) => ({
        url: '/api/boards/create',
        method: 'POST',
        body: boardData,
      }),
    }),
    updateBoard: builder.mutation({
      query: (credentials) => ({
        url: '/api/boards/update',
        method: 'PATCH',
        body: credentials,
      }),
    }),
    getBoards: builder.query({
      query: () => ({
        url: '/api/boards/',
        method: 'GET',
      }),
    }),
    searchBoards:builder.query({
      query: (params) => ({
        url: '/api/boards/search',
        params: { // Все параметры автоматически добавятся в URL
          query: params.query,
          limit: params.limit || 10,
          page: params.page || 0,
          sort: params.sort || 'TITLE_ASC',
        },
        method:'GET',
      }),
    })
  }),
});

export const {
  useCreateBoardMutation,
  useUpdateBoardMutation,
  useGetBoardsQuery,
  useSearchBoardsQuery,
} = boardApi;
