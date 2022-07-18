package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoClassificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Contato {

    private Integer idContato;

    private Integer idUsuario;

    private String numero;

    private String descricao;

    private TipoClassificacao tipo;
}