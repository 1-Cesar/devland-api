package br.com.vemser.devlandapi.dto.userlogin;

import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import lombok.Data;

@Data
public class UserLoginCreateDTO {

    private String login;

    private String senha;

    private UsuarioCreateDTO usuarioCreateDTO;
}
