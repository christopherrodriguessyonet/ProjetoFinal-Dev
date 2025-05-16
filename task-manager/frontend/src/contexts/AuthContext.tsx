import React, { createContext, useContext, useState } from 'react';
import api from '../services/api';

interface AuthContextData {
    isAuthenticated: boolean;
    isAdmin: boolean;
    token: string | null;
    login: (email: string, password: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const [token, setToken] = useState<string | null>(localStorage.getItem('token'));
    const [isAdmin, setIsAdmin] = useState(false);

    const login = async (email: string, password: string) => {
        try {
            const response = await api.post('/auth/login', { email, password });
            const { token } = response.data;
            localStorage.setItem('token', token);
            setToken(token);
            // Aqui você pode adicionar lógica para verificar se é admin, se o backend retornar essa info
        } catch (error) {
            throw new Error('Falha na autenticação');
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        setToken(null);
        setIsAdmin(false);
    };

    return (
        <AuthContext.Provider value={{
            isAuthenticated: !!token,
            isAdmin,
            token,
            login,
            logout
        }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);