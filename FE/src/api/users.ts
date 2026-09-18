import { api } from './client';
import type { UserResponse } from '../types';

export function listaAltriUtenti(): Promise<UserResponse[]> {
  return api.get<UserResponse[]>('/users').then((res) => res.data);
}
