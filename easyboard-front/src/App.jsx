import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { store } from './reduxStore.js';
import WelcomePage from './pages/welcome/WelcomePage.jsx';
import DashboardPage from './pages/dashboard/DashboardPage.jsx';
import Login from './pages/login/LoginPage.jsx';
import {ProtectedRoute} from './pages/protectedRoute/ProtectedRoute.jsx';
import Layout from './components/Layout/Layout.jsx';
import Board from './pages/board.jsx/board.jsx';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { initStompClient, sendMessage } from './api/socket/boardSocket.js';
import { subscribeToChannel } from './api/socket/subscribeToChannel.jsx';
import { addIncomingInvite, deleteIncomingInvite } from './api/invites/inviteSlice.js';
import { inviteStatusTypes } from './const/inviteStatusTypes.js';


function App() {
  const token = useSelector((state) => state.auth.token)
  const user = useSelector((state) => state.auth.user)

  useEffect(() => {
    const handleBeforeUnload = () => {
      disconnectStomp(); // аккуратно отключиться
    };
  
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, []);



  useEffect(() => {
    if (token) {
      initStompClient(token, store);
    }
  }, [token]);
  


  const connected = useSelector((state) => state.websocket.connected);
  useEffect(() => {
    if (connected){
      const onInviteResponse = (data) => {
        if (data.status === inviteStatusTypes.PENDING) {
          console.log(data.status)
          store.dispatch(addIncomingInvite(data))
        }
        else store.dispatch(deleteIncomingInvite(data.id))
      };
      const subscription = subscribeToChannel(store, '/users/queue/invite.Requests', onInviteResponse);
      
    
      return () => {
        subscription.unsubscribe();
      };
    }
  }, [connected]);

  
  return (
    <Router>
      <Routes>
        <Route element={<Layout/>}>
          <Route element={<ProtectedRoute/>}>
              <Route path="/dashboard/*" element={<DashboardPage />} />
              <Route path="/boards/:roomId" element={<Board />} />
          </Route>
          <Route path="/login" element={<Login />} />
          <Route path='/' element={<WelcomePage />}/>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;