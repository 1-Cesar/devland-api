package br.com.vemser.devlandapi.dto;

import br.com.vemser.devlandapi.entity.ComentarioEntity;
import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelatorioPostagemDTO {
    //postage
    private String nome;
    private String titulo;
    private TipoPostagem tipoPostagem;
    private LocalDateTime data;
    private String descricao;
    private Integer curtidas;
    private ComentarioEntity comentarios;

   /* // comentarios
    private String descricaoComentarios;
    private Integer curtidasComentario;
    private LocalDateTime dataComentario;
    //private UsuarioDTO usuarioComentario;*/

}
