import React, { useState, useEffect } from 'react';
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
  TextField,
  IconButton,
  Stack,
} from '@mui/material';
import { DatePicker, LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import dayjs, { Dayjs } from 'dayjs';
import 'dayjs/locale/pt-br';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';
import { Snackbar } from '@mui/material';

dayjs.locale('pt-br');

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
  const [dataInicial, setDataInicial] = useState<Dayjs | null>(null);
  const [dataFinal, setDataFinal] = useState<Dayjs | null>(null);
  const [usuarioFilter, setUsuarioFilter] = useState<string>('');
  const navigate = useNavigate();
  const { isAdmin } = useAuth();
  const [sucesso, setSucesso] = useState('');

  useEffect(() => {
    const loadAll = async () => {
      try {
        const response = await api.get('/tasks');
        setTasks(response.data);
      } catch (error) {
        console.error('Erro ao carregar tarefas iniciais:', error);
      }
    };
    loadAll();
  }, []);

  const fetchTasks = async () => {
    try {
      let url = '/tasks';
      const params = new URLSearchParams();

      if (statusFilter) params.append('status', statusFilter);
      if (dataInicial) params.append('dataInicial', dataInicial.format('YYYY-MM-DD'));
      if (dataFinal) params.append('dataFinal', dataFinal.format('YYYY-MM-DD'));
      if (isAdmin && usuarioFilter) params.append('usuario', usuarioFilter);

      if (params.toString() !== '') {
        url = `/tasks/filtro?${params.toString()}`;
      }

      const response = await api.get(url);
      setTasks(response.data);
    } catch (error) {
      console.error('Erro ao buscar tarefas:', error);
    }
  };

  const handleClearFilters = async () => {
    setStatusFilter('');
    setDataInicial(null);
    setDataFinal(null);
    setUsuarioFilter('');
    try {
      const response = await api.get('/tasks');
      setTasks(response.data);
    } catch (error) {
      console.error('Erro ao limpar filtros:', error);
    }
  };

  const handleDelete = async (id: number) => {
    await api.delete(`/tasks/${id}`);
    fetchTasks();
    setSucesso('Tarefa excluída com sucesso');
  };

  return (
    <Box p={4}>
      <Typography variant="h4" gutterBottom>
        Tarefas da Equipe
      </Typography>

      <LocalizationProvider dateAdapter={AdapterDayjs}>
        <Stack direction="row" spacing={2} mb={2} alignItems="center">
          <Box sx={{ width: '200px' }}>
            <FormControl fullWidth>
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
          </Box>

          <Box sx={{ width: '200px' }}>
            <DatePicker
              label="Data Inicial"
              value={dataInicial}
              onChange={(newValue) => setDataInicial(newValue as Dayjs | null)}
              inputFormat="DD/MM/YYYY"
              renderInput={(params) => <TextField {...params} fullWidth />}
            />
          </Box>

          <Box sx={{ width: '200px' }}>
            <DatePicker
              label="Data Final"
              value={dataFinal}
              onChange={(newValue) => setDataFinal(newValue as Dayjs | null)}
              inputFormat="DD/MM/YYYY"
              renderInput={(params) => <TextField {...params} fullWidth />}
            />
          </Box>

          {isAdmin && (
            <Box sx={{ width: '250px' }}>
              <TextField
                label="Filtrar por usuário"
                value={usuarioFilter}
                onChange={(e) => setUsuarioFilter(e.target.value)}
                fullWidth
              />
            </Box>
          )}

          <IconButton
            color="primary"
            onClick={fetchTasks}
            sx={{ border: '1px solid #1976d2', borderRadius: 1 }}
          >
            <SearchIcon />
          </IconButton>

          <Button
            variant="outlined"
            color="secondary"
            onClick={handleClearFilters}
            startIcon={<ClearIcon />}
          >
            Limpar
          </Button>
        </Stack>
      </LocalizationProvider>

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
            {tasks.length === 0 ? (
              <TableRow>
                <TableCell colSpan={7} align="center">
                  Nenhuma tarefa encontrada.
                </TableCell>
              </TableRow>
            ) : (
              tasks.map((task) => (
                <TableRow key={task.id}>
                  <TableCell>{task.titulo}</TableCell>
                  <TableCell>{task.descricao}</TableCell>
                  <TableCell>{task.status}</TableCell>
                  <TableCell>{task.responsavel}</TableCell>
                  <TableCell>{task.completo ? 'Sim' : 'Não'}</TableCell>
                  <TableCell>
                    {task.dataEntrega
                      ? dayjs(task.dataEntrega).format('DD/MM/YYYY HH:mm')
                      : '-'}
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
              ))
            )}
          </TableBody>
        </Table>
      </TableContainer>
      <Snackbar
        open={!!sucesso}
        autoHideDuration={3000}
        onClose={() => setSucesso('')}
        message={sucesso}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      />
    </Box>
  );
};

export default Dashboard;