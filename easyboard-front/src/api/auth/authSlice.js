// src/features/auth/authSlice.js
import { createSlice } from '@reduxjs/toolkit';
const initialState = {
  token: null,
  user:null
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    setCredentials: (state, action) => {
      const token = action.payload;
      state.token = token;      
      localStorage.setItem('token', JSON.stringify(token)); 
    },
    setUser:(state, action) => {
      const user = action.payload;
      state.user = user;      
      localStorage.setItem('user', JSON.stringify(user)); 
    },
    logout: (state) => {
      state.token = null;
      state.user = null;
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },
  },
});

export const { setCredentials, logout, setUser } = authSlice.actions;

export default authSlice.reducer;

const storedAuth = localStorage.getItem('token');
if (storedAuth) {
  initialState.token = JSON.parse(storedAuth);
}

const storedUser = localStorage.getItem('user');
if (storedAuth) {
  initialState.user = JSON.parse(storedUser);
}