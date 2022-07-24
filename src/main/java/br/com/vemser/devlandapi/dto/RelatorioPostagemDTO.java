package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RelatorioPostagemDTO {
    //postagem
    private String tituloPostagem;
    private String descricaoPostagem;
    private TipoPostagem tipoPostagem;
    private String fotoPostagem;
    private Integer curtidasPostagem;
    private LocalDateTime dataPostagem;
    private UsuarioDTO usuarioDTOPostagem;

    // comentarios
    private String descricaoComentario;
    private Integer curtidasComentario;
    private LocalDateTime dataComentario;
    private UsuarioDTO usuarioDTOComentario;

}
