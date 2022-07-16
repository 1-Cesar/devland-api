package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoClassificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    private Integer idUsuario;

    private Integer idEndereco;

    private TipoClassificacao tipo;

    private String logradouro;

    private String numero;

    private String complemento;

    private String cep;

    private String cidade;

    private String estado;

    private String pais;
}
