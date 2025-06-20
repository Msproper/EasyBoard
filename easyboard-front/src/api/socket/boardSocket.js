import { Client } from '@stomp/stompjs';
import { setConnected, setDisconnected } from './websocketSlice';
import { subscribeToChannel, unsubscribeFromChannel } from './subscribeToChannel';
import { inviteRequestDest, inviteResponseDest } from '@/const/destinations';
import { showNotification } from '../notification/notificationSlice';
import { notificationTypesClasses } from '@/const/notificationTypesClasses';
import { inviteStatusTypes } from '@/const/inviteStatusTypes';
import { addIncomingInvite, deleteIncomingInvite } from '../invites/inviteSlice';
import { boardApi } from '../board/boardApi';

let stompClient = null;
let activeToken = null;

export const initStompClient = (store) => {
  console.log(store)
  const { token } = store.getState().auth;
  if (stompClient && activeToken === token) return stompClient;

  disconnectStomp();
  activeToken = token;

  stompClient = new Client({
    brokerURL: 'ws://localhost:8080/ws',
    connectHeaders: {
      Authorization: `Bearer ${token}`
    },
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    onConnect: () => {
      console.log('WebSocket connected');
      store.dispatch(setConnected());
      subscribeToChannel(store, inviteResponseDest, onInviteRequest)
      subscribeToChannel(store, inviteRequestDest, onInviteResponse)

    },
    onDisconnect: () => {
      console.log('WebSocket disconnected');
      unsubscribeFromChannel(store, inviteResponseDest)
      unsubscribeFromChannel(store, inviteRequestDest)
      store.dispatch(setDisconnected());
    },
    onStompError: (frame) => {
      console.error('STOMP error:', frame);
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


const onInviteResponse = (data, store) => {
  if (data.status === inviteStatusTypes.PENDING) {
    store.dispatch(addIncomingInvite(data))
  }
  else store.dispatch(deleteIncomingInvite(data.id))
};

const onInviteRequest = (data, store) => {
  if (data.status === inviteStatusTypes.ACCEPTED) {
    store.dispatch(showNotification({type:notificationTypesClasses.SUCCESS, message:"Ваша заявка на доступ к доске "+data.boardTitle+" была принята"}))
    store.dispatch(boardApi.util.invalidateTags(['Boards']))
  } 
  else if (data.status === inviteStatusTypes.DECLINED) {
    store.dispatch(showNotification({type:notificationTypesClasses.ERROR, message:"Ваша заявка на доступ к доске "+data.boardTitle+" была отклонена"}))
    store.dispatch(boardApi.util.invalidateTags(['Boards']))

  }
  else if (data.status === inviteStatusTypes.BANNED
  ) {
    store.dispatch(boardApi.util.invalidateTags(['Boards']))
    store.dispatch(showNotification({type:notificationTypesClasses.ERROR, message:"Вы были заблокированы для доступа к доске "+data.boardTitle+". Обратитесь к владельцу доски чтобы выйти из черного списка"}))
  }
};
    