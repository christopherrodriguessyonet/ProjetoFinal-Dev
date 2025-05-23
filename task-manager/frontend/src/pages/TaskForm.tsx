import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../services/api';
import {
  TextField,
  Button,
  Paper,
  Typography,
  Box,
  FormControlLabel,
  Checkbox,
  MenuItem,
  Alert,
} from '@mui/material';
import { useAuth } from '../contexts/AuthContext';

interface TaskFormData {
  titulo: string;
  descricao: string;
  status: string;
  responsavel: string;
  completo: boolean;
  dataEntrega: string;
}

const TaskForm: React.FC = () => {
  const navigate = useNavigate();
  const { id } = useParams();
  const { user } = useAuth();

  const [form, setForm] = useState<TaskFormData>({
    titulo: '',
    descricao: '',
    status: 'PENDENTE',
    responsavel: user?.sub || '',
    completo: false,
    dataEntrega: '',
  });

  const [usuarios, setUsuarios] = useState<string[]>([]);
  const [sucesso, setSucesso] = useState('');
  const [erro, setErro] = useState('');

  useEffect(() => {
    if (id) {
      api.get(`/tasks/${id}`).then(res => {
        setForm({
          ...res.data,
          dataEntrega: res.data.dataEntrega
            ? res.data.dataEntrega.substring(0, 16)
            : '',
        });
      });

      api.get('/users').then(res => {
        const emails = res.data.map((u: any) => u.email);
        setUsuarios(emails);
      });
    }
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro('');
    setSucesso('');

    if (form.descricao.trim().length < 10) {
      setErro('A descrição deve ter no mínimo 10 caracteres.');
      return;
    }

    if (form.titulo.trim().length < 3) {
      setErro('O título deve ter no mínimo 3 caracteres.');
      return;
    }

    try {
      if (id) {
        await api.put(`/tasks/${id}`, form);
        setSucesso('Tarefa atualizada com sucesso!');
      } else {
        await api.post('/tasks', form);
        setSucesso('Tarefa criada com sucesso!');
      }


      setTimeout(() => {
        if (user?.groups.includes('ADMIN')) {
          navigate('/home');
        } else {
          navigate('/minhas-tarefas');
        }
      }, 1500);
    } catch (err: any) {
      console.error('Erro ao salvar tarefa:', err);

      if (err.response?.status === 400 && typeof err.response.data === 'object') {
        const mensagens = Object.values(err.response.data);
        if (mensagens.length > 0) {
          setErro(mensagens.join('\n'));
        } else {
          setErro('Erro de validação. Verifique os campos.');
        }
      } else {
        setErro('Erro ao salvar a tarefa. Veja o console para mais detalhes.');
      }
    }
  };

  return (
    <Box sx={{ mt: 4 }}>
      <Paper sx={{ p: 4, maxWidth: 600, mx: 'auto' }}>
        <Typography variant="h5" gutterBottom>
          {id ? 'Editar Tarefa' : 'Nova Tarefa'}
        </Typography>

        {erro && <Alert severity="error" sx={{ mb: 2 }}>{erro}</Alert>}
        {sucesso && <Alert severity="success" sx={{ mb: 2 }}>{sucesso}</Alert>}

        <form onSubmit={handleSubmit}>
          <TextField
            label="Título"
            name="titulo"
            value={form.titulo}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
            inputProps={{ minLength: 3 }}
          />
          <TextField
            label="Descrição"
            name="descricao"
            value={form.descricao}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
            inputProps={{ minLength: 10 }}
          />
          <TextField
            select
            label="Status"
            name="status"
            value={form.status}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          >
            <MenuItem value="PENDENTE">PENDENTE</MenuItem>
            <MenuItem value="ANDAMENTO">ANDAMENTO</MenuItem>
            <MenuItem value="CONCLUIDO">CONCLUÍDO</MenuItem>
          </TextField>

          {id ? (
            <TextField
              select
              label="Responsável"
              name="responsavel"
              value={form.responsavel}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
            >
              {usuarios.map((email) => (
                <MenuItem key={email} value={email}>
                  {email}
                </MenuItem>
              ))}
            </TextField>
          ) : (
            <TextField
              label="Responsável"
              name="responsavel"
              type="email"
              value={form.responsavel}
              onChange={handleChange}
              fullWidth
              margin="normal"
              required
            />
          )}

          <TextField
            label="Data de Entrega"
            name="dataEntrega"
            type="datetime-local"
            value={form.dataEntrega}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          {id && (
            <FormControlLabel
              control={
                <Checkbox
                  checked={form.completo}
                  onChange={handleChange}
                  name="completo"
                />
              }
              label="Completo"
            />
          )}
          <Box
            sx={{ mt: 2, display: 'flex', justifyContent: 'space-between' }}
          >
            <Button variant="contained" color="primary" type="submit">
              Salvar
            </Button>
            <Button
              variant="outlined"
              color="secondary"
              onClick={() => navigate('/home')}
            >
              Cancelar
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default TaskForm;
