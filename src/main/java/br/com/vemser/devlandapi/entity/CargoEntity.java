package br.com.vemser.devlandapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cargo")
public class CargoEntity implements GrantedAuthority {

    @Id
    @SequenceGenerator(name = "SEQ_CARGO", sequenceName = "seq_cargo", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CARGO")
    @Column(name = "ID_CARGO")
    private int idCargo;

    @Column(name = "NOME")
    private String nome;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USERLOGIN_CARGO",
            joinColumns = @JoinColumn(name = "ID_CARGO"),
            inverseJoinColumns = @JoinColumn(name = "ID_USERLOGIN")
    )
    private Set<UsuarioEntity> usuarios;

    @Override
    public String getAuthority() {
        return this.nome;
    }
}
