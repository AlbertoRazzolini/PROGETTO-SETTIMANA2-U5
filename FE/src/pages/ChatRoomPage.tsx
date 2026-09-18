import { useCallback, useEffect, useRef, useState, type FormEvent } from 'react';
import { useOutletContext, useParams } from 'react-router-dom';
import type { MessageResponse } from '../types';
import { cronologiaMessaggi, richiediSuggerimento, segnaComeLetti } from '../api/chats';
import { useAuth } from '../auth/AuthContext';
import { useChatSocket } from '../ws/useChatSocket';
import type { ChatOutletContext } from './ChatLayoutPage';

function formattaOra(iso: string): string {
  return new Date(iso).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
}

export default function ChatRoomPage() {
  const { chatId } = useParams<{ chatId: string }>();
  const { chats } = useOutletContext<ChatOutletContext>();
  const { utente } = useAuth();

  const [messaggi, setMessaggi] = useState<MessageResponse[]>([]);
  const [testo, setTesto] = useState('');
  const [errore, setErrore] = useState<string | null>(null);
  const [caricamento, setCaricamento] = useState(true);
  const [suggerendo, setSuggerendo] = useState(false);
  const listaRef = useRef<HTMLDivElement>(null);

  const chat = chats.find((c) => c.id === chatId);
  const altroPartecipante = chat && utente
    ? (chat.utente1.id === utente.id ? chat.utente2 : chat.utente1)
    : null;

  const handleMessaggioRicevuto = useCallback(
    (messaggio: MessageResponse) => {
      if (messaggio.chatId !== chatId) {
        return;
      }
      setMessaggi((prev) => (prev.some((m) => m.id === messaggio.id) ? prev : [...prev, messaggio]));

      if (messaggio.mittente.id !== utente?.id && chatId) {
        segnaComeLetti(chatId).catch(() => undefined);
      }
    },
    [chatId, utente?.id],
  );

  const { stato, inviaMessaggio } = useChatSocket(chatId ?? null, handleMessaggioRicevuto);

  useEffect(() => {
    if (!chatId) {
      return;
    }
    setCaricamento(true);
    setErrore(null);
    cronologiaMessaggi(chatId)
      .then((storico) => setMessaggi(storico))
      .catch((err) => setErrore(err instanceof Error ? err.message : 'errore nel caricamento messaggi'))
      .finally(() => setCaricamento(false));
  }, [chatId]);

  useEffect(() => {
    if (!chatId || caricamento) {
      return;
    }
    segnaComeLetti(chatId)
      .then((aggiornati) => {
        if (aggiornati.length === 0) {
          return;
        }
        const aggiornatiPerId = new Map(aggiornati.map((m) => [m.id, m]));
        setMessaggi((prev) => prev.map((m) => aggiornatiPerId.get(m.id) ?? m));
      })
      .catch(() => undefined);
  }, [chatId, caricamento]);

  useEffect(() => {
    listaRef.current?.scrollTo({ top: listaRef.current.scrollHeight, behavior: 'smooth' });
  }, [messaggi]);

  function handleInvia(e: FormEvent) {
    e.preventDefault();
    if (!chatId || !testo.trim()) {
      return;
    }
    inviaMessaggio(chatId, testo.trim());
    setTesto('');
  }

  async function handleSuggerimento() {
    if (!chatId) {
      return;
    }
    setErrore(null);
    setSuggerendo(true);
    try {
      const { suggerimento } = await richiediSuggerimento(chatId);
      setTesto(suggerimento);
    } catch (err) {
      setErrore(err instanceof Error ? err.message : 'suggerimento non disponibile');
    } finally {
      setSuggerendo(false);
    }
  }

  if (!chatId) {
    return null;
  }

  return (
    <div className="chat-room">
      <header className="chat-room-header">
        <h2>{altroPartecipante?.username ?? 'Chat'}</h2>
        <span className={`ws-status ws-status--${stato}`}>{stato}</span>
      </header>

      {errore && <p className="errore">{errore}</p>}

      <div className="chat-messages" ref={listaRef}>
        {caricamento && <p>Caricamento messaggi...</p>}
        {!caricamento && messaggi.length === 0 && <p className="muted">Nessun messaggio, inizia la conversazione.</p>}
        {messaggi.map((m) => {
          const mio = m.mittente.id === utente?.id;
          return (
            <div key={m.id} className={`chat-bubble ${mio ? 'chat-bubble--mine' : 'chat-bubble--theirs'}`}>
              <p>{m.contenuto}</p>
              <span className="chat-bubble-meta">
                {formattaOra(m.inviatoIl)}
                {mio && (m.letto ? ' · letto' : ' · inviato')}
              </span>
            </div>
          );
        })}
      </div>

      <form className="chat-input" onSubmit={handleInvia}>
        <button
          type="button"
          className="suggerimento-button"
          onClick={handleSuggerimento}
          disabled={suggerendo}
          title="Chiedi un suggerimento all'IA per continuare la conversazione"
        >
          {suggerendo ? 'Penso...' : 'Suggerimento'}
        </button>
        <input
          value={testo}
          onChange={(e) => setTesto(e.target.value)}
          placeholder="Scrivi un messaggio..."
          disabled={stato !== 'connesso'}
        />
        <button type="submit" disabled={stato !== 'connesso' || !testo.trim()}>
          Invia
        </button>
      </form>
    </div>
  );
}
