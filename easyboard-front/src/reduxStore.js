// src/app/store.js
import { configureStore } from '@reduxjs/toolkit';
import { authApi } from './api/auth/authApi';
import { boardApi } from './api/board/boardApi';
import apiReducer from './api/auth/authSlice'
import boardReducer from './api/board/boardSlice'
import invitesReducer from './api/invites/inviteSlice'
import websocketReducer from './api/socket/websocketSlice'
import { inviteApi } from './api/invites/inviteApi';

export const store = configureStore({
  reducer: {
    [boardApi.reducerPath]: boardApi.reducer,
    [authApi.reducerPath]: authApi.reducer,
    [inviteApi.reducerPath]: inviteApi.reducer,
    auth:apiReducer,
    board:boardReducer,
    invites:invitesReducer,
    websocket:websocketReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware().concat(authApi.middleware).concat(boardApi.middleware).concat(inviteApi.middleware), devTools:true,
});
