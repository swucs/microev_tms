import { api, unwrap } from './client';
import type { DriverSummary, ResponseDto } from './types';

export const driverApi = {
  search: (params: { driverName?: string; centerName?: string; statusCd?: string }) =>
    unwrap<DriverSummary[]>(api.get<ResponseDto<DriverSummary[]>>('/driver', { params })),
  create: (body: Record<string, unknown>) =>
    unwrap<number>(api.post<ResponseDto<number>>('/driver', body)),
  modify: (driverSeq: number, body: Record<string, unknown>) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/driver/${driverSeq}`, body)),
  modifyPassword: (driverSeq: number, password: string) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/driver/${driverSeq}/password`, { password })),
  remove: (driverSeq: number) =>
    unwrap<void>(api.delete<ResponseDto<void>>(`/driver/${driverSeq}`)),
};
