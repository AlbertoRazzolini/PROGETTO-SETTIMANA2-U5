import { useCallback, useEffect, useState } from 'react';
import { NavLink, Outlet, useNavigate } from 'react-router-dom';
import type { ChatResponse, UserResponse } from '../types';
import { listaChat, apriChat } from '../api/chats';
import { listaAltriUtenti } from '../api/users';
import { useAuth } from '../auth/AuthContext';

export interface ChatOutletContext {
  chats: ChatResponse[];
}

function nomeAltroPartecipante(chat: ChatResponse, ioId: string): string {
  return chat.utente1.id === ioId ? chat.utente2.username : chat.utente1.username;
}

export default function ChatLayoutPage() {
  const { utente } = useAuth();
  const navigate = useNavigate();
  const [chats, setChats] = useState<ChatResponse[]>([]);
  const [utenti, setUtenti] = useState<UserResponse[]>([]);
  const [errore, setErrore] = useState<string | null>(null);
  const [caricamento, setCaricamento] = useState(true);

  const caricaTutto = useCallback(async () => {
    try {
      const [chatList, userList] = await Promise.all([listaChat(), listaAltriUtenti()]);
      setChats(chatList);
      setUtenti(userList);
    } catch (err) {
      setErrore(err instanceof Error ? err.message : 'errore nel caricamento');
    } finally {
      setCaricamento(false);
    }
  }, []);

  useEffect(() => {
    caricaTutto();
  }, [caricaTutto]);

  async function handleApriChat(altroUtenteId: string) {
    try {
      const chat = await apriChat(altroUtenteId);
      setChats((prev) => (prev.some((c) => c.id === chat.id) ? prev : [...prev, chat]));
      navigate(`/chat/${chat.id}`);
    } catch (err) {
      setErrore(err instanceof Error ? err.message : 'impossibile aprire la chat');
    }
  }

  const utentiConChatGiaAperta = new Set(
    chats.map((c) => (c.utente1.id === utente?.id ? c.utente2.id : c.utente1.id)),
  );
  const utentiSenzaChat = utenti.filter((u) => !utentiConChatGiaAperta.has(u.id));

  return (
    <div className="chat-layout">
      <aside className="chat-sidebar">
        {errore && <p className="errore">{errore}</p>}
        {caricamento ? (
          <p>Caricamento...</p>
        ) : (
          <>
            <section>
              <h2>Le tue chat</h2>
              {chats.length === 0 && <p className="muted">Nessuna chat aperta</p>}
              <ul className="chat-list">
                {chats.map((chat) => (
                  <li key={chat.id}>
                    <NavLink to={`/chat/${chat.id}`} className={({ isActive }) => (isActive ? 'active' : '')}>
                      {nomeAltroPartecipante(chat, utente?.id ?? '')}
                    </NavLink>
                  </li>
                ))}
              </ul>
            </section>
            <section>
              <h2>Utenti</h2>
              {utentiSenzaChat.length === 0 && <p className="muted">Nessun altro utente disponibile</p>}
              <ul className="chat-list">
                {utentiSenzaChat.map((u) => (
                  <li key={u.id}>
                    <button type="button" className="user-button" onClick={() => handleApriChat(u.id)}>
                      {u.username}
                    </button>
                  </li>
                ))}
              </ul>
            </section>
          </>
        )}
      </aside>
      <main className="chat-main">
        <Outlet context={{ chats } satisfies ChatOutletContext} />
      </main>
    </div>
  );
}
