import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  boards: [], 
};

const boardSlice = createSlice({
  name: 'board',
  initialState,
  reducers: {
    setBoards: (state, action) => {
      state.boards = action.payload;
    },
    
    addBoards: (state, action) => {
      if (state.boards === null) {
        state.boards = action.payload;
      } else {
        state.boards.push(...action.payload);
      }
    },
    
    addBoard: (state, action) => {
      if (state.boards === null) {
        state.boards = [action.payload];
      } else {
        state.boards.push(action.payload);
      }
    },
    
    removeBoard: (state, action) => {
      if (state.boards !== null) {
        state.boards = state.boards.filter(board => board.id !== action.payload);
      }
    },
    
    updateBoard: (state, action) => {
      if (state.boards !== null) {
        const index = state.boards.findIndex(b => b.id === action.payload.id);
        if (index !== -1) {
          state.boards[index] = { ...state.boards[index], ...action.payload };
        }
      }
    },
    
    clearBoards: (state) => {
      state.boards = null; 
    }
  },
});


export const { 
  setBoards, 
  addBoards, 
  addBoard, 
  removeBoard, 
  updateBoard, 
  clearBoards 
} = boardSlice.actions;

export default boardSlice.reducer;

