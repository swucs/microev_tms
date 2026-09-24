import { useState } from 'react';
import { App as AntdApp, Button, Card, Form, Input, Modal, Space, Table } from 'antd';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { adminApi, type SearchAdminsParams } from '../api/admin';
import type { AdminSummary } from '../api/types';
import CodeSelect from '../components/CodeSelect';
import { errMsg } from '../components/errorMsg';

export default function AdminPage() {
  const { message } = AntdApp.useApp();
  const qc = useQueryClient();
  const [modalForm] = Form.useForm();
  const [params, setParams] = useState<SearchAdminsParams>({});
  const [editing, setEditing] = useState<AdminSummary | null>(null);
  const [modalOpen, setModalOpen] = useState(false);

  const { data, isLoading } = useQuery({
    queryKey: ['admins', params],
    queryFn: () => adminApi.search(params),
  });

  const save = useMutation({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    mutationFn: async (values: any) => {
      if (editing) {
        // eslint-disable-next-line @typescript-eslint/no-explicit-any
        const body: any = {
          adminName: values.adminName,
          contact: values.contact,
          position: values.position,
          statusCd: values.statusCd,
        };
        if (values.password) body.password = values.password;
        await adminApi.modify(editing.adminSeq, body);
      } else {
        await adminApi.create(values);
      }
    },
    onSuccess: () => {
      message.success('저장됨');
      setModalOpen(false);
      qc.invalidateQueries({ queryKey: ['admins'] });
    },
    onError: (e) => message.error(errMsg(e, '저장 실패')),
  });

  const openCreate = () => {
    setEditing(null);
    modalForm.resetFields();
    setModalOpen(true);
  };
  const openEdit = (row: AdminSummary) => {
    setEditing(row);
    modalForm.setFieldsValue({ ...row, password: '' });
    setModalOpen(true);
  };

  return (
    <Space direction="vertical" style={{ width: '100%' }} size="middle">
      <Card>
        <Form layout="inline" onFinish={(v) => setParams(v as SearchAdminsParams)}>
          <Form.Item name="email">
            <Input placeholder="이메일" allowClear />
          </Form.Item>
          <Form.Item name="adminName">
            <Input placeholder="관리자명" allowClear />
          </Form.Item>
          <Form.Item name="statusCd" style={{ width: 160 }}>
            <CodeSelect group="AdminStatus" placeholder="상태" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              조회
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="관리자 목록" extra={<Button type="primary" onClick={openCreate}>등록</Button>}>
        <Table<AdminSummary>
          rowKey="adminSeq"
          loading={isLoading}
          dataSource={data}
          columns={[
            { title: '이메일', dataIndex: 'email' },
            { title: '이름', dataIndex: 'adminName' },
            { title: '연락처', dataIndex: 'contact' },
            { title: '직책', dataIndex: 'position' },
            { title: '상태', dataIndex: 'statusCdName' },
            { title: '최근접속', dataIndex: 'lastAccessedAt' },
            {
              title: '관리',
              render: (_, row) => (
                <Button size="small" onClick={() => openEdit(row)}>
                  수정
                </Button>
              ),
            },
          ]}
        />
      </Card>
      <Modal
        title={editing ? '관리자 수정' : '관리자 등록'}
        open={modalOpen}
        onCancel={() => setModalOpen(false)}
        onOk={() => modalForm.submit()}
        confirmLoading={save.isPending}
      >
        <Form form={modalForm} layout="vertical" onFinish={(v) => save.mutate(v)}>
          {!editing && (
            <Form.Item label="이메일" name="email" rules={[{ required: true }]}>
              <Input />
            </Form.Item>
          )}
          <Form.Item
            label={editing ? '비밀번호 (변경 시만 입력)' : '비밀번호'}
            name="password"
            rules={editing ? [] : [{ required: true }]}
          >
            <Input.Password />
          </Form.Item>
          <Form.Item label="이름" name="adminName" rules={[{ required: true }]}>
            <Input />
          </Form.Item>
          <Form.Item label="연락처" name="contact">
            <Input />
          </Form.Item>
          <Form.Item label="직책" name="position">
            <Input />
          </Form.Item>
          {editing && (
            <Form.Item label="상태" name="statusCd">
              <CodeSelect group="AdminStatus" placeholder="상태" />
            </Form.Item>
          )}
        </Form>
      </Modal>
    </Space>
  );
}
