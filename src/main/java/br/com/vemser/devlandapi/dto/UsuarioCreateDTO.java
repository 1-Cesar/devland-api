package br.com.vemser.devlandapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioCreateDTO extends UsuarioDTO {

    private Integer idUsuario;
}
