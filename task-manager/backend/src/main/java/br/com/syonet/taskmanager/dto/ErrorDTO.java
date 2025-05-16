package br.com.syonet.taskmanager.dto;

public class ErrorDTO {
    public String message;
    public int status;

    public ErrorDTO(String message, int status) {
        this.message = message;
        this.status = status;
    }
} 