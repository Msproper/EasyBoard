import { createApi } from '@reduxjs/toolkit/query/react';
import { baseQueryWithReauth } from '../baseQuery';


export const roomApi = createApi({
  reducerPath: 'inviteApi',
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({
    getPermissionLevel: builder.query()
  }),
});

export const {

} = roomApi;
