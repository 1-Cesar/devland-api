package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.UserLoginDTO;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.security.TokenService;
import br.com.vemser.devlandapi.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final UserLoginService userLoginService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;

    @PostMapping
    public String auth(@RequestBody @Valid UserLoginCreateDTO userLoginCreateDTO) throws RegraDeNegocioException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userLoginCreateDTO.getLogin(),
                        userLoginCreateDTO.getSenha()
                );
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Object usuarioLogado = authentication.getPrincipal();
        UserLoginEntity userLoginEntity = (UserLoginEntity) usuarioLogado;
        String token = tokenService.getToken(userLoginEntity);
        return token;
    }

    @GetMapping("/recuperarLogin")
    public String usuarioLogado() throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        return usuarioLogadoEntity.getLogin();
    }
}