import { useState } from 'react';
import { App as AntdApp, Button, Card, Form, Input, Modal, Popconfirm, Select, Space, Table } from 'antd';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { driverApi } from '../api/driver';
import { centerApi } from '../api/center';
import type { DriverSummary } from '../api/types';
import CodeSelect from '../components/CodeSelect';
import { errMsg } from '../components/errorMsg';

export default function DriverPage() {
  const { message } = AntdApp.useApp();
  const qc = useQueryClient();
  const [modalForm] = Form.useForm();
  const [pwForm] = Form.useForm();
  const [params, setParams] = useState<Record<string, string>>({});
  const [editing, setEditing] = useState<DriverSummary | null>(null);
  const [modalOpen, setModalOpen] = useState(false);
  const [pwTarget, setPwTarget] = useState<DriverSummary | null>(null);

  const { data, isLoading } = useQuery({
    queryKey: ['drivers', params],
    queryFn: () => driverApi.search(params),
  });
  const { data: centers } = useQuery({ queryKey: ['centers-all'], queryFn: () => centerApi.search({}) });

  const invalidate = () => qc.invalidateQueries({ queryKey: ['drivers'] });

  const save = useMutation({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    mutationFn: async (values: any) => {
      if (editing) await driverApi.modify(editing.driverSeq, values);
      else await driverApi.create(values);
    },
    onSuccess: () => {
      message.success('저장됨');
      setModalOpen(false);
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '저장 실패')),
  });

  const remove = useMutation({
    mutationFn: (seq: number) => driverApi.remove(seq),
    onSuccess: () => {
      message.success('삭제됨');
      invalidate();
    },
    onError: (e) => message.error(errMsg(e, '삭제 실패')),
  });

  const changePw = useMutation({
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    mutationFn: (values: any) => driverApi.modifyPassword(pwTarget!.driverSeq, values.password),
    onSuccess: () => {
      message.success('비밀번호 변경됨');
      setPwTarget(null);
      pwForm.resetFields();
    },
    onError: (e) => message.error(errMsg(e, '변경 실패')),
  });

  const openCreate = () => {
    setEditing(null);
    modalForm.resetFields();
    setModalOpen(true);
  };
  const openEdit = (row: DriverSummary) => {
    setEditing(row);
    modalForm.setFieldsValue(row);
    setModalOpen(true);
  };

  return (
    <Space direction="vertical" style={{ width: '100%' }} size="middle">
      <Card>
        <Form layout="inline" onFinish={(v) => setParams(v as Record<string, string>)}>
          <Form.Item name="driverName">
            <Input placeholder="기사명" allowClear />
          </Form.Item>
          <Form.Item name="centerName">
            <Input placeholder="센터명" allowClear />
          </Form.Item>
          <Form.Item name="statusCd" style={{ width: 160 }}>
            <CodeSelect group="DriverStatus" placeholder="상태" />
          </Form.Item>
          <Form.Item>
            <Button type="primary" htmlType="submit">
              조회
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="기사 목록" extra={<Button type="primary" onClick={openCreate}>등록</Button>}>
        <Table<DriverSummary>
          rowKey="driverSeq"
          loading={isLoading}
          dataSource={data}
          columns={[
            { title: '이름', dataIndex: 'driverName' },
            { title: '센터', dataIndex: 'centerName' },
            { title: '로그인ID', dataIndex: 'loginId' },
            { title: '연락처', dataIndex: 'driverPhoneNum' },
            { title: '상태', dataIndex: 'statusCdName' },
            {
              title: '관리',
              render: (_, row) => (
                <Space>
                  <Button size="small" onClick={() => openEdit(row)}>
                    수정
                  </Button>
                  <Button size="small" onClick={() => setPwTarget(row)}>
                    비번
                  </Button>
                  <Popconfirm title="삭제할까?" onConfirm={() => remove.mutate(row.driverSeq)}>
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
        title={editing ? '기사 수정' : '기사 등록'}
        open={modalOpen}
        width={640}
        onCancel={() => setModalOpen(false)}
        onOk={() => modalForm.submit()}
        confirmLoading={save.isPending}
      >
        <Form form={modalForm} layout="vertical" onFinish={(v) => save.mutate(v)}>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="이름" name="driverName" rules={[{ required: true }]} style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="센터" name="centerSeq" rules={[{ required: true }]} style={{ flex: 1 }}>
              <Select
                allowClear
                showSearch
                optionFilterProp="label"
                placeholder="센터"
                options={(centers ?? []).map((c) => ({ value: c.centerSeq, label: c.centerName }))}
              />
            </Form.Item>
            <Form.Item label="상태" name="statusCd" style={{ flex: 1 }}>
              <CodeSelect group="DriverStatus" placeholder="상태" />
            </Form.Item>
          </Space>
          {!editing && (
            <Space style={{ width: '100%' }} size="middle">
              <Form.Item label="로그인ID" name="loginId" rules={[{ required: true }]} style={{ flex: 1 }}>
                <Input />
              </Form.Item>
              <Form.Item label="비밀번호" name="password" rules={[{ required: true }]} style={{ flex: 1 }}>
                <Input.Password />
              </Form.Item>
            </Space>
          )}
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="연락처" name="driverPhoneNum" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="이메일" name="email" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="소속" name="compName" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="면허번호" name="driverLicenseNum" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="면허일자" name="driverLicenseDate" style={{ flex: 1 }}>
              <Input placeholder="YYYY-MM-DD" />
            </Form.Item>
            <Form.Item label="면허발급" name="driverLicenseAgency" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
          </Space>
          <Space style={{ width: '100%' }} size="middle">
            <Form.Item label="근무일" name="workingDays" style={{ flex: 1 }}>
              <Input />
            </Form.Item>
            <Form.Item label="시작" name="workingStartHour" style={{ flex: 1 }}>
              <Input placeholder="HH:mm" />
            </Form.Item>
            <Form.Item label="종료" name="workingEndHour" style={{ flex: 1 }}>
              <Input placeholder="HH:mm" />
            </Form.Item>
          </Space>
        </Form>
      </Modal>
      <Modal
        title={`비밀번호 변경 (${pwTarget?.driverName})`}
        open={!!pwTarget}
        onCancel={() => setPwTarget(null)}
        onOk={() => pwForm.submit()}
        confirmLoading={changePw.isPending}
      >
        <Form form={pwForm} layout="vertical" onFinish={(v) => changePw.mutate(v)}>
          <Form.Item label="새 비밀번호" name="password" rules={[{ required: true }]}>
            <Input.Password />
          </Form.Item>
        </Form>
      </Modal>
    </Space>
  );
}
