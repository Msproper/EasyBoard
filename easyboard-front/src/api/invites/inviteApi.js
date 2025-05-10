import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithReauth } from '../baseQuery';


export const inviteApi = createApi({
  reducerPath: 'inviteApi',
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({
    createInvite: builder.mutation({
      query: (boardId) => ({
        url: "/api/invites/"+boardId+"/sendRequest",
        method: 'POST',
      }),
    }),
    sendInviteResponse: builder.mutation({
      query: (updatedInvite) => ({
        url: "/api/invites/"+updatedInvite.boardId+"/sendResponse",
        method: 'POST',
        body: updatedInvite
      }),
    }),
  }),
});

export const {
    useCreateInviteMutation,
    useSendInviteResponseMutation,
} = inviteApi;
