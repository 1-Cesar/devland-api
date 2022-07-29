package br.com.vemser.devlandapi.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
@Getter
@Setter
@Entity(name = "autenticacao")
public class UserLoginEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "autenticacao_seq")
    @SequenceGenerator(name = "autenticacao_seq", sequenceName = "seq_autenticacao", allocationSize = 1)
    @Column(name = "id_autenticacao")
    private Integer idAutenticacao;

    @Column(name = "login")
    @NotEmpty
    private String login;

    @Column(name = "senha")
    @NotEmpty
    private String senha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
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
