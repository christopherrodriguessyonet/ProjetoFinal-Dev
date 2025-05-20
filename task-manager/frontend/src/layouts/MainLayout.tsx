import React from 'react';
import { Box, Drawer, Toolbar, List, ListItem, ListItemButton, ListItemText, AppBar, Typography, IconButton } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const drawerWidth = 240;

const MainLayout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const { isAuthenticated, isAdmin, logout } = useAuth();
    const navigate = useNavigate();

    if (!isAuthenticated) return <>{children}</>;

    const menuItems = [
        { label: 'Home', path: '/home' },
        { label: 'Minhas Tarefas', path: '/minhas-tarefas' },
        { label: 'Nova Tarefa', path: '/nova-tarefa' },
        ...(isAdmin ? [
            { label: 'Dashboard', path: '/dashboard' },
            { label: 'Cadastrar Usuário', path: '/cadastrar-usuario' },
        ] : []),
        { label: 'Sair', action: () => { logout(); navigate('/login'); } },
    ];

    return (
        <Box sx={{ display: 'flex' }}>
            <AppBar position="fixed" sx={{ zIndex: (theme) => theme.zIndex.drawer + 1 }}>
                <Toolbar>
                    <Typography variant="h6" noWrap>Gerenciador de tarefas</Typography>
                </Toolbar>
            </AppBar>

            <Drawer variant="permanent" sx={{
                width: drawerWidth,
                [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: 'border-box' },
            }}>
                <Toolbar />
                <List>
                    {menuItems.map((item, index) => (
                        <ListItem key={index} disablePadding>
                            <ListItemButton onClick={() => item.path ? navigate(item.path) : item.action?.()}>
                                <ListItemText primary={item.label} />
                            </ListItemButton>
                        </ListItem>
                    ))}
                </List>
            </Drawer>

            <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
                <Toolbar />
                {children}
            </Box>
        </Box>
    );
};

export default MainLayout;