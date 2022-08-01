package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoPostagem;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

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

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "postagem",         //Indica o lado inverso do relacionamento
            cascade = CascadeType.ALL,   //Faz a cascata para deletar
            orphanRemoval = true)
    private Set<ComentarioEntity> comentarios;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuario;

}
