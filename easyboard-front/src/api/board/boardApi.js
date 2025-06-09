import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithReauth } from '../baseQuery';



export const boardApi = createApi({
  reducerPath: 'boardApi',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Boards'], 
  endpoints: (builder) => ({
    createBoard: builder.mutation({
      query: (boardData) => ({
        url: '/api/boards/create',
        method: 'POST',
        body: boardData,
      }),
      invalidatesTags:['Boards']
    }),
    updateBoard: builder.mutation({
      query: (credentials) => ({
        url: '/api/boards/update',
        method: 'PATCH',
        body: credentials,
      }),

    }),
    saveSnapshot: builder.mutation({
      query: (data)=>({
        url: `/api/boards/save/${data.id}`,
        method: "POST",
        body: data.body,
      }),
    }),
    getSnapshot:builder.query({
      query: (id) => ({
        url: `/api/boards/snapshot/${id}`,
        method: 'GET',
      }),
    }),
    getBoards: builder.query({
      query: () => ({
        url: '/api/boards/',
        method: 'GET',
      }),
      providesTags: ['Boards']
    }),
    getPhoto: builder.query({
      query: (filePath) => ({
        url: `/api/files/photo`,
        params: { filePath },
        responseHandler: async (response) => {
          if (!response.ok) {
            const error = await response.json();
            throw error; // Пробрасываем ошибку как обычный объект
          }
          return response.blob();
        },
        validateStatus: (response) => {
          // Обрабатываем только успешные запросы как blob
          return response.status === 200;
        },
      }),
      transformErrorResponse: (response) => {
        // Преобразуем ошибку в сериализуемый формат
        return {
          status: response.status,
          data: response.data,
        };
      },
    }),
    searchBoards:builder.query({
      query: (params) => ({
        url: '/api/boards/search',
        params: { 
          query: params.query,
          limit: params.limit || 10,
          page: params.page || 0,
          sort: params.sort || 'TITLE_ASC',
        },
        method:'GET',
      }),
      providesTags: ['Boards']
    })
  }),
});

export const {
  useSaveSnapshotMutation,
  useCreateBoardMutation,
  useUpdateBoardMutation,
  useGetBoardsQuery,
  useSearchBoardsQuery,
  useGetPhotoQuery,
  useGetSnapshotQuery
} = boardApi;
