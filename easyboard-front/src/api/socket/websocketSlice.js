import { createSlice } from '@reduxjs/toolkit';

const websocketSlice = createSlice({
  name: 'websocket',
  initialState: {
    connected: false,
    subscriptions: {},
  },
  reducers: {
    setConnected: (state, action) => {
      state.connected = action.payload;
    },
    addSubscription: (state, action) => {
      const { channel, callback } = action.payload;
      state.subscriptions[channel] = callback;
    },
    removeSubscription: (state, action) => {
      delete state.subscriptions[action.payload];
    },
    clearSubscriptions: (state) => {
      state.subscriptions = {};
    },
  }
});

export const { setConnected, addSubscription, removeSubscription, clearSubscriptions } = websocketSlice.actions;
export default websocketSlice.reducer;
