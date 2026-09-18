import { api } from './client';
import type { LoginRequest, LoginResponse, RegisterRequest, UserResponse } from '../types';

export function login(richiesta: LoginRequest): Promise<LoginResponse> {
  return api.post<LoginResponse>('/auth/login', richiesta).then((res) => res.data);
}

export function register(richiesta: RegisterRequest): Promise<UserResponse> {
  return api.post<UserResponse>('/auth/register', richiesta).then((res) => res.data);
}
