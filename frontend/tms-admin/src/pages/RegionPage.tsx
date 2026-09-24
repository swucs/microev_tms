import { useState } from 'react';
import { App as AntdApp, Button, Card, Form, Input, Modal, Popconfirm, Space, Table } from 'antd';
import { MinusCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { regionApi } from '../api/region';
import type { RegionSummary } from '../api/types';
import { errMsg } from '../components/errorMsg';

export default function RegionPage() {
  const { message } = AntdApp.useApp();
  const qc = useQueryClient();
  const [modalForm] = Form.useForm();
  const [params, setParams] = useState<Record<string, string>>({});
  const [editing, setEditing] = useState<RegionSummary | null>(null);
  const [modalOpen, setModalOpen] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['regions', params],
    queryFn: () => regionApi.search(params),
  });

  const invalidate = () => qc.invalidateQueries({ queryKey: ['regions'] });

  const save = useMutation({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    mutationFn: async (values: any) => {
      if (editing) await regionApi.modify(editing.regionSeq, values);
      else await regionApi.create(values);
    },
    onSuccess: () => {
      message.success('저장됨');
      setModalOpen(false);
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '저장 실패')),
  });

  const remove = useMutation({
    mutationFn: (seq: number) => regionApi.remove(seq),
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
  const openEdit = async (row: RegionSummary) => {
    setEditing(row);
    try {
      const detail = (await regionApi.detail(row.regionSeq)) as {
        regionName: string;
        eupMyeonDongs: string[];
      };
      modalForm.setFieldsValue(detail);
    } catch {
      modalForm.setFieldsValue({ regionName: row.regionName, eupMyeonDongs: [] });
    }
    setModalOpen(true);
  };

  return (
    <Space direction="vertical" style={{ width: '100%' }} size="middle">
      <Card>
        <Form layout="inline" onFinish={(v) => setParams(v as Record<string, string>)}>
          <Form.Item name="regionName">
            <Input placeholder="권역명" allowClear />
          </Form.Item>
          <Form.Item name="vehicleName">
            <Input placeholder="차량명" allowClear />
          </Form.Item>
          <Form.Item name="eupMyeonDong">
            <Input placeholder="읍면동" allowClear />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              조회
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="권역 목록" extra={<Button type="primary" onClick={openCreate}>등록</Button>}>
        <Table<RegionSummary>
          rowKey="regionSeq"
          loading={isLoading}
          dataSource={data}
          columns={[
            { title: '권역명', dataIndex: 'regionName' },
            { title: '읍면동', dataIndex: 'eupMyeonDong' },
            { title: '차량', dataIndex: 'vehicleName' },
            { title: '등록자', dataIndex: 'creatorName' },
            { title: '등록일', dataIndex: 'createdAt' },
            {
              title: '관리',
              render: (_, row) => (
                <Space>
                  <Button size="small" onClick={() => openEdit(row)}>
                    수정
                  </Button>
                  <Popconfirm title="삭제할까?" onConfirm={() => remove.mutate(row.regionSeq)}>
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
        title={editing ? '권역 수정' : '권역 등록'}
        open={modalOpen}
        onCancel={() => setModalOpen(false)}
        onOk={() => modalForm.submit()}
        confirmLoading={save.isPending}
      >
        <Form form={modalForm} layout="vertical" onFinish={(v) => save.mutate(v)}>
          <Form.Item label="권역명" name="regionName" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.List name="eupMyeonDongs">
            {(fields, { add, remove: removeField }) => (
              <div style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                {fields.map((field) => (
                  <Space key={field.key} align="baseline">
                    <Form.Item {...field} noStyle rules={[{ required: true }]}>
                      <Input placeholder="읍면동" style={{ width: 300 }} />
                    </Form.Item>
                    <MinusCircleOutlined onClick={() => removeField(field.name)} />
                  </Space>
                ))}
                <Form.Item>
                  <Button type="dashed" onClick={() => add()} icon={<PlusOutlined />}>
                    읍면동 추가
                  </Button>
                </Form.Item>
              </div>
            )}
          </Form.List>
        </Form>
      </Modal>
    </Space>
  );
}
