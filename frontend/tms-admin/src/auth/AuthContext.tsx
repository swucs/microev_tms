import { createContext, useCallback, useContext, useMemo, useState, type ReactNode } from 'react';
import { loginApi } from '../api/login';
import { tokenStore } from '../api/client';

interface AuthUser {
  email: string;
  adminName: string;
}

interface AuthContextValue {
  user: AuthUser | null;
  login: (email: string, password: string) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const email = tokenStore.getEmail();
    return email && tokenStore.getAccess() ? { email, adminName: '' } : null;
  });

  const login = useCallback(async (email: string, password: string) => {
    const res = await loginApi.login(email, password);
    tokenStore.save(res.accessToken, res.refreshToken, res.email);
    setUser({ email: res.email, adminName: res.adminName });
  }, []);

  const logout = useCallback(() => {
    tokenStore.clear();
    setUser(null);
  }, []);

  const value = useMemo(() => ({ user, login, logout }), [user, login, logout]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('AuthProvider 필요');
  return ctx;
}
