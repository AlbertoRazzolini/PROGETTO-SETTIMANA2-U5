import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export default function Navbar() {
  const { utente, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/login');
  }

  return (
    <header className="navbar">
      <span className="navbar-brand">Chat</span>
      <nav>
        <NavLink to="/chat" end>
          Chat
        </NavLink>
        <NavLink to="/stats">Statistiche</NavLink>
      </nav>
      <div className="navbar-user">
        <span>{utente?.username}</span>
        <button type="button" onClick={handleLogout}>
          Esci
        </button>
      </div>
    </header>
  );
}
