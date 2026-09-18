import { api } from './client';
import type { ChatResponse, MessageResponse, SuggestionResponse } from '../types';

export function listaChat(): Promise<ChatResponse[]> {
  return api.get<ChatResponse[]>('/chats').then((res) => res.data);
}

export function apriChat(altroUtenteId: string): Promise<ChatResponse> {
  return api.post<ChatResponse>('/chats', { altroUtenteId }).then((res) => res.data);
}

export function cronologiaMessaggi(chatId: string): Promise<MessageResponse[]> {
  return api.get<MessageResponse[]>(`/chats/${chatId}/messages`).then((res) => res.data);
}

export function segnaComeLetti(chatId: string): Promise<MessageResponse[]> {
  return api.patch<MessageResponse[]>(`/chats/${chatId}/messages/read`).then((res) => res.data);
}

export function richiediSuggerimento(chatId: string): Promise<SuggestionResponse> {
  return api.post<SuggestionResponse>(`/chats/${chatId}/suggest`).then((res) => res.data);
}
