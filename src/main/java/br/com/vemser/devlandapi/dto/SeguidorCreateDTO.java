package br.com.vemser.devlandapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeguidorCreateDTO {

    private String nomeSeguidor;

    private Integer idSeguidor;

    private Integer idUsuario;
}
