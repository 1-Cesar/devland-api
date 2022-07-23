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

//    , insertable = false, updatable = false
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoPostagem tipoPostagem;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "foto")
    private String foto;

    @Column(name = "curtidas")
    private Integer curtidas;

    @Column(name = "data_postagem")
    private LocalDateTime data;


    //TODO verificar Script - conferir ON DELETE CASCADE - nas filhas
}
