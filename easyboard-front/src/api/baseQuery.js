import { fetchBaseQuery } from "@reduxjs/toolkit/query";
import { logout, setCredentials, setUser } from './auth/authSlice';
import  {authDest}  from "@/const/destinations";


const baseQuery = fetchBaseQuery({
  baseUrl: 'http://localhost:8080/api',
  credentials: 'include',
  prepareHeaders: (headers) => {
      const token = localStorage.getItem('token');
      if (token) {
          headers.set('Authorization', `Bearer ${token}`);
      }
      return headers;
  },
});

export const baseQueryWithReauth = async (args, api, extraOptions) => {
  console.log("sended:", args)
  let result = await baseQuery(args, api, extraOptions);
  
  if (result.error && (result.error.status === 401)) {
    const refreshResult = await baseQuery(
      {
        url: `${authDest}/refresh`,
        method: 'POST',
      },
      api,
      extraOptions
    );
    
    if (refreshResult.error) {
      api.dispatch(logout());
    } else {
      api.dispatch(setCredentials(refreshResult.data.token));
      result = await baseQuery(args, api, extraOptions);
    }
  }
  console.log(args)
  if (result.data?.token && (args.url === `${authDest}/sign-up` || args.url === `${authDest}/sign-in` || args.url === `${authDest}/anonymous`)) {
    api.dispatch(setCredentials(result.data.token));
    const userResult = await baseQuery(
      {
        url: '/auth/update',
        method: 'GET',
      },
      api,
      extraOptions
    );
    api.dispatch(setUser(userResult.data))
  }

  if (result.error) console.log(result.error.data)
  
  return result;
};

  