package br.com.vemser.devlandapi.entity;

import lombok.*;

import javax.persistence.*;

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

    @Column(name = "id_postagem")
    private Integer idPostagem;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "ups")
    private Integer curtidas;


    @Column(name = "data_comentario")
    private String data;


    private UsuarioEntity usuarioEntity;

}
