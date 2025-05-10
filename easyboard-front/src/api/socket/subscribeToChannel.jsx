
import { getStompClient, sendMessage} from './boardSocket';
import { addSubscription, removeSubscription } from './websocketSlice';



export const subscribeToChannel = (store, channel, callback) => {
  const client = getStompClient();

  const subscription = client.subscribe(channel, (message) => {
    callback(JSON.parse(message.body));
  }, {
    Authorization: `Bearer ${store.getState().auth.token}`
  });
  store.dispatch(addSubscription(channel, callback));
  return subscription;
};

export const unsubscribeFromChannel = (store, channel) => {
  const client = getStompClient();
  const subscription = store.getState().websocket.subscriptions[channel];
  if (subscription) {
    subscription.unsubscribe();
    store.dispatch(removeSubscription(channel));
  }
};
