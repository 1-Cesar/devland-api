package br.com.vemser.devlandapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comentario")
public class ComentarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comentario_seq")
    @SequenceGenerator(name = "comentario_seq", sequenceName = "seq_comentario", allocationSize = 1)
    @Column(name = "id_comentario")
    private Integer idComentario;

    @Column(name = "id_postagem", insertable = false, updatable = false)
    private Integer idPostagem;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "descricao")
    private String descricaoComentarios;

    @Column(name = "curtidas")
    private Integer curtidasComentario;

    @Column(name = "data_comentario")
    private LocalDateTime dataComentario;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_postagem", referencedColumnName = "id_postagem")
    private PostagemEntity postagem;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuario;

}
