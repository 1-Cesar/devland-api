package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import lombok.Data;

@Data
public class UserLoginCreateDTO {

    private String login;

    private String senha;

    private UsuarioEntity usuarioEntity;


}
