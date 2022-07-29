package br.com.vemser.devlandapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override //2:11
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //buscar usuário pelo login
        Optional<UsuarioEntity> usuarioOptional = usuarioService.findByLogin(username);

        return usuarioOptional
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Inválido"));

    }
}