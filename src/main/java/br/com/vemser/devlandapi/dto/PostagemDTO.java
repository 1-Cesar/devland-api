package br.com.vemser.devlandapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostagemDTO extends PostagemCreateDTO {

    @Schema(description = "Id da Postagem")
    private Integer idPostagem;

    @Schema(description = "Número de Curtidas")
    private Integer ups;

    @Schema(description = "Número de Descurtidas")
    private Integer downs;

    @Schema(description = "Número de Visualizações")
    private Integer views;

    @Schema(description = "Data da Postagem")
    private Date data;

}
