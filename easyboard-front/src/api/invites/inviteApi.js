import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithReauth } from '../baseQuery';
import { invitesDest } from '@/const/destinations';
import { fastInviteDest } from '@/const/destinations';


export const inviteApi = createApi({
  reducerPath: 'inviteApi',
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({
    createInvite: builder.mutation({
      query: (boardId) => ({
        url: `${invitesDest}/`+boardId+"/request",
        method: 'POST',
      }),
    }),
    sendInviteResponse: builder.mutation({
      query: (updatedInvite) => ({
        url: `${invitesDest}/response`,
        method: 'POST',
        body: updatedInvite
      }),
    }),
    getAccessInvite: builder.query({
      query: (boardId)=>({
        url: `${fastInviteDest}/${boardId}`,
        method: 'GET',
      })
    }),
    sendAccessInviteCode: builder.mutation({
      query: (code) => ({
        url: `${fastInviteDest}/code/${code}`,
        method: 'POST',
      }),
    }),    
    sendAccessInviteUuid: builder.mutation({
      query: (code) => ({
        url: `${fastInviteDest}/uuid/${code}`,
        method: 'POST',
      }),
    }),
  }),
});

export const {
    useCreateInviteMutation,
    useSendInviteResponseMutation,
    useGetAccessInviteQuery,
    useSendAccessInviteCodeMutation,
    useSendAccessInviteUuidMutation,
} = inviteApi;
