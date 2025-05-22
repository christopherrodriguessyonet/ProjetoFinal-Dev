package br.com.syonet.taskmanager.dto;

public class TokenDTO {
    public String accessToken;
    public String refreshToken;

    public TokenDTO() {}

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
