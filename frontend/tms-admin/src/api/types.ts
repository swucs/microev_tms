// backend/api.md 기준 DTO 골격. 상세 스키마는 각 모듈 스웨거 참조.

export interface ResponseDto<T> {
  resultCode: string;
  resultMessage: string;
  data: T;
}

// 로그인
export interface LoginRes {
  portalUserSeq: number;
  email: string;
  adminName: string;
  accessToken: string;
  refreshToken: string;
}

export interface ReissueTokenRes {
  accessToken: string;
  refreshToken: string;
}

// 관리자
export interface AdminSummary {
  adminSeq: number;
  email: string;
  adminName: string;
  contact: string;
  position: string;
  lastAccessedAt: string;
  statusCd: string;
  statusCdName: string;
}

// 센터
export interface CenterSummary {
  centerSeq: number;
  centerName: string;
  centerTypeCd: string;
  centerTypeCdName: string;
  zipCode: string;
  centerAddr1: string;
  centerAddr2: string;
  managerName: string;
  contactNum: string;
  usageYn: string;
  vehicleCount: number;
}

// 차량
export interface VehicleSummary {
  vehicleSeq: number;
  centerSeq: number;
  centerName: string;
  regionSeq: number;
  regionName: string;
  driverSeq: number;
  driverName: string;
  vehicleName: string;
  model: string;
  vehicleTypeCd: string;
  vehicleTypeCdName: string;
  vehicleNum: string;
}

// 기사
export interface DriverSummary {
  driverSeq: number;
  driverName: string;
  centerSeq: number;
  centerName: string;
  loginId: string;
  driverPhoneNum: string;
  email: string;
  statusCd: string;
}

// 권역
export interface RegionSummary {
  regionSeq: number;
  regionName: string;
  eupMyeonDong: string;
  vehicleName: string;
  creatorName: string;
  createdAt: string;
}

// 배차
export interface DispatchSummary {
  dispatchSeq: number;
  dispatchName: string;
  deliveryDate: string;
  centerSeq: number;
  centerName: string;
  dispatchStatusCd: string;
  dispatchStatusCdName: string;
  deliveryCount: number;
  deliveryVehicleCount: number;
}

// 공통코드
export interface ValidCode {
  comCodeGroupCd: string;
  comCodeCd: string;
  comCodeName: string;
  sortOrder: number;
}

export interface CommonCodeGroup {
  comCodeGroupCd: string;
  comCodeGroupName: string;
}

export interface CommonCodeItem {
  comCodeSeq: number;
  comCodeGroupCd: string;
  comCodeCd: string;
  comCodeName: string;
  sortOrder: number;
  usageYn: string;
}
