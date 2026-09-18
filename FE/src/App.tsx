import { Navigate, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './auth/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import AppLayout from './components/AppLayout';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ChatLayoutPage from './pages/ChatLayoutPage';
import ChatEmptyPage from './pages/ChatEmptyPage';
import ChatRoomPage from './pages/ChatRoomPage';
import StatsPage from './pages/StatsPage';
import './App.css';

function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />

        <Route
          element={
            <ProtectedRoute>
              <AppLayout />
            </ProtectedRoute>
          }
        >
          <Route path="/chat" element={<ChatLayoutPage />}>
            <Route index element={<ChatEmptyPage />} />
            <Route path=":chatId" element={<ChatRoomPage />} />
          </Route>
          <Route path="/stats" element={<StatsPage />} />
        </Route>

        <Route path="*" element={<Navigate to="/chat" replace />} />
      </Routes>
    </AuthProvider>
  );
}

export default App;
