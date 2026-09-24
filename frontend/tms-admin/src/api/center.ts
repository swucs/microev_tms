import { api, unwrap } from './client';
import type { CenterSummary, ResponseDto } from './types';

export const centerApi = {
  search: (params: { centerName?: string; centerTypeCd?: string; managerName?: string }) =>
    unwrap<CenterSummary[]>(api.get<ResponseDto<CenterSummary[]>>('/center', { params })),
  create: (body: Record<string, unknown>) =>
    unwrap<number>(api.post<ResponseDto<number>>('/center', body)),
  detail: (centerSeq: number) =>
    unwrap<CenterSummary>(api.get<ResponseDto<CenterSummary>>(`/center/${centerSeq}`)),
  modify: (centerSeq: number, body: Record<string, unknown>) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/center/${centerSeq}`, body)),
  remove: (centerSeq: number) =>
    unwrap<void>(api.delete<ResponseDto<void>>(`/center/${centerSeq}`)),
  vehicles: (centerSeq: number) =>
    unwrap<unknown[]>(api.get<ResponseDto<unknown[]>>(`/center/${centerSeq}/vehicle`)),
};
