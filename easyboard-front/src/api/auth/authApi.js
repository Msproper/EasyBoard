import { createApi} from '@reduxjs/toolkit/query/react';
import { setUser, logout } from './authSlice';
import { baseQueryWithReauth } from '../baseQuery';



export const authApi = createApi({
  reducerPath: 'authApi',
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({
    signUp: builder.mutation({
      query: (userData) => ({
        url: '/api/auth/sign-up',
        method: 'POST',
        body: userData,
      }),
    }),
    signIn: builder.mutation({
      query: (credentials) => ({
        url: '/api/auth/sign-in',
        method: 'POST',
        body: credentials,
      }),
    }),
    updateUser: builder.query({
      query: () => ({
        url: '/api/auth/update',
        method: 'GET',
      }),
      async onQueryStarted(args, { dispatch, queryFulfilled }) {
        try {
          const { data } = await queryFulfilled;
          dispatch(setUser(data));
        } catch (error) {
          console.log("Ошибка при обновлении данных", error)
        }
      },
    }),
    logoutUser: builder.mutation({
      query:()=>({
        url: '/api/auth/logout',
        method: 'POST',
      }), 
      async onQueryStarted(args, { dispatch, queryFulfilled }) {
        try {
          await queryFulfilled;
          dispatch(logout());
        } catch (error) {
          console.log("ошибка при выходе ", error)
        }
      },
    })
  }),
});

export const {
  useSignUpMutation,
  useSignInMutation,
  useUpdateUserQuery,
  useLogoutUserMutation,
} = authApi;
