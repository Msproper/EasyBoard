import { unsetBoard } from "../board/boardSlice";

const authMiddleware = (store) => (next) => (action) => {
  if (action.type === 'auth/logout' || action.type === 'authApi/logout/fulfilled') {
    store.dispatch(unsetBoard())
  }

  return next(action);
};
export default authMiddleware  