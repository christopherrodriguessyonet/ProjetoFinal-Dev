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

const MinhasTarefas: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const navigate = useNavigate();
  const user = JSON.parse(localStorage.getItem('user') || '{}');

  const fetchTasks = async () => {
    try {
      const response = await api.get('/tasks/minhas');
      setTasks(response.data);
    } catch (error: any) {
      console.error("Erro ao buscar tarefas:", error);
      if (error.response?.data) {
        console.error("Detalhes do erro:", error.response.data);
      }
    }
  };


  useEffect(() => {
    fetchTasks();
  }, []);

  const handleDelete = async (id: number) => {
    await api.delete(`/tasks/${id}`);
    fetchTasks();
  };

  return (
    <Box p={4}>
      <Typography variant="h5" mb={2}>Minhas Tarefas</Typography>
      <Button variant="contained" color="primary" sx={{ mb: 2 }} onClick={() => navigate('/nova-tarefa')}>Adicionar Nova Tarefa</Button>
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
                <TableCell>{task.dataEntrega ? new Date(task.dataEntrega).toLocaleDateString() : '-'}</TableCell>
                <TableCell>
                  <Button variant="outlined" color="primary" size="small" onClick={() => navigate(`/editar-tarefa/${task.id}`)}>Editar</Button>
                  <Button variant="outlined" color="error" size="small" sx={{ ml: 1 }} onClick={() => handleDelete(task.id)}>Excluir</Button>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
};

export default MinhasTarefas; 