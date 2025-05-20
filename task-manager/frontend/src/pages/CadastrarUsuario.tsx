import React, { useState } from 'react';
import { Box, Button, TextField, Typography, Paper, MenuItem, Alert } from '@mui/material';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';

const roles = [
  { value: 'USER', label: 'Usuário' },
  { value: 'ADMIN', label: 'Administrador' }
];

const CadastrarUsuario: React.FC = () => {
  const [email, setEmail] = useState('');
  const [nome, setNome] = useState('');
  const [senha, setSenha] = useState('');
  const [role, setRole] = useState('USER');
  const [erro, setErro] = useState('');
  const [sucesso, setSucesso] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro('');
    setSucesso('');
    try {
      await api.post('/users', { email, nome, senha, role });
      setSucesso('Usuário cadastrado com sucesso!');
      setTimeout(() => navigate('/home'), 1500);
    } catch (err: any) {
      setErro(err.response?.data?.message || 'Erro ao cadastrar usuário');
    }
  };

  return (
    <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
      <Paper elevation={3} sx={{ p: 4, minWidth: 320 }}>
        <Typography variant="h5" mb={2}>Cadastrar Usuário</Typography>
        <form onSubmit={handleSubmit}>
          <TextField label="E-mail" fullWidth margin="normal" value={email} onChange={e => setEmail(e.target.value)} required />
          <TextField label="Nome" fullWidth margin="normal" value={nome} onChange={e => setNome(e.target.value)} required />
          <TextField label="Senha" type="password" fullWidth margin="normal" value={senha} onChange={e => setSenha(e.target.value)} required />
          <TextField select label="Tipo" fullWidth margin="normal" value={role} onChange={e => setRole(e.target.value)} required>
            {roles.map(option => (
              <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
            ))}
          </TextField>
          {erro && <Alert severity="error" sx={{ mt: 2 }}>{erro}</Alert>}
          {sucesso && <Alert severity="success" sx={{ mt: 2 }}>{sucesso}</Alert>}
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Cadastrar</Button>
        </form>
      </Paper>
    </Box>
  );
};

export default CadastrarUsuario;
