package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.enums.TipoClassificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoCreateDTO {

    private Integer idUsuario;

    private TipoClassificacao tipo;

    private String logradouro;

    private String numero;

    private String complemento;

    private String cep;

    private String cidade;

    private String estado;

    private String pais;
}
