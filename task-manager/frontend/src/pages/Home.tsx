import React from 'react';
import { Grid, Card, CardContent, Typography, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const Home: React.FC = () => {
  const navigate = useNavigate();
  const { user, isAdmin, logout } = useAuth();

  const cards = [
    {
      title: '📋 Minhas Tarefas',
      description: 'Acesse todas suas tarefas',
      path: '/minhas-tarefas'
    },
    {
      title: '➕ Nova Tarefa',
      description: 'Crie uma nova tarefa',
      path: '/nova-tarefa'
    },
    ...(isAdmin ? [
      {
        title: '📊 Dashboard',
        description: 'Veja todas as tarefas do sistema',
        path: '/dashboard'
      },
      {
        title: '👤 Cadastrar Usuário',
        description: 'Crie um novo usuário',
        path: '/cadastrar-usuario'
      },
      {
        title: '👤 Usuários',
        description: 'Veja todos os usuários',
        path: '/lista-usuarios'
      }
    ] : [])
  ];

  return (
    <Box p={4}>
      <Typography variant="h4" gutterBottom>
        Bem-vindo, {user?.sub || 'usuário'}!
      </Typography>

      <Grid container spacing={3} mt={2}>
        {cards.map((card, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card sx={{ cursor: 'pointer', transition: '0.3s', '&:hover': { transform: 'scale(1.03)' } }} onClick={() => navigate(card.path)}>
              <CardContent>
                <Typography variant="h6" gutterBottom>{card.title}</Typography>
                <Typography variant="body2" color="text.secondary">{card.description}</Typography>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default Home;