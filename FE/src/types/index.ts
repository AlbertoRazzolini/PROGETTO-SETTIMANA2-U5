export interface UserResponse {
  id: string;
  username: string;
  email: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  utente: UserResponse;
}

export interface ChatResponse {
  id: string;
  utente1: UserResponse;
  utente2: UserResponse;
}

export interface MessageResponse {
  id: string;
  chatId: string;
  mittente: UserResponse;
  contenuto: string;
  inviatoIl: string;
  letto: boolean;
}

export interface SendMessageRequest {
  chatId: string;
  contenuto: string;
}

export interface SuggestionResponse {
  suggerimento: string;
}

export interface StatisticsResponse {
  messaggiInviati: number;
  messaggiRicevuti: number;
  chatAperte: number;
}

export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  messages: string[];
}
