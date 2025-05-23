import React, { createContext, useContext, useState } from 'react';
import api from '../services/api';
import { parseJwt, TokenPayload } from '../utils/jwt';

interface AuthContextData {
    isAuthenticated: boolean;
    isAdmin: boolean;
    token: string | null;
    user: TokenPayload | null;
    login: (email: string, password: string) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    const storedToken = localStorage.getItem('token');
    let initialUser = null;
    try {
        initialUser = storedToken ? parseJwt(storedToken) : null;
    } catch (e) {
        initialUser = null;
    }


    const [token, setToken] = useState<string | null>(storedToken);
    const [user, setUser] = useState<TokenPayload | null>(initialUser);
    const [isAdmin, setIsAdmin] = useState(initialUser?.groups.includes('ADMIN') || false);

    const login = async (email: string, password: string) => {
        try {
            const response = await api.post('/auth/login', { email, senha: password });
            const { accessToken, refreshToken } = response.data;

            localStorage.setItem('token', accessToken);
            localStorage.setItem('refreshToken', refreshToken);


            setToken(accessToken);
            const userData = parseJwt(accessToken);
            setUser(userData);
            setIsAdmin(userData?.groups.includes('ADMIN') || false);
            localStorage.setItem('user', JSON.stringify(userData));
        } catch (error) {
            throw new Error('Falha na autenticação');
        }
    };


    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('user');
        setToken(null);
        setUser(null);
        setIsAdmin(false);
    };

    return (
        <AuthContext.Provider value={{
            isAuthenticated: !!token,
            isAdmin,
            token,
            user,
            login,
            logout
        }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
