import { api, unwrap } from './client';
import type { ResponseDto } from './types';

export const commonApi = {
  coordinate: (address: string) =>
    unwrap<{ latitude: string; longitude: string }>(
      api.get<ResponseDto<{ latitude: string; longitude: string }>>('/common/coordinate', { params: { address } }),
    ),
  eupMyeonDong: (address: string) =>
    unwrap<{ eupMyeonDong: string }>(
      api.get<ResponseDto<{ eupMyeonDong: string }>>('/common/eupMyeonDong', { params: { address } }),
    ),
};
