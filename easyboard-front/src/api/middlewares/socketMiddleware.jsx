import { initStompClient, disconnectStomp } from "../socket/boardSocket";

const socketMiddleware = (store) => (next) => (action) => {
  if (action.type === 'auth/setUser' || action.type === 'authApi/refresh/fulfilled') {
    initStompClient(store);
  }
  else if (action.type === 'auth/logout') {
    disconnectStomp();
  }

  if (action.type === 'websocket/setConnected') {

  }
  return next(action);
};
export default socketMiddleware  


  