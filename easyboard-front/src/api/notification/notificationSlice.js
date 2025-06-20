

import { v4 as uuidv4 } from 'uuid';
import { createSlice} from '@reduxjs/toolkit';
const initialState = [];
const notificationSlice = createSlice({
  name: 'notifications',
  initialState,
  reducers: {
    showNotification: (state, action) => {
      state.push({ ...action.payload, id: uuidv4() });
    },
    removeNotification: (state, action) => {
      return state.filter(n => n.id !== action.payload);
    },
  },
});

export const { showNotification, removeNotification } = notificationSlice.actions;
export default notificationSlice.reducer;
