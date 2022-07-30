package br.com.vemser.devlandapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity(name = "userlogin")
public class UserLoginEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userlogin_seq")
    @SequenceGenerator(name = "userlogin_seq", sequenceName = "seq_userlogin", allocationSize = 1)
    @Column(name = "id_userlogin")
    private Integer idUserLogin;

    @Column(name = "login")
    @NotEmpty
    private String login;

    @Column(name = "senha")
    @NotEmpty
    private String senha;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    private UsuarioEntity usuarioEntity;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "USERLOGIN_CARGO",
            joinColumns = @JoinColumn(name = "ID_USERLOGIN"),
            inverseJoinColumns = @JoinColumn(name = "ID_CARGO")
    )
    private Set<CargoEntity> cargos;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return cargos;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
