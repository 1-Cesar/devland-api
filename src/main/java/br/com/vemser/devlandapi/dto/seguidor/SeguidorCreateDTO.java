package br.com.vemser.devlandapi.dto.seguidor;

import lombok.Data;

@Data
public class SeguidorCreateDTO {

    private Integer idSeguidor;

    private String nomeSeguidor;

    private Integer idUsuario;
}
