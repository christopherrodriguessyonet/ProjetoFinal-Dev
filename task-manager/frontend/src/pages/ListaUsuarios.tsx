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
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import { useAuth } from '../contexts/AuthContext';
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';

const ListaUsuarios: React.FC = () => {
    const [users, setUsers] = useState<any[]>([]);
    const [usuarioFilter, setUsuarioFilter] = useState<string>('');
    const [perfilFilter, setPerfilFilter] = useState<string>('');
    const navigate = useNavigate();
    const { isAdmin } = useAuth();

    useEffect(() => {
        fetchUsers(); // Carrega os usuários ao carregar o componente
    }, []);

    const fetchUsers = async () => {
        try {
            let url = '/users';
            const params = new URLSearchParams();

            if (usuarioFilter) params.append('usuario', usuarioFilter); // Filtro de nome/e-mail
            if (perfilFilter) params.append('perfil', perfilFilter); // Filtro de perfil

            if (params.toString() !== '') {
                // Adiciona os filtros à URL
                url = `/users?${params.toString()}`;
            }

            const response = await api.get(url); // Requisição para a API
            setUsers(response.data);
        } catch (error) {
            console.error('Erro ao buscar usuários:', error);
        }
    };

    const handleClearFilters = async () => {
        setUsuarioFilter('');
        setPerfilFilter('');
        try {
            const response = await api.get('/users');
            setUsers(response.data);
        } catch (error) {
            console.error('Erro ao limpar filtros:', error);
        }
    };

    const handleDelete = async (id: number) => {
        await api.delete(`/users/${id}`);
        fetchUsers();
    };

    return (
        <Box p={4}>
            <Typography variant="h4" gutterBottom>
                Usuários
            </Typography>

            <Stack direction="row" spacing={2} mb={2} alignItems="center">
                <Box sx={{ width: '200px' }}>
                    <FormControl fullWidth>
                        <InputLabel>Perfil</InputLabel>
                        <Select
                            value={perfilFilter}
                            label="Perfil"
                            onChange={(e) => setPerfilFilter(e.target.value)}
                        >
                            <MenuItem value="">Todos</MenuItem>
                            <MenuItem value="ADMIN">Administrador</MenuItem>
                            <MenuItem value="USER">Usuário</MenuItem>
                        </Select>
                    </FormControl>
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
                    onClick={fetchUsers}
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

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Nome</TableCell>
                            <TableCell>Email</TableCell>
                            <TableCell>Perfil</TableCell>
                            <TableCell>Ações</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {users.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={4} align="center">
                                    Nenhum usuário encontrado.
                                </TableCell>
                            </TableRow>
                        ) : (
                            users.map((user) => (
                                <TableRow key={user.id}>
                                    <TableCell>{user.nome}</TableCell>
                                    <TableCell>{user.email}</TableCell>
                                    <TableCell>{user.role}</TableCell>
                                    <TableCell>
                                        <Button
                                            variant="outlined"
                                            color="primary"
                                            size="small"
                                            onClick={() => navigate(`/editar-usuario/${user.id}`)}
                                            sx={{ mr: 1 }}
                                        >
                                            Editar
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="error"
                                            size="small"
                                            onClick={() => handleDelete(user.id)}
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
        </Box>
    );
};

export default ListaUsuarios;
