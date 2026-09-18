import { createContext, useContext, useMemo, useState, type ReactNode } from 'react';
import type { LoginRequest, RegisterRequest, UserResponse } from '../types';
import { login as loginRequest, register as registerRequest } from '../api/auth';
import { clearSession, getStoredUser, getToken, saveSession } from './session';

interface AuthContextValue {
  utente: UserResponse | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (richiesta: LoginRequest) => Promise<void>;
  register: (richiesta: RegisterRequest) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [utente, setUtente] = useState<UserResponse | null>(() => getStoredUser());
  const [token, setToken] = useState<string | null>(() => getToken());

  const value = useMemo<AuthContextValue>(
    () => ({
      utente,
      token,
      isAuthenticated: Boolean(token && utente),
      async login(richiesta) {
        const risposta = await loginRequest(richiesta);
        saveSession(risposta.token, risposta.utente);
        setToken(risposta.token);
        setUtente(risposta.utente);
      },
      async register(richiesta) {
        await registerRequest(richiesta);
      },
      logout() {
        clearSession();
        setToken(null);
        setUtente(null);
      },
    }),
    [utente, token],
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) {
    throw new Error('useAuth deve essere usato dentro un AuthProvider');
  }
  return ctx;
}
