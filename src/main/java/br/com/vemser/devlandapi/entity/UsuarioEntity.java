package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "seq_usuario", allocationSize = 1)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nome")
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "area_atuacao")
    private String areaAtuacao;

    @Column(name = "cpf_cnpj")
    private String cpfCnpj;

    @Column(name = "foto")
    private String foto;

    @Column(name = "genero")
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @Column(name = "tipo")
    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;


    //------------------------------------------------------------------------------------------------------------------
    // Relacionamento Usuário - Contatos

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "usuario",         //Indica o lado inverso do relacionamento
            cascade = CascadeType.ALL,   //Faz a cascata para deletar
            orphanRemoval = true)        //Deleta os órfãos
    private Set<ContatoEntity> contatos;

    //------------------------------------------------------------------------------------------------------------------
    // Relacionamento Usuário - Seguidores

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "usuario",         //Indica o lado inverso do relacionamento
            cascade = CascadeType.ALL   //Faz a cascata para deletar
            )        //Deleta os órfãos
    private List<SeguidorEntity> seguidores;

    //------------------------------------------------------------------------------------------------------------------
    //Relacionamento Usuário - Endereços

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "usuario_x_endereco",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_endereco"))
    private List<EnderecoEntity> enderecos;

    //------------------------------------------------------------------------------------------------------------------
    //Relacionamento Usuário - Tecnologias

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "usuario",
            cascade = CascadeType.ALL
            )
    private List<TecnologiasEntity> tecnologias;

    //------------------------------------------------------------------------------------------------------------------
    // Relacionamento Usuário — Postagens

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "usuario",         //Indica o lado inverso do relacionamento
            cascade = CascadeType.ALL,   //Faz a cascata para deletar
            orphanRemoval = true)
    private Set<PostagemEntity> postagens;

    //------------------------------------------------------------------------------------------------------------------
    // Relacionamento Usuário — Comentários

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "usuario",         //Indica o lado inverso do relacionamento
            cascade = CascadeType.ALL,   //Faz a cascata para deletar
            orphanRemoval = true)
    private Set<ComentarioEntity> comentarios;

    //------------------------------------------------------------------------------------------------------------------
    // Relacionamento Usuário — UserLogin

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UserLoginEntity userLoginEntity;
}
