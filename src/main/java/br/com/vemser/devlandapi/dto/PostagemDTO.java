package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostagemDTO {

    @Schema(description = "Id da Postagem")
    private Integer idPostagem;

    @Schema(description = "Tipo da Postagem")
    private TipoPostagem tipoPostagem;

    @Schema(description = "Título da Postagem")
    private String titulo;

    @Schema(description = "Descrição da Postagem")
    private String descricao;

    @Schema(description = "Foto da Postagem")
    private String foto;

    @Schema(description = "Número de Curtidas")
    private Integer curtidas;

    @Schema(description = "Data da Postagem")
    private String data;

    @Schema(description = "Id do Usuário")
    private Integer idUsuario;

}
