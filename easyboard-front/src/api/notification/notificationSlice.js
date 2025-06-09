// features/notification/notificationSlice.ts
import { createSlice} from '@reduxjs/toolkit';
const initialState = [];
const notificationSlice = createSlice({
  name: 'notifications',
  initialState,
  reducers: {
    showNotification: (state, action) => {
      state.push({ ...action.payload, id: crypto.randomUUID() });
    },
    removeNotification: (state, action) => {
      return state.filter(n => n.id !== action.payload);
    },
  },
});

export const { showNotification, removeNotification } = notificationSlice.actions;
export default notificationSlice.reducer;
