import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import api from '../services/api';
import { TextField, Button, Paper, Typography, Box, FormControlLabel, Checkbox } from '@mui/material';

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
  const [form, setForm] = useState<TaskFormData>({
    titulo: '',
    descricao: '',
    status: '',
    responsavel: '',
    completo: false,
    dataEntrega: '',
  });

  useEffect(() => {
    if (id) {
      api.get(`/tasks/${id}`).then(res => {
        setForm({ ...res.data, dataEntrega: res.data.dataEntrega ? res.data.dataEntrega.substring(0, 16) : '' });
      });
    }
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setForm(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (id) {
      await api.put(`/tasks/${id}`, form);
    } else {
      await api.post('/tasks', form);
    }
    navigate('/dashboard');
  };

  return (
    <Box sx={{ mt: 4 }}>
      <Paper sx={{ p: 4, maxWidth: 600, mx: 'auto' }}>
        <Typography variant="h5" gutterBottom>{id ? 'Editar Tarefa' : 'Nova Tarefa'}</Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            label="Título"
            name="titulo"
            value={form.titulo}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Descrição"
            name="descricao"
            value={form.descricao}
            onChange={handleChange}
            fullWidth
            margin="normal"
            required
          />
          <TextField
            label="Status"
            name="status"
            value={form.status}
            onChange={handleChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Responsável"
            name="responsavel"
            value={form.responsavel}
            onChange={handleChange}
            fullWidth
            margin="normal"
          />
          <TextField
            label="Data de Entrega"
            name="dataEntrega"
            type="datetime-local"
            value={form.dataEntrega}
            onChange={handleChange}
            fullWidth
            margin="normal"
          />
          {id && (
            <FormControlLabel
              control={<Checkbox checked={form.completo} onChange={handleChange} name="completo" />}
              label="Completo"
            />
          )}
          <Box sx={{ mt: 2, display: 'flex', justifyContent: 'space-between' }}>
            <Button variant="contained" color="primary" type="submit">
              Salvar
            </Button>
            <Button variant="outlined" color="secondary" onClick={() => navigate('/dashboard')}>
              Cancelar
            </Button>
          </Box>
        </form>
      </Paper>
    </Box>
  );
};

export default TaskForm;