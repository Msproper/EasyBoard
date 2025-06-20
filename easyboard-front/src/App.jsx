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
import { InviteRoute } from './pages/InviteRoute/InviteRoute.jsx';
function App() {
  useEffect(() => {
    const handleBeforeUnload = () => {
      disconnectStomp(); // аккуратно отключиться
    };
  
    window.addEventListener('beforeunload', handleBeforeUnload);
    return () => window.removeEventListener('beforeunload', handleBeforeUnload);
  }, []);
  
  
  return (
    <Router>
      <Routes>
        <Route element={<Layout/>}>
          <Route element={<ProtectedRoute/>}>
              <Route path="/dashboard/*" element={<DashboardPage />} />
              <Route path="/board/" element={<Board />} />
              <Route path='/invite/:inviteUuid' element={<InviteRoute></InviteRoute>}></Route>
          </Route>
          <Route path="/login" element={<Login />} />
          <Route path='/' element={<WelcomePage />}/>
        </Route>
      </Routes>
    </Router>
  );
}

export default App;