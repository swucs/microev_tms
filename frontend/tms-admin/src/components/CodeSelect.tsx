import { Select } from 'antd';
import { useQuery } from '@tanstack/react-query';
import { commonCodeApi } from '../api/commonCode';

interface Props {
  group: string;
  placeholder?: string;
  value?: string;
  onChange?: (v: string) => void;
}

/** 공통코드 셀렉트. 코드값 하드코딩 금지, valid-common-codes로 채움. */
export default function CodeSelect({ group, placeholder, value, onChange }: Props) {
  const { data, isLoading } = useQuery({
    queryKey: ['codes', group],
    queryFn: () => commonCodeApi.validCodes(group),
    staleTime: 5 * 60 * 1000,
  });

  return (
    <Select
      allowClear
      showSearch
      optionFilterProp="label"
      placeholder={placeholder}
      loading={isLoading}
      value={value}
      onChange={onChange}
      options={(data ?? []).map((c) => ({ value: c.comCodeCd, label: c.comCodeName }))}
    />
  );
}
