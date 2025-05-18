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

const theme = createTheme({
  palette: {
    mode: 'light',
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

interface ProtectedRouteProps {
  children: React.ReactNode;
  requireAdmin?: boolean;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, requireAdmin }) => {
  const { isAuthenticated, isAdmin } = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requireAdmin && !isAdmin) {
    return <Navigate to="/dashboard" replace />;
  }

  return <>{children}</>;
};

const AppRoutes = () => {
  const { isAuthenticated } = useAuth();
  return (
    <Routes>
      <Route path="/login" element={!isAuthenticated ? <Login /> : <Navigate to="/home" replace />} />
      <Route path="/home" element={isAuthenticated ? <Home /> : <Navigate to="/login" replace />} />
      <Route path="/minhas-tarefas" element={isAuthenticated ? <MinhasTarefas /> : <Navigate to="/login" replace />} />
      <Route path="/dashboard" element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" replace />} />
      <Route path="/nova-tarefa" element={isAuthenticated ? <TaskForm /> : <Navigate to="/login" replace />} />
      <Route path="/editar-tarefa/:id" element={isAuthenticated ? <TaskForm /> : <Navigate to="/login" replace />} />
      <Route path="/cadastrar-usuario" element={<CadastrarUsuario />} />
      <Route path="/" element={<Navigate to="/login" replace />} />
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