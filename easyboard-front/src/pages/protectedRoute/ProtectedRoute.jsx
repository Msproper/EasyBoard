
import { Navigate, Outlet,useNavigate } from 'react-router-dom';
import { useSelector, useStore } from 'react-redux';
import { inviteStatusTypes } from '@/const/inviteStatusTypes';
import { useContext } from 'react';
import { AppContext } from '@/utils/context';
import { notificationTypesClasses } from '@/const/notificationTypesClasses';
import { useEffect } from 'react';
import { subscribeToChannel } from '@/api/socket/subscribeToChannel';
import { store } from '@/reduxStore';
export const ProtectedRoute = () => {
  const user = useStore().getState().auth.user;

  const connected = useSelector((state) => state.websocket.connected);
  const {showAlert} = useContext(AppContext)

  useEffect(() => {
    if (connected){
      const onInviteResponse = (data) => {
        if (data.status === inviteStatusTypes.ACCEPTED) {
          showAlert(notificationTypesClasses.SUCCESS, "Вы были успешно приняты в комнату "+data.boardTitle)
        } 
        else if (data.status === inviteStatusTypes.DECLINED) {
          showAlert( notificationTypesClasses.ERROR, "Ваша заявка на доступ к доске "+data.boardTitle+" был отклонён",)
        }
        else if (data.status === inviteStatusTypes.BANNED
        ) {
          showAlert(notificationTypesClasses.ERROR, "Вы были заблокированы для доступа к доске "+data.boardTitle+". Обратитесь к владельцу доски чтобы выйти из черного списка", )
        }
        // else store.dispatch(deleteIncomingInvite(data.id))
      };
      const subscription = subscribeToChannel(store, '/users/queue/invite.Response', onInviteResponse);
      
    
      return () => {
        subscription.unsubscribe();
      };
    }
  }, [connected]);


  if (!user) {
      return <Navigate to={'/login'} replace={true} />;
    } else return <Outlet></Outlet>
};