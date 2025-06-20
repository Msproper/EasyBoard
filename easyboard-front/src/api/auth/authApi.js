import { createApi} from '@reduxjs/toolkit/query/react';
import { setUser, logout } from './authSlice';
import { baseQueryWithReauth } from '../baseQuery';
import { authDest } from '@/const/destinations';



export const authApi = createApi({
  reducerPath: 'authApi',
  baseQuery: baseQueryWithReauth,
  endpoints: (builder) => ({
    signUp: builder.mutation({
      query: (userData) => ({
        url: `${authDest}/sign-up`,
        method: 'POST',
        body: userData,
      }),
    }),
    signIn: builder.mutation({
      query: (credentials) => ({
        url: `${authDest}/sign-in`,
        method: 'POST',
        body: credentials,
      }),
    }),
    anonymousSignUp: builder.mutation({
      query: () => ({
        url: `${authDest}/anonymous`,
        method: 'POST',
      }),
    }),
    updateUser: builder.query({
      query: () => ({
        url: `${authDest}/update`,
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
        url: `${authDest}/logout`,
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
  useAnonymousSignUpMutation,
} = authApi;
