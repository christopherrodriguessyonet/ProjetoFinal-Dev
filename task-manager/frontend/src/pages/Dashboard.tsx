import React, { useEffect, useState } from 'react';
import {
  Box,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Typography,
  MenuItem,
  Select,
  FormControl,
  InputLabel,
} from '@mui/material';
import api from '../services/api';
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
  const [statusFilter, setStatusFilter] = useState<string>('');
  const navigate = useNavigate();

  const fetchTasks = async () => {
    try {
      const url = statusFilter
        ? `/tasks/filtro?status=${statusFilter}`
        : `/tasks`;

      const response = await api.get(url);
      setTasks(response.data);
    } catch (error) {
      console.error('Erro ao buscar tarefas:', error);
    }
  };

  useEffect(() => {
    fetchTasks();
  }, [statusFilter]);

  const handleDelete = async (id: number) => {
    await api.delete(`/tasks/${id}`);
    fetchTasks();
  };

  return (
    <Box p={4}>
      <Button variant="outlined" sx={{ mb: 2 }} onClick={() => navigate('/home')}>
        Home
      </Button>

      <Typography variant="h4" gutterBottom>
        Tarefas da Equipe
      </Typography>

      <FormControl sx={{ mb: 2, minWidth: 200 }}>
        <InputLabel>Status</InputLabel>
        <Select
          value={statusFilter}
          label="Status"
          onChange={(e) => setStatusFilter(e.target.value)}
        >
          <MenuItem value="">Todos</MenuItem>
          <MenuItem value="PENDENTE">Pendente</MenuItem>
          <MenuItem value="ANDAMENTO">Andamento</MenuItem>
          <MenuItem value="CONCLUIDO">Concluído</MenuItem>
        </Select>
      </FormControl>

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
                <TableCell>
                  {task.dataEntrega ? new Date(task.dataEntrega).toLocaleString() : '-'}
                </TableCell>
                <TableCell>
                  <Button
                    variant="outlined"
                    color="primary"
                    size="small"
                    onClick={() => navigate(`/editar-tarefa/${task.id}`)}
                    sx={{ mr: 1 }}
                  >
                    Editar
                  </Button>
                  <Button
                    variant="outlined"
                    color="error"
                    size="small"
                    onClick={() => handleDelete(task.id)}
                  >
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
