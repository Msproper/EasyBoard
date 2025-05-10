import { Client } from '@stomp/stompjs';
import { setConnected } from './websocketSlice';

let stompClient = null;
let activeToken = null;

export const initStompClient = (token, store) => {
  if (stompClient && activeToken === token) return stompClient;

  if (stompClient) {
    stompClient.deactivate();
  }

  activeToken = token;

  stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    connectHeaders: {
      Authorization: `Bearer ${token}`
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    debug: (str) => console.log('[STOMP]', str),
    onConnect: () => {
      console.log('WebSocket connected');
      store.dispatch(setConnected(true));
    },
    onDisconnect: () => {
      console.log('WebSocket disconnected');
      store.dispatch(setConnected(false));
    },
    onStompError: (frame) => {
      console.error('STOMP error:', frame.headers);
    }
  });

  stompClient.activate();
  return stompClient;
};

export const getStompClient = () => {
  if (!stompClient) throw new Error('STOMP client not initialized');
  return stompClient;
};

export const sendMessage = (destination, payload) =>{
  if (stompClient){
    stompClient.publish({destination:"/app"+destination, headers:{
      Authorization: `Bearer ${activeToken}`
    }, body:payload})
  }
}

export const disconnectStomp = () => {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
    activeToken = null;
  }
};
