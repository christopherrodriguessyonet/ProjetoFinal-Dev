import React from 'react';
import { Button, Box, Typography, Paper } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Home: React.FC = () => {
  const navigate = useNavigate();
  const { user, isAdmin, logout } = useAuth();

  const handleLogout = () => {
    logout();
    window.location.href = '/login';
  };

  return (
    <Box display="flex" flexDirection="column" alignItems="center" justifyContent="center" minHeight="100vh">
      <Paper elevation={3} sx={{ p: 4, minWidth: 320, mb: 2 }}>
        <Typography variant="h4" mb={2}>Bem-vindo, {user?.sub || 'Usuário'}!</Typography>

        <Button variant="contained" color="primary" fullWidth sx={{ mb: 2 }} onClick={() => navigate('/minhas-tarefas')}>Minhas Tarefas</Button>
        <Button variant="contained" color="secondary" fullWidth sx={{ mb: 2 }} onClick={() => navigate('/nova-tarefa')}>Adicionar Nova Tarefa</Button>

        {isAdmin && (
          <>
            <Button variant="contained" color="info" fullWidth sx={{ mb: 2 }} onClick={() => navigate('/dashboard')}>Dashboard</Button>
            <Button variant="contained" color="success" fullWidth sx={{ mb: 2 }} onClick={() => navigate('/cadastrar-usuario')}>Cadastrar Usuário</Button>
          </>
        )}

        <Button variant="outlined" color="primary" fullWidth sx={{ mb: 2 }} onClick={() => navigate('/home')}>Home</Button>
        <Button variant="outlined" color="error" fullWidth onClick={handleLogout}>Sair</Button>
      </Paper>
    </Box>
  );
};

export default Home;
