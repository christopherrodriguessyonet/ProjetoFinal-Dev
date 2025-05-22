import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, CssBaseline } from '@mui/material';
import { createTheme } from '@mui/material/styles';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Login from './pages/Login';
import Home from './pages/Home';
import MinhasTarefas from './pages/MinhasTarefas';
import Dashboard from './pages/Dashboard';
import TaskForm from './pages/TaskForm';
import CadastrarUsuario from './pages/CadastrarUsuario';
import ListaUsuarios from './pages/ListaUsuarios';
import MainLayout from './layouts/MainLayout';
import EditarUsuario from './pages/EditarUsuario';

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: { main: '#1976d2' },
    secondary: { main: '#dc004e' },
  },
});

interface ProtectedRouteProps {
  children: React.ReactNode;
  requireAdmin?: boolean;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requireAdmin }) => {
  const { isAuthenticated, isAdmin } = useAuth();

  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (requireAdmin && !isAdmin) return <Navigate to="/home" replace />;

  return <MainLayout>{children}</MainLayout>;
};

const AppRoutes = () => {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={!isAuthenticated ? <Login /> : <Navigate to="/home" replace />} />
      <Route path="/home" element={<ProtectedRoute><Home /></ProtectedRoute>} />
      <Route path="/minhas-tarefas" element={<ProtectedRoute><MinhasTarefas /></ProtectedRoute>} />
      <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
      <Route path="/nova-tarefa" element={<ProtectedRoute><TaskForm /></ProtectedRoute>} />
      <Route path="/editar-tarefa/:id" element={<ProtectedRoute><TaskForm /></ProtectedRoute>} />
      <Route path="/cadastrar-usuario" element={<ProtectedRoute requireAdmin><CadastrarUsuario /></ProtectedRoute>} />
      <Route path="/lista-usuarios" element={<ProtectedRoute requireAdmin><ListaUsuarios /></ProtectedRoute>} />
      <Route path="/" element={<Navigate to="/login" replace />} />
      <Route path="/editar-usuario/:id" element={<ProtectedRoute requireAdmin><EditarUsuario /></ProtectedRoute>} />
    </Routes>
  );
};

const App: React.FC = () => {
  return (
    <BrowserRouter>
      <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
          <AppRoutes />
        </AuthProvider>
      </ThemeProvider>
    </BrowserRouter>
  );
};

export default App;
