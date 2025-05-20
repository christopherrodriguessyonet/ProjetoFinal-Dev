import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import api from '../services/api';
import {
    TextField,
    Button,
    Paper,
    Typography,
    Box,
    MenuItem,
    Alert,
} from '@mui/material';

const roles = [
    { value: 'USER', label: 'Usuário' },
    { value: 'ADMIN', label: 'Administrador' },
];

const EditarUsuario: React.FC = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [nome, setNome] = useState('');
    const [email, setEmail] = useState('');
    const [role, setRole] = useState('USER');
    const [senha, setSenha] = useState('');
    const [erro, setErro] = useState('');
    const [sucesso, setSucesso] = useState('');

    useEffect(() => {
        const fetchUser = async () => {
            try {
                const response = await api.get(`/users/${id}`);
                const { nome, email, role } = response.data;
                setNome(nome);
                setEmail(email);
                setRole(role);
            } catch (error) {
                setErro('Erro ao carregar dados do usuário.');
            }
        };

        if (id) {
            fetchUser();
        }
    }, [id]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setErro('');
        try {
            await api.put(`/users/${id}`, {
                nome,
                email,
                senha: senha || undefined, // só envia se não for vazio
                role,
            });
            setSucesso('Usuário atualizado com sucesso!');
            setTimeout(() => navigate('/dashboard'), 1500);
        } catch (error: any) {
            setErro(error.response?.data?.message || 'Erro ao atualizar usuário.');
        }
    };

    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
            <Paper elevation={3} sx={{ p: 4, minWidth: 320 }}>
                <Typography variant="h5" mb={2}>Editar Usuário</Typography>
                <form onSubmit={handleSubmit}>
                    <TextField
                        label="Nome"
                        fullWidth
                        margin="normal"
                        value={nome}
                        onChange={e => setNome(e.target.value)}
                        required
                    />
                    <TextField
                        label="E-mail"
                        fullWidth
                        margin="normal"
                        value={email}
                        onChange={e => setEmail(e.target.value)}
                        required
                    />
                    <TextField
                        label="Nova Senha"
                        type="password"
                        fullWidth
                        margin="normal"
                        value={senha}
                        onChange={e => setSenha(e.target.value)}
                    />
                    <TextField
                        select
                        label="Perfil"
                        fullWidth
                        margin="normal"
                        value={role}
                        onChange={e => setRole(e.target.value)}
                        required
                    >
                        {roles.map(option => (
                            <MenuItem key={option.value} value={option.value}>{option.label}</MenuItem>
                        ))}
                    </TextField>
                    {erro && <Alert severity="error" sx={{ mt: 2 }}>{erro}</Alert>}
                    {sucesso && <Alert severity="success" sx={{ mt: 2 }}>{sucesso}</Alert>}
                    <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 2 }}>Salvar</Button>
                </form>
            </Paper>
        </Box>
    );
};

export default EditarUsuario;
