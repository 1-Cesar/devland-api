package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    private String nome, email, senha, areaAtuacao, cpfCnpj;

    private TipoUsuario tipoUsuario;

    private Integer idUsuario;
}
