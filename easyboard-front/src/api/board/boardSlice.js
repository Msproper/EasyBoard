import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  boardId: null,
  boardUuid: null,
};

const boardSlice = createSlice({
  name: 'board',
  initialState,
  reducers: {
    setBoard: (state, action) => {
      state.boardId = action.payload.boardId;
      state.boardUuid = action.payload.boardUuid;
      localStorage.setItem('board', JSON.stringify(state)); 
    },
    unsetBoard:(state) =>{
      state = initialState;
      localStorage.removeItem("board")
    }
  },
});


export const { 
  setBoard,
  unsetBoard
} = boardSlice.actions;

export default boardSlice.reducer;

const storedBoard = localStorage.getItem('board');
if (storedBoard) {
  const parsedBoard = JSON.parse(storedBoard);
  initialState.boardId = parsedBoard.boardId
  initialState.boardUuid = parsedBoard.boardUuid
}
