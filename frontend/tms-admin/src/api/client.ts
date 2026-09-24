import axios, { AxiosError, type InternalAxiosRequestConfig } from 'axios';
import type { ResponseDto, ReissueTokenRes } from './types';

const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8090/tms/admin';

const ACCESS_KEY = 'tms-access-token';
const REFRESH_KEY = 'tms-refresh-token';
const EMAIL_KEY = 'tms-admin-email';

export const tokenStore = {
  getAccess: () => localStorage.getItem(ACCESS_KEY),
  getRefresh: () => localStorage.getItem(REFRESH_KEY),
  getEmail: () => localStorage.getItem(EMAIL_KEY),
  save: (access: string, refresh: string, email: string) => {
    localStorage.setItem(ACCESS_KEY, access);
    localStorage.setItem(REFRESH_KEY, refresh);
    localStorage.setItem(EMAIL_KEY, email);
  },
  clear: () => {
    localStorage.removeItem(ACCESS_KEY);
    localStorage.removeItem(REFRESH_KEY);
    localStorage.removeItem(EMAIL_KEY);
  },
};

export const api = axios.create({ baseURL: BASE_URL });

api.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const token = tokenStore.getAccess();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

let reissuing: Promise<string> | null = null;

async function reissue(): Promise<string> {
  if (!reissuing) {
    reissuing = (async () => {
      const refreshToken = tokenStore.getRefresh();
      const email = tokenStore.getEmail();
      if (!refreshToken || !email) throw new Error('인증 정보 없음');
      const { data } = await axios.put<ResponseDto<ReissueTokenRes>>(
        `${BASE_URL}/login/newToken`,
        { refreshToken, email },
      );
      tokenStore.save(data.data.accessToken, data.data.refreshToken, email);
      return data.data.accessToken;
    })().finally(() => {
      reissuing = null;
    });
  }
  return reissuing;
}

api.interceptors.response.use(
  (res) => res,
  async (error: AxiosError) => {
    const original = error.config as (InternalAxiosRequestConfig & { _retried?: boolean }) | undefined;
    if (error.response?.status === 401 && original && !original._retried) {
      original._retried = true;
      try {
        const token = await reissue();
        original.headers.Authorization = `Bearer ${token}`;
        return api(original);
      } catch {
        tokenStore.clear();
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  },
);

/** ResponseDto 언랩 헬퍼 */
export async function unwrap<T>(promise: Promise<{ data: ResponseDto<T> }>): Promise<T> {
  const { data } = await promise;
  return data.data;
}
