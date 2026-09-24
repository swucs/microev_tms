import { Layout, Menu, Button, theme } from 'antd';
import { Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const { Header, Sider, Content } = Layout;

const MENU = [
  { key: '/dispatch', label: '배차계획' },
  { key: '/centers', label: '센터관리' },
  { key: '/vehicles', label: '차량관리' },
  { key: '/drivers', label: '기사관리' },
  { key: '/regions', label: '권역관리' },
  { key: '/admins', label: '관리자관리' },
  { key: '/common-codes', label: '공통코드' },
];

export default function AppLayout() {
  const { user, logout } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();
  const { token } = theme.useToken();

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider breakpoint="lg" collapsedWidth="0">
        <div style={{ color: '#fff', padding: 16, fontWeight: 700 }}>TMS 관리자</div>
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[location.pathname]}
          items={MENU}
          onClick={({ key }) => navigate(key)}
        />
      </Sider>
      <Layout>
        <Header
          style={{
            background: token.colorBgContainer,
            display: 'flex',
            justifyContent: 'flex-end',
            alignItems: 'center',
            gap: 12,
          }}
        >
          <span>{user?.adminName || user?.email}</span>
          <Button
            onClick={() => {
              logout();
              navigate('/login');
            }}
          >
            로그아웃
          </Button>
        </Header>
        <Content style={{ margin: 16 }}>
          <Outlet />
        </Content>
      </Layout>
    </Layout>
  );
}
