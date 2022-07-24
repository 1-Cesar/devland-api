package br.com.vemser.devlandapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "seguidor")
public class SeguidorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seguidor_seq")
    @SequenceGenerator(name = "seguidor_seq", sequenceName = "seq_seguidor", allocationSize = 1)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_seguidor")
    private Integer idSeguidor;

    @Column(name = "nome_seguidor")
    private String nomeSeguidor;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    //--------------------------------------------------------------------

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuario;

}
