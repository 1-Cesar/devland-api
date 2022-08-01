package br.com.vemser.devlandapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "tecnologias")
public class TecnologiasEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tecnologias_seq")
    @SequenceGenerator(name = "tecnologias_seq", sequenceName = "seq_tecnologias", allocationSize = 1)
    @Column(name = "id_tecnologia")
    private Integer idTecnologias;

    @Column(name = "id_usuario", insertable = false, updatable = false)
    private Integer idUsuario;

    @Column(name = "nome_tecnologia")
    private String nomeTecnologia;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuario;
}
