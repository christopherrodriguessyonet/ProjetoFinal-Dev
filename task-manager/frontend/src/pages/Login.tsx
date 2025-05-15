import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Box, Button, TextField, Typography, Paper } from '@mui/material';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            const res = await api.post('/auth/login', { email, password: senha });
            localStorage.setItem('token', res.data.token);
            setErro('');
            navigate('/home');
        } catch (err) {
            setErro('Usuário ou senha inválidos');
        }
    };

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
            <Paper elevation={3} sx={{ p: 4, minWidth: 320 }}>
                <Typography variant="h5" mb={2}>Login</Typography>
                <form onSubmit={handleLogin}>
                    <TextField label="E-mail" fullWidth margin="normal" value={email} onChange={e => setEmail(e.target.value)} />
                    <TextField label="Senha" type="password" fullWidth margin="normal" value={senha} onChange={e => setSenha(e.target.value)} />
                    {erro && <Typography color="error">{erro}</Typography>}
                    <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Entrar</Button>
                </form>
            </Paper>
        </Box>
    );
};

export default Login;