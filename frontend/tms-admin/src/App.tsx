import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { App as AntdApp } from 'antd';
import { AuthProvider } from './auth/AuthContext';
import PrivateRoute from './components/PrivateRoute';
import AppLayout from './components/AppLayout';
import LoginPage from './pages/LoginPage';
import DispatchPage from './pages/DispatchPage';
import CenterPage from './pages/CenterPage';
import VehiclePage from './pages/VehiclePage';
import DriverPage from './pages/DriverPage';
import RegionPage from './pages/RegionPage';
import AdminPage from './pages/AdminPage';
import CommonCodePage from './pages/CommonCodePage';

export default function App() {
  return (
    <AntdApp>
      <AuthProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/login" element={<LoginPage />} />
            <Route element={<PrivateRoute />}>
              <Route element={<AppLayout />}>
                <Route index element={<Navigate to="/dispatch" replace />} />
                <Route path="/dispatch" element={<DispatchPage />} />
                <Route path="/centers" element={<CenterPage />} />
                <Route path="/vehicles" element={<VehiclePage />} />
                <Route path="/drivers" element={<DriverPage />} />
                <Route path="/regions" element={<RegionPage />} />
                <Route path="/admins" element={<AdminPage />} />
                <Route path="/common-codes" element={<CommonCodePage />} />
              </Route>
            </Route>
          </Routes>
        </BrowserRouter>
      </AuthProvider>
    </AntdApp>
  );
}
