package br.com.syonet.taskmanager.dto;

import br.com.syonet.taskmanager.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDTO {
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, message = "O nome deve ter no mínimo 3 caracteres")
    public String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    public String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    public String senha;

    public String role;

    public static UserDTO fromEntity(User user) {
        UserDTO dto = new UserDTO();
        dto.nome = user.nome;
        dto.email = user.email;
        dto.senha = "";
        dto.role = user.role.name();
        return dto;
    }
    
}
