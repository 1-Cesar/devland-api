package br.com.vemser.devlandapi.dto;

import lombok.Data;

@Data
public class SeguidorCreateDTO {

    private String nomeSeguidor;

    private Integer idSeguidor;

    private Integer idUsuario;
}
