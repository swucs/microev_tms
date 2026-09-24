import { api, unwrap } from './client';
import type { AdminSummary, ResponseDto } from './types';

export interface SearchAdminsParams {
  email?: string;
  adminName?: string;
  statusCd?: string;
}

export const adminApi = {
  search: (params: SearchAdminsParams) =>
    unwrap<AdminSummary[]>(api.get<ResponseDto<AdminSummary[]>>('/admin', { params })),
  create: (body: { email: string; password: string; adminName: string; contact?: string; position?: string }) =>
    unwrap<number>(api.post<ResponseDto<number>>('/admin', body)),
  modify: (adminSeq: number, body: { password?: string; adminName?: string; contact?: string; position?: string; statusCd?: string }) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/admin/${adminSeq}`, body)),
};
