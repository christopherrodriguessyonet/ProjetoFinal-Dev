package br.com.syonet.taskmanager.dto;

import java.util.List;

public class TokenPayload {
    public String sub;
    public List<String> groups;
    public long exp;
}
