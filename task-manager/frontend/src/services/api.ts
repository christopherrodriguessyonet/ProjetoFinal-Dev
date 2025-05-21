import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers = config.headers || {};
        config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
});

api.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // Se não for erro de autenticação, repassa normalmente
        if (!error.response || error.response.status !== 401 || originalRequest._retry) {
            return Promise.reject(error);
        }

        originalRequest._retry = true;
        const refreshToken = localStorage.getItem('refreshToken');

        if (!refreshToken) {
            window.location.href = '/login';
            return Promise.reject(error);
        }

        try {
            const response = await axios.post('http://localhost:8080/api/auth/refresh', {
                refreshToken,
            });

            const { accessToken, refreshToken: newRefresh } = response.data;
            localStorage.setItem('token', accessToken);
            localStorage.setItem('refreshToken', newRefresh);

            // Atualiza e reenvia a requisição original
            originalRequest.headers['Authorization'] = `Bearer ${accessToken}`;
            return api(originalRequest);
        } catch (err) {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            window.location.href = '/login';
            return Promise.reject(err);
        }
    }
);

export default api;
