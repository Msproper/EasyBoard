import { createSlice } from '@reduxjs/toolkit';

const websocketSlice = createSlice({
  name: 'websocket',
  initialState: {
    connected: false,
    subscriptions: {},
  },
  reducers: {
    setConnected: (state) => {
      state.connected = true;
    },
    setDisconnected: (state) => {
      state.connected = false
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

export const { setConnected, addSubscription, removeSubscription, clearSubscriptions, setDisconnected } = websocketSlice.actions;
export default websocketSlice.reducer;
