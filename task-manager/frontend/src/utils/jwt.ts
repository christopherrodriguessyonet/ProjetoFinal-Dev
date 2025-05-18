import { jwtDecode } from 'jwt-decode'; // <-- assim funciona na versão atual (v4+)


export interface TokenPayload {
    sub: string;         // E-mail do usuário
    groups: string[];    // ["ADMIN"] ou ["USER"]
    exp: number;         // Timestamp de expiração
}

export const parseJwt = (token: string): TokenPayload | null => {
    try {
        return jwtDecode<TokenPayload>(token);
    } catch (e) {
        return null;
    }
};
