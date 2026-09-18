import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errore, setErrore] = useState<string | null>(null);
  const [successo, setSuccesso] = useState(false);
  const [caricamento, setCaricamento] = useState(false);

  async function handleSubmit(e: FormEvent) {
    e.preventDefault();
    setErrore(null);
    setCaricamento(true);
    try {
      await register({ username, email, password });
      setSuccesso(true);
      setTimeout(() => navigate('/login'), 1200);
    } catch (err) {
      setErrore(err instanceof Error ? err.message : 'errore di registrazione');
    } finally {
      setCaricamento(false);
    }
  }

  return (
    <div className="auth-page">
      <form className="auth-form" onSubmit={handleSubmit}>
        <h1>Registrati</h1>
        {errore && <p className="errore">{errore}</p>}
        {successo && <p className="successo">Registrazione completata, reindirizzamento al login...</p>}
        <label>
          Username
          <input value={username} onChange={(e) => setUsername(e.target.value)} required autoFocus />
        </label>
        <label>
          Email
          <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        </label>
        <label>
          Password
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
            minLength={8}
          />
        </label>
        <button type="submit" disabled={caricamento}>
          {caricamento ? 'Creazione in corso...' : 'Crea account'}
        </button>
        <p>
          Hai gia' un account? <Link to="/login">Accedi</Link>
        </p>
      </form>
    </div>
  );
}
