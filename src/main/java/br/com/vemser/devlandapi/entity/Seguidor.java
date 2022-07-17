package br.com.vemser.devlandapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Seguidor {

    private Integer id;

    private String nomeSeguidor;

    private Integer idSeguidor;

    private Integer idUsuario;
}
