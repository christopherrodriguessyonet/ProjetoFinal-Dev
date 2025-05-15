import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { Box, Button, TextField, Typography, Paper } from '@mui/material';
import { useAuth } from '../contexts/AuthContext';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const navigate = useNavigate();
    const { login } = useAuth();

    const onSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErro('');
        try {
            await login(email, senha);
            navigate('/home');
        } catch (err: any) {
            setErro('Credenciais inválidas');
        }
    };

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
            <Paper elevation={3} sx={{ p: 4, minWidth: 320 }}>
                <Typography variant="h5" mb={2}>Login</Typography>
                <form onSubmit={onSubmit}>
                    <TextField label="E-mail" fullWidth margin="normal" value={email} onChange={e => setEmail(e.target.value)} />
                    <TextField label="Senha" type="password" fullWidth margin="normal" value={senha} onChange={e => setSenha(e.target.value)} />
                    {erro && <Typography color="error">{erro}</Typography>}
                    <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Entrar</Button>
                    <Button variant="outlined" color="secondary" fullWidth sx={{ mt: 2 }} onClick={() => alert('Contate seu administrador')}>Solicitar registro</Button>
                </form>
            </Paper>
        </Box>
    );
};

export default Login;
