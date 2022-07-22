package br.com.vemser.devlandapi.entity;

import br.com.vemser.devlandapi.enums.TipoClassificacao;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "endereco")
public class EnderecoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "endereco_seq")
    @SequenceGenerator(name = "endereco_seq", sequenceName = "seq_endereco", allocationSize = 1)
    @Column(name = "id_endereco")
    private Integer idEndereco;

    private TipoClassificacao tipo;

    private String logradouro;

    private String numero;

    private String complemento;

    private String cep;

    private String cidade;

    private String estado;

    private String pais;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "usuario_x_endereco",
            joinColumns = @JoinColumn(name = "id_endereco"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario"))
    private List<UsuarioEntity> usuarios;
}
