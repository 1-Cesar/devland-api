package br.com.vemser.devlandapi.dto.relatorios;

import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPersonalizadoDevDTO {

    private String nome;

    private String email;

    private String areaAtuacao;

    private String foto;

    private Genero genero;

    private TipoUsuario tipoUsuario;

    private TipoClassificacao tipo;

    private String numero;

    private String descricao;

    private String cidade;

    private String estado;

    private String pais;

    private String nomeTecnologia;

}
