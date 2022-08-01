package br.com.vemser.devlandapi.security;

import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UserLoginService usuarioService;

    @Override //2:11
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //buscar usuário pelo login
        Optional<UserLoginEntity> optionalUserLogin = usuarioService.findByLogin(username);

        return optionalUserLogin
                .orElseThrow(() -> new UsernameNotFoundException("Usuário Inválido"));

    }
}