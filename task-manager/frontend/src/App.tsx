import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { ThemeProvider, CssBaseline, createTheme } from '@mui/material';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Login from './pages/Login';
import Home from './pages/Home';
import MinhasTarefas from './pages/MinhasTarefas';
import Dashboard from './pages/Dashboard';
import TaskForm from './pages/TaskForm';
import CadastrarUsuario from './pages/CadastrarUsuario';
import MainLayout from './layouts/MainLayout';

const theme = createTheme({ palette: { mode: 'light' } });

const ProtectedRoute = ({ children, requireAdmin = false }: any) => {
  const { isAuthenticated, isAdmin } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (requireAdmin && !isAdmin) return <Navigate to="/home" replace />;
  return <>{children}</>;
};

const AppRoutes = () => (
  <Routes>
    <Route path="/login" element={<Login />} />
    <Route path="/home" element={<ProtectedRoute><MainLayout><Home /></MainLayout></ProtectedRoute>} />
    <Route path="/minhas-tarefas" element={<ProtectedRoute><MainLayout><MinhasTarefas /></MainLayout></ProtectedRoute>} />
    <Route path="/nova-tarefa" element={<ProtectedRoute><MainLayout><TaskForm /></MainLayout></ProtectedRoute>} />
    <Route path="/editar-tarefa/:id" element={<ProtectedRoute><MainLayout><TaskForm /></MainLayout></ProtectedRoute>} />
    <Route path="/dashboard" element={<ProtectedRoute requireAdmin><MainLayout><Dashboard /></MainLayout></ProtectedRoute>} />
    <Route path="/cadastrar-usuario" element={<ProtectedRoute requireAdmin><MainLayout><CadastrarUsuario /></MainLayout></ProtectedRoute>} />
    <Route path="/" element={<Navigate to="/login" replace />} />
  </Routes>
);

const App: React.FC = () => (
  <BrowserRouter>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </ThemeProvider>
  </BrowserRouter>
);

export default App;
