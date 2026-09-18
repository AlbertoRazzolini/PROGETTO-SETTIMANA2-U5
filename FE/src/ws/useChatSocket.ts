import { useCallback, useEffect, useRef, useState } from 'react';
import { Client, type IMessage } from '@stomp/stompjs';
import type { MessageResponse } from '../types';
import { getToken } from '../auth/session';

const wsUrl = import.meta.env.VITE_WS_URL ?? 'ws://localhost:8080/ws';

type Stato = 'disconnesso' | 'connessione' | 'connesso';

/** Mantiene una singola connessione STOMP e la sottoscrizione alla chat aperta. */
export function useChatSocket(chatId: string | null, onMessaggio: (m: MessageResponse) => void) {
  const clientRef = useRef<Client | null>(null);
  const [stato, setStato] = useState<Stato>('disconnesso');
  const onMessaggioRef = useRef(onMessaggio);
  onMessaggioRef.current = onMessaggio;

  useEffect(() => {
    const token = getToken();
    if (!token) {
      return;
    }

    const client = new Client({
      brokerURL: wsUrl,
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 3000,
      onConnect: () => setStato('connesso'),
      onWebSocketClose: () => setStato('disconnesso'),
      onStompError: () => setStato('disconnesso'),
    });

    setStato('connessione');
    client.activate();
    clientRef.current = client;

    return () => {
      client.deactivate();
      clientRef.current = null;
    };
  }, []);

  useEffect(() => {
    const client = clientRef.current;
    if (!client || stato !== 'connesso' || !chatId) {
      return;
    }

    const subscription = client.subscribe(`/topic/chat/${chatId}`, (frame: IMessage) => {
      const messaggio = JSON.parse(frame.body) as MessageResponse;
      onMessaggioRef.current(messaggio);
    });

    return () => subscription.unsubscribe();
  }, [chatId, stato]);

  const inviaMessaggio = useCallback((chatIdDestinazione: string, contenuto: string) => {
    clientRef.current?.publish({
      destination: '/app/chat.send',
      body: JSON.stringify({ chatId: chatIdDestinazione, contenuto }),
    });
  }, []);

  return { stato, inviaMessaggio };
}
