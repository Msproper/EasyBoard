import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { store } from './reduxStore.js';
import WelcomePage from './pages/welcome/WelcomePage.jsx';
import DashboardPage from './pages/dashboard/DashboardPage.jsx';
import Login from './pages/login/LoginPage.jsx';
import {ProtectedRoute} from './pages/protectedRoute/ProtectedRoute.jsx';
import Layout from './pages/Layout/Layout.jsx';
import Board from './pages/board.jsx/Board.jsx';
import { useEffect } from 'react';
import { useSelector } from 'react-redux';
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
  
  const connected = useSelector((state) => state.websocket.connected);
  
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