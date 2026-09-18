import axios, { type AxiosError } from 'axios';
import type { ApiError } from '../types';
import { getToken, clearSession } from '../auth/session';

const baseURL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api';

export const api = axios.create({ baseURL });

api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiError>) => {
    if (error.response?.status === 401) {
      clearSession();
    }
    const messaggi = error.response?.data?.messages;
    const messaggio = messaggi && messaggi.length > 0 ? messaggi.join(', ') : error.message;
    return Promise.reject(new Error(messaggio));
  },
);
