import { useEffect, useState } from 'react';
import type { StatisticsResponse } from '../types';
import { inviaStatisticheViaEmail, statisticheMie } from '../api/stats';

export default function StatsPage() {
  const [stats, setStats] = useState<StatisticsResponse | null>(null);
  const [errore, setErrore] = useState<string | null>(null);
  const [caricamento, setCaricamento] = useState(true);
  const [invioEmail, setInvioEmail] = useState<'idle' | 'invio' | 'inviata'>('idle');

  useEffect(() => {
    statisticheMie()
      .then(setStats)
      .catch((err) => setErrore(err instanceof Error ? err.message : 'errore nel caricamento statistiche'))
      .finally(() => setCaricamento(false));
  }, []);

  async function handleInviaEmail() {
    setErrore(null);
    setInvioEmail('invio');
    try {
      await inviaStatisticheViaEmail();
      setInvioEmail('inviata');
    } catch (err) {
      setErrore(err instanceof Error ? err.message : "invio dell'email non riuscito");
      setInvioEmail('idle');
    }
  }

  return (
    <div className="stats-page">
      <h1>Le tue statistiche</h1>
      {errore && <p className="errore">{errore}</p>}
      {caricamento && <p>Caricamento...</p>}
      {stats && (
        <div className="stats-grid">
          <div className="stat-card">
            <span className="stat-value">{stats.messaggiInviati}</span>
            <span className="stat-label">Messaggi inviati</span>
          </div>
          <div className="stat-card">
            <span className="stat-value">{stats.messaggiRicevuti}</span>
            <span className="stat-label">Messaggi ricevuti</span>
          </div>
          <div className="stat-card">
            <span className="stat-value">{stats.chatAperte}</span>
            <span className="stat-label">Chat aperte</span>
          </div>
        </div>
      )}
      <button type="button" onClick={handleInviaEmail} disabled={invioEmail !== 'idle'}>
        {invioEmail === 'invio' && 'Invio in corso...'}
        {invioEmail === 'inviata' && 'Email inviata!'}
        {invioEmail === 'idle' && 'Invia statistiche via email'}
      </button>
    </div>
  );
}
