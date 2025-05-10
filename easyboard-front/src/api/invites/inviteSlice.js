// src/features/auth/authSlice.js
import { createSlice } from '@reduxjs/toolkit';
const initialState = {
  status: null,
  uuid: null,
  timeStamp: null,
  incomingInvites:{}
};

const inviteSlice = createSlice({
  name: 'invites',
  initialState,
  reducers: {
    setInvite: (state, action) => {
      state.status = action.payload.status;
      state.uuid = action.payload.uuid;
      state.timeStamp = action.payload.timeStamp;

    },
    cleanInvite: (state) => {
      state = initialState;
    },
    addIncomingInvite:(state, action) =>{
      state.incomingInvites[action.payload.id] = action.payload
    },
    deleteIncomingInvite:(state, action) =>{
      delete state.incomingInvites[action.payload]
    },
    clearIncomingInvite:(state) =>{
      state.incomingInvites = []
    }
  },
});

export const { setInvite, cleanInvite, addIncomingInvite, deleteIncomingInvite, clearIncomingInvite } = inviteSlice.actions;

export default inviteSlice.reducer;

