import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { Button, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';

interface Task {
  id: number;
  titulo: string;
  descricao: string;
  status: string;
  responsavel: string;
  completo: boolean;
  dataEntrega: string;
}

const Dashboard: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const navigate = useNavigate();

  const fetchTasks = async () => {
    try {
      const response = await api.get('/tasks/minhas');
      console.log('✅ RESPOSTA CORRETA:', response.data);
      setTasks(response.data);
    } catch (error: any) {
      console.error('❌ ERRO AO BUSCAR TAREFAS');

      if (error.response) {
        console.error('📄 STATUS:', error.response.status);
        console.error('📄 HEADERS:', error.response.headers);
        console.error('📄 DATA:', error.response.data);
      } else {
        console.error(error.message);
      }

      // Mostra alerta amigável
      alert("Erro ao carregar tarefas. Veja o console para detalhes.");
    }
  };


  useEffect(() => {
    fetchTasks();
  }, []);



  useEffect(() => {
    fetchTasks();
  }, []);

  const handleDelete = async (id: number) => {
    await api.delete(`/tasks/${id}`);
    fetchTasks();
  };

  return (
    <Box p={4}>
      <Button variant="outlined" color="primary" sx={{ mb: 2 }} onClick={() => navigate('/home')}>Home</Button>
      <Typography variant="h4" gutterBottom>Minhas Tarefas</Typography>
      <Button variant="contained" color="primary" onClick={() => navigate('/nova-tarefa')} sx={{ mb: 2 }}>
        Nova Tarefa
      </Button>
      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Título</TableCell>
              <TableCell>Descrição</TableCell>
              <TableCell>Status</TableCell>
              <TableCell>Responsável</TableCell>
              <TableCell>Completo</TableCell>
              <TableCell>Data de Entrega</TableCell>
              <TableCell>Ações</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {tasks.map((task) => (
              <TableRow key={task.id}>
                <TableCell>{task.titulo}</TableCell>
                <TableCell>{task.descricao}</TableCell>
                <TableCell>{task.status}</TableCell>
                <TableCell>{task.responsavel}</TableCell>
                <TableCell>{task.completo ? 'Sim' : 'Não'}</TableCell>
                <TableCell>{task.dataEntrega ? new Date(task.dataEntrega).toLocaleString() : '-'}</TableCell>
                <TableCell>
                  <Button variant="outlined" color="primary" size="small" onClick={() => navigate(`/editar-tarefa/${task.id}`)} sx={{ mr: 1 }}>
                    Editar
                  </Button>
                  <Button variant="outlined" color="error" size="small" onClick={() => handleDelete(task.id)}>
                    Excluir
                  </Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default Dashboard; 