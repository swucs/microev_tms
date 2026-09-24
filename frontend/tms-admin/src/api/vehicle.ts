import { api, unwrap } from './client';
import type { VehicleSummary, ResponseDto } from './types';

export const vehicleApi = {
  search: (params: { vehicleName?: string; centerName?: string; vehicleTypeCd?: string }) =>
    unwrap<VehicleSummary[]>(api.get<ResponseDto<VehicleSummary[]>>('/vehicle', { params })),
  create: (body: Record<string, unknown>) =>
    unwrap<number>(api.post<ResponseDto<number>>('/vehicle', body)),
  modify: (vehicleSeq: number, body: Record<string, unknown>) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/vehicle/${vehicleSeq}`, body)),
  remove: (vehicleSeq: number) =>
    unwrap<void>(api.delete<ResponseDto<void>>(`/vehicle/${vehicleSeq}`)),
};
