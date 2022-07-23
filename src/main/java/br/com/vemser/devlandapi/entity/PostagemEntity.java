package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "postagem")
public class PostagemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "postagem_seq")
    @SequenceGenerator(name = "postagem_seq", sequenceName = "seq_postagem", allocationSize = 1)
    @Column(name = "id_postagem")
    private Integer idPostagem;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "tipo")
    private TipoPostagem tipoPostagem;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    //TODO - adicionar campo na tabela
    @Column(name = "foto")
    private String foto;

    //TODO - renomear ups(curtidas) e remover downs -- em comentario e em postagem
    @Column(name = "ups")
    private Integer curtidas;

    @Column(name = "data_postagem")
    private LocalDateTime data;
}
