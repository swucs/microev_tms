import { api, unwrap } from './client';
import type { LoginRes, ReissueTokenRes, ResponseDto } from './types';

export const loginApi = {
  login: (email: string, password: string) =>
    unwrap<LoginRes>(api.post<ResponseDto<LoginRes>>('/login', { email, password })),
  reissue: (refreshToken: string, email: string) =>
    unwrap<ReissueTokenRes>(api.put<ResponseDto<ReissueTokenRes>>('/login/newToken', { refreshToken, email })),
};
