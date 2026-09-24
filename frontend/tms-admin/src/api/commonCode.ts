import { api, unwrap } from './client';
import type { CommonCodeGroup, CommonCodeItem, ResponseDto, ValidCode } from './types';

export const commonCodeApi = {
  validCodes: (comCodeGroupCd: string) =>
    unwrap<ValidCode[]>(api.get<ResponseDto<ValidCode[]>>(`/common-code/valid-common-codes/${comCodeGroupCd}`)),
  groups: (params: { comCodeGroupCd?: string; comCodeGroupName?: string }) =>
    unwrap<CommonCodeGroup[]>(api.get<ResponseDto<CommonCodeGroup[]>>('/common-code/common-code-groups', { params })),
  createGroup: (body: { comCodeGroupCd: string; comCodeGroupName: string }) =>
    unwrap<unknown>(api.post<ResponseDto<unknown>>('/common-code/common-code-group', body)),
  modifyGroup: (body: { comCodeGroupCd: string; comCodeGroupName: string }) =>
    unwrap<unknown>(api.put<ResponseDto<unknown>>('/common-code/common-code-group', body)),
  removeGroup: (comCodeGroupCds: string[]) =>
    unwrap<void>(api.delete<ResponseDto<void>>('/common-code/common-code-group', { data: { comCodeGroupCds } })),
  codes: (comCodeGroupCd: string) =>
    unwrap<CommonCodeItem[]>(api.get<ResponseDto<CommonCodeItem[]>>(`/common-code/common-codes/${comCodeGroupCd}`)),
  createCode: (body: Record<string, unknown>) =>
    unwrap<number>(api.post<ResponseDto<number>>('/common-code/common-code', body)),
  modifyCode: (comCodeSeq: number, body: Record<string, unknown>) =>
    unwrap<void>(api.put<ResponseDto<void>>(`/common-code/common-code/${comCodeSeq}`, body)),
  removeCode: (comCodeSeqs: number[]) =>
    unwrap<void>(api.delete<ResponseDto<void>>('/common-code/common-code', { data: { comCodeSeqs } })),
};
