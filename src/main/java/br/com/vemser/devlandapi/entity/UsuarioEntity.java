package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoUsuario;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "usuario")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "seq_usuario", allocationSize = 1)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    private String nome;

    private String email;

    private String areaAtuacao;

    private String cpfCnpj;

    private String foto;

    private TipoUsuario tipoUsuario;
}
