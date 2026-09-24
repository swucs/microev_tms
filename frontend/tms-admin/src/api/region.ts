import { api, unwrap } from './client';
import type { RegionSummary, ResponseDto } from './types';

export const regionApi = {
  search: (params: { regionName?: string; vehicleName?: string; eupMyeonDong?: string }) =>
    unwrap<RegionSummary[]>(api.get<ResponseDto<RegionSummary[]>>('/region', { params })),
  create: (body: { regionName: string; eupMyeonDongs: string[] }) =>
    unwrap<number>(api.post<ResponseDto<number>>('/region', body)),
  detail: (regionSeq: number) =>
    unwrap<unknown>(api.get<ResponseDto<unknown>>(`/region/${regionSeq}`)),
  modify: (regionSeq: number, body: { regionName: string; eupMyeonDongs: string[] }) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/region/${regionSeq}`, body)),
  remove: (regionSeq: number) =>
    unwrap<void>(api.delete<ResponseDto<void>>(`/region/${regionSeq}`)),
};
