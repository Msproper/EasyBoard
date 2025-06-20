import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithReauth } from '../baseQuery';
import { boardDest } from '@/const/destinations';



export const boardApi = createApi({
  reducerPath: 'boardApi',
  baseQuery: baseQueryWithReauth,
  tagTypes: ['Boards'], 
  endpoints: (builder) => ({
    createBoard: builder.mutation({
      query: (boardData) => ({
        url: boardDest,
        method: 'POST',
        body: boardData,
      }),
      invalidatesTags:['Boards']
    }),
    updateBoard: builder.mutation({
      query: (credentials) => ({
        url: `${boardDest}/${credentials.id}`,
        method: 'PATCH',
        body: credentials.body,
      }),

    }),
    saveSnapshot: builder.mutation({
      query: (data)=>({
        url: `${boardDest}/${data.id}/snapshot`,
        method: "POST",
        body: data.body,
      }),
    }),
    getSnapshot:builder.query({
      query: (id) => ({
        url: `${boardDest}/${data.id}/snapshot`,
        method: 'GET',
      }),
    }),
    getBoards: builder.query({
      query: (params) => ({
        url: `${boardDest}`,
        params: { 
          query: params.query,
          limit: params.limit || 10,
          page: params.page || 0,
          sort: params.sort || 'TITLE_ASC',
        },
        method:'GET',
      }),
      providesTags: ['Boards']
    }),
    getPhoto: builder.query({
      query: (filePath) => ({
        url: `/files/photo`,
        params: { filePath },
        responseHandler: async (response) => {
          if (!response.ok) {
            const error = await response.json();
            throw error; 
          }
          return response.blob();
        },
        validateStatus: (response) => {
          return response.status === 200;
        },
      }),
      transformErrorResponse: (response) => {
        return {
          status: response.status,
          data: response.data,
        };
      },
    }),
    likeBoard: builder.mutation({
      query: (boardId) => ({
        url: `${boardDest}/likes/${boardId}`,
        method: 'POST',
      }),
      invalidatesTags: ['Boards'],
    }),
    unlikeBoard: builder.mutation({
      query: (boardId) => ({
        url: `${boardDest}/likes/${boardId}`,
        method: 'DELETE',
      }),
      invalidatesTags: ['Boards'],
    }),
    searchBoards:builder.query({
      query: (params) => ({
        url: `${boardDest}/search`,
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
  useGetSnapshotQuery,
  useLikeBoardMutation,
  useUnlikeBoardMutation,
} = boardApi;
