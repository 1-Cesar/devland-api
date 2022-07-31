package br.com.vemser.devlandapi.dto.userlogin;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import lombok.Data;

@Data
public class UserLoginCreateDTO {

    private String login;

    private String senha;

    private UsuarioEntity usuarioEntity;
}
