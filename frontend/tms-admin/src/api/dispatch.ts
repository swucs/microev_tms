import { api, unwrap } from './client';
import type { DispatchSummary, ResponseDto } from './types';

export const dispatchApi = {
  search: (params: { centerName?: string; dispatchStatusCd?: string }) =>
    unwrap<DispatchSummary[]>(api.get<ResponseDto<DispatchSummary[]>>('/dispatch', { params })),
  create: (body: { centerSeq: number; dispatchName: string; deliveryDate: string }) =>
    unwrap<number>(api.post<ResponseDto<number>>('/dispatch', body)),
  detail: (dispatchSeq: number) =>
    unwrap<unknown>(api.get<ResponseDto<unknown>>(`/dispatch/${dispatchSeq}`)),
  modify: (dispatchSeq: number, body: { dispatchName: string; deliveryDate: string }) =>
    unwrap<unknown>(api.put<ResponseDto<unknown>>(`/dispatch/${dispatchSeq}`, body)),
  remove: (dispatchSeq: number) =>
    unwrap<void>(api.delete<ResponseDto<void>>(`/dispatch/${dispatchSeq}`)),
  confirm: (dispatchSeq: number, body: { deliveryOrders: { vehicleSeq: number; deliverySeqList: number[] }[] }) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/dispatch/${dispatchSeq}/dispatched`, body)),
  toWaiting: (dispatchSeq: number) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/dispatch/${dispatchSeq}/waiting`)),
  autoDispatch: (body: { centerSeq: number; roadAddress: string }) =>
    unwrap<unknown>(api.post<ResponseDto<unknown>>('/dispatch/autoDispatch', body)),
  // 배송정보
  createDelivery: (body: Record<string, unknown>) =>
    unwrap<number>(api.post<ResponseDto<number>>('/dispatch/delivery', body)),
  modifyDelivery: (deliverySeq: number, body: Record<string, unknown>) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/dispatch/delivery/${deliverySeq}`, body)),
  removeDelivery: (deliverySeq: number) =>
    unwrap<void>(api.delete<ResponseDto<void>>(`/dispatch/delivery/${deliverySeq}`)),
  // 엑셀 (ResponseDto 아님, 바이너리)
  downloadSampleExcel: (dispatchSeq: number) =>
    api.get(`/dispatch/delivery/excel/download-sample-delivery/${dispatchSeq}`, { responseType: 'blob' }),
  uploadExcel: (dispatchSeq: number, file: File) => {
    const form = new FormData();
    form.append('file', file);
    return api.post(`/dispatch/delivery/excel/upload/${dispatchSeq}`, form);
  },
  downloadExcel: (dispatchSeq: number) =>
    api.get(`/dispatch/delivery/excel/download/${dispatchSeq}`, { responseType: 'blob' }),
};
