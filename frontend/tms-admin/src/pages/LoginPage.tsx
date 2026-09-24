import { useState } from 'react';
import { Button, Card, Form, Input, Typography, App as AntdApp } from 'antd';
import { useNavigate } from 'react-router-dom';
import { AxiosError } from 'axios';
import { useAuth } from '../auth/AuthContext';

export default function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const { message } = AntdApp.useApp();
  const [loading, setLoading] = useState(false);

  const onFinish = async (values: { email: string; password: string }) => {
    setLoading(true);
    try {
      await login(values.email, values.password);
      navigate('/', { replace: true });
    } catch (e) {
      const msg =
        e instanceof AxiosError ? e.response?.data?.resultMessage ?? '로그인 실패' : '로그인 실패';
      message.error(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ display: 'flex', justifyContent: 'center', marginTop: 120 }}>
      <Card title="TMS 관리자 로그인" style={{ width: 360 }}>
        <Form layout="vertical" onFinish={onFinish} autoComplete="off">
          <Form.Item label="이메일" name="email" rules={[{ required: true, message: '이메일 입력' }]}>
            <Input placeholder="admin@obigo.com" />
          </Form.Item>
          <Form.Item label="비밀번호" name="password" rules={[{ required: true, message: '비밀번호 입력' }]}>
            <Input.Password />
          </Form.Item>
          <Button type="primary" htmlType="submit" block loading={loading}>
            로그인
          </Button>
        </Form>
        <Typography.Paragraph type="secondary" style={{ marginTop: 12, marginBottom: 0 }}>
          로컬 테스트: admin@obigo.com / 1234
        </Typography.Paragraph>
      </Card>
    </div>
  );
}
