import { useState } from 'react';
import { App as AntdApp, Button, Card, Form, Input, Modal, Popconfirm, Space, Table } from 'antd';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { centerApi } from '../api/center';
import { commonApi } from '../api/common';
import type { CenterSummary } from '../api/types';
import CodeSelect from '../components/CodeSelect';
import { errMsg } from '../components/errorMsg';

export default function CenterPage() {
  const { message } = AntdApp.useApp();
  const qc = useQueryClient();
  const [modalForm] = Form.useForm();
  const [params, setParams] = useState<Record<string, string>>({});
  const [editing, setEditing] = useState<CenterSummary | null>(null);
  const [modalOpen, setModalOpen] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['centers', params],
    queryFn: () => centerApi.search(params),
  });

  const invalidate = () => qc.invalidateQueries({ queryKey: ['centers'] });

  const save = useMutation({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    mutationFn: async (values: any) => {
      if (editing) await centerApi.modify(editing.centerSeq, values);
      else await centerApi.create(values);
    },
    onSuccess: () => {
      message.success('저장됨');
      setModalOpen(false);
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '저장 실패')),
  });

  const remove = useMutation({
    mutationFn: (seq: number) => centerApi.remove(seq),
    onSuccess: () => {
      message.success('삭제됨');
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '삭제 실패')),
  });

  const fillCoordinate = async () => {
    const addr = modalForm.getFieldValue('centerAddr1') as string;
    if (!addr) {
      message.warning('주소 입력 후 조회');
      return;
    }
    try {
      const { latitude, longitude } = await commonApi.coordinate(addr);
      modalForm.setFieldsValue({ latitude, longitude });
      message.success('좌표 조회됨');
    } catch (e) {
      message.error(errMsg(e, '좌표 조회 실패'));
    }
  };

  const openCreate = () => {
    setEditing(null);
    modalForm.resetFields();
    setModalOpen(true);
  };
  const openEdit = (row: CenterSummary) => {
    setEditing(row);
    modalForm.setFieldsValue(row);
    setModalOpen(true);
  };

  return (
    <Space direction="vertical" style={{ width: '100%' }} size="middle">
      <Card>
        <Form layout="inline" onFinish={(v) => setParams(v as Record<string, string>)}>
          <Form.Item name="centerName">
            <Input placeholder="센터명" allowClear />
          </Form.Item>
          <Form.Item name="centerTypeCd" style={{ width: 160 }}>
            <CodeSelect group="CenterType" placeholder="센터유형" />
          </Form.Item>
          <Form.Item name="managerName">
            <Input placeholder="담당자" allowClear />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              조회
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="센터 목록" extra={<Button type="primary" onClick={openCreate}>등록</Button>}>
        <Table<CenterSummary>
          rowKey="centerSeq"
          loading={isLoading}
          dataSource={data}
          columns={[
            { title: '센터명', dataIndex: 'centerName' },
            { title: '유형', dataIndex: 'centerTypeCdName' },
            { title: '주소', dataIndex: 'centerAddr1' },
            { title: '담당자', dataIndex: 'managerName' },
            { title: '연락처', dataIndex: 'contactNum' },
            { title: '차량수', dataIndex: 'vehicleCount' },
            { title: '사용', dataIndex: 'usageYn' },
            {
              title: '관리',
              render: (_, row) => (
                <Space>
                  <Button size="small" onClick={() => openEdit(row)}>
                    수정
                  </Button>
                  <Popconfirm title="삭제할까?" onConfirm={() => remove.mutate(row.centerSeq)}>
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
        title={editing ? '센터 수정' : '센터 등록'}
        open={modalOpen}
        width={640}
        onCancel={() => setModalOpen(false)}
        onOk={() => modalForm.submit()}
        confirmLoading={save.isPending}
      >
        <Form form={modalForm} layout="vertical" onFinish={(v) => save.mutate(v)}>
          <Form.Item label="센터명" name="centerName" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item label="센터유형" name="centerTypeCd" rules={[{ required: true }]}>
            <CodeSelect group="CenterType" placeholder="센터유형" />
          </Form.Item>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="우편번호" name="zipCode" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="담당자" name="managerName" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="연락처" name="contactNum" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
          </Space>
          <Form.Item label="주소" name="centerAddr1">
            <Input />
          </Form.Item>
          <Form.Item label="상세주소" name="centerAddr2">
            <Input />
          </Form.Item>
          <Space style={{ width: '100%' }} align="end" size="middle">
            <Form.Item label="위도" name="latitude" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="경도" name="longitude" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item>
              <Button onClick={fillCoordinate}>좌표 조회</Button>
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="면적" name="totalArea" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="사용여부" name="usageYn" style={{ flex: 1 }}>
              <Input placeholder="Y/N" />
            </Form.Item>
          </Space>
        </Form>
      </Modal>
    </Space>
  );
}
