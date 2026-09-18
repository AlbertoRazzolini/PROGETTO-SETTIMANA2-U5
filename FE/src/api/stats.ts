import { api } from './client';
import type { StatisticsResponse } from '../types';

export function statisticheMie(): Promise<StatisticsResponse> {
  return api.get<StatisticsResponse>('/stats/me').then((res) => res.data);
}

export function inviaStatisticheViaEmail(): Promise<void> {
  return api.post('/stats/me/email').then(() => undefined);
}
