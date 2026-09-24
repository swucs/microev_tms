import { AxiosError } from 'axios';

export function errMsg(e: unknown, fallback: string): string {
  if (e instanceof AxiosError) {
    const msg = e.response?.data?.resultMessage;
    if (typeof msg === 'string' && msg) return msg;
  }
  return fallback;
}
