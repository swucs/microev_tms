import { useState } from 'react';
import { App as AntdApp, Button, Card, Form, Input, InputNumber, Modal, Popconfirm, Select, Space, Table } from 'antd';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { vehicleApi } from '../api/vehicle';
import { centerApi } from '../api/center';
import { driverApi } from '../api/driver';
import { regionApi } from '../api/region';
import type { VehicleSummary } from '../api/types';
import CodeSelect from '../components/CodeSelect';
import { errMsg } from '../components/errorMsg';

export default function VehiclePage() {
  const { message } = AntdApp.useApp();
  const qc = useQueryClient();
  const [modalForm] = Form.useForm();
  const [params, setParams] = useState<Record<string, string>>({});
  const [editing, setEditing] = useState<VehicleSummary | null>(null);
  const [modalOpen, setModalOpen] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['vehicles', params],
    queryFn: () => vehicleApi.search(params),
  });
  const { data: centers } = useQuery({ queryKey: ['centers-all'], queryFn: () => centerApi.search({}) });
  const { data: drivers } = useQuery({ queryKey: ['drivers-all'], queryFn: () => driverApi.search({}) });
  const { data: regions } = useQuery({ queryKey: ['regions-all'], queryFn: () => regionApi.search({}) });

  const invalidate = () => qc.invalidateQueries({ queryKey: ['vehicles'] });

  const save = useMutation({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    mutationFn: async (values: any) => {
      if (editing) await vehicleApi.modify(editing.vehicleSeq, values);
      else await vehicleApi.create(values);
    },
    onSuccess: () => {
      message.success('저장됨');
      setModalOpen(false);
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '저장 실패')),
  });

  const remove = useMutation({
    mutationFn: (seq: number) => vehicleApi.remove(seq),
    onSuccess: () => {
      message.success('삭제됨');
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '삭제 실패')),
  });

  const openCreate = () => {
    setEditing(null);
    modalForm.resetFields();
    setModalOpen(true);
  };
  const openEdit = (row: VehicleSummary) => {
    setEditing(row);
    modalForm.setFieldsValue(row);
    setModalOpen(true);
  };

  return (
    <Space direction="vertical" style={{ width: '100%' }} size="middle">
      <Card>
        <Form layout="inline" onFinish={(v) => setParams(v as Record<string, string>)}>
          <Form.Item name="vehicleName">
            <Input placeholder="차량명" allowClear />
          </Form.Item>
          <Form.Item name="centerName">
            <Input placeholder="센터명" allowClear />
          </Form.Item>
          <Form.Item name="vehicleTypeCd" style={{ width: 160 }}>
            <CodeSelect group="VehicleType" placeholder="차량유형" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              조회
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="차량 목록" extra={<Button type="primary" onClick={openCreate}>등록</Button>}>
        <Table<VehicleSummary>
          rowKey="vehicleSeq"
          loading={isLoading}
          dataSource={data}
          columns={[
            { title: '차량명', dataIndex: 'vehicleName' },
            { title: '센터', dataIndex: 'centerName' },
            { title: '기사', dataIndex: 'driverName' },
            { title: '권역', dataIndex: 'regionName' },
            { title: '유형', dataIndex: 'vehicleTypeCdName' },
            { title: '차량번호', dataIndex: 'vehicleNum' },
            {
              title: '관리',
              render: (_, row) => (
                <Space>
                  <Button size="small" onClick={() => openEdit(row)}>
                    수정
                  </Button>
                  <Popconfirm title="삭제할까?" onConfirm={() => remove.mutate(row.vehicleSeq)}>
                    <Button size="small" danger>
                      삭제
                    </Button>
                  </Popconfirm>
                </Space>
              ),
            },
          ]}
        />
      </Card>
      <Modal
        title={editing ? '차량 수정' : '차량 등록'}
        open={modalOpen}
        width={640}
        onCancel={() => setModalOpen(false)}
        onOk={() => modalForm.submit()}
        confirmLoading={save.isPending}
      >
        <Form form={modalForm} layout="vertical" onFinish={(v) => save.mutate(v)}>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="차량명" name="vehicleName" rules={[{ required: true }]} style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="모델" name="model" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="차량번호" name="vehicleNum" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="센터" name="centerSeq" rules={[{ required: true }]} style={{ flex: 1 }}>
              <Select
                allowClear
                showSearch
                optionFilterProp="label"
                placeholder="센터"
                options={(centers ?? []).map((c) => ({ value: c.centerSeq, label: c.centerName }))}
              />
            </Form.Item>
            <Form.Item label="기사" name="driverSeq" style={{ flex: 1 }}>
              <Select
                allowClear
                showSearch
                optionFilterProp="label"
                placeholder="기사"
                options={(drivers ?? []).map((d) => ({ value: d.driverSeq, label: d.driverName }))}
              />
            </Form.Item>
            <Form.Item label="권역" name="regionSeq" style={{ flex: 1 }}>
              <Select
                allowClear
                showSearch
                optionFilterProp="label"
                placeholder="권역"
                options={(regions ?? []).map((r) => ({ value: r.regionSeq, label: r.regionName }))}
              />
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="차량유형" name="vehicleTypeCd" style={{ flex: 1 }}>
              <CodeSelect group="VehicleType" placeholder="차량유형" />
            </Form.Item>
            <Form.Item label="연료" name="fuelTypeCd" style={{ flex: 1 }}>
              <CodeSelect group="FuelType" placeholder="연료" />
            </Form.Item>
            <Form.Item label="사용구분" name="vehicleUseTypeCd" style={{ flex: 1 }}>
              <CodeSelect group="VehicleUseType" placeholder="사용구분" />
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="등록번호" name="vehicleRegNum" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="연식" name="modelYear" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="소유자" name="ownerName" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="적재용량" name="maxLoadingCapacity" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="연비" name="fuelEfficiency" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="사용여부" name="usageYn" style={{ flex: 1 }}>
              <Input placeholder="Y/N" />
            </Form.Item>
          </Space>
          <Form.Item label="차고지" name="garageName">
            <Input />
          </Form.Item>
          <Form.Item label="톤수" name="tonGrade" hidden>
            <InputNumber />
          </Form.Item>
        </Form>
      </Modal>
    </Space>
  );
}
