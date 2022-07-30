package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import lombok.Data;

@Data
public class UserLoginDTO extends UserLoginCreateDTO{

    private Integer idUserLogin;


}
