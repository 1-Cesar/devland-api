package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.AuthDocs;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginAuthDTO;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.enums.TipoStatus;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.security.TokenService;
import br.com.vemser.devlandapi.service.UserLoginService;
import br.com.vemser.devlandapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AuthController implements AuthDocs {

    private final UserLoginService userLoginService;

    private final TokenService tokenService;

    private final AuthenticationManager authenticationManager;

    private final UsuarioService usuarioService;

    @PostMapping
    public String auth(@RequestBody @Valid UserLoginAuthDTO userLoginAuthDTO) throws RegraDeNegocioException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(
                        userLoginAuthDTO.getLogin(),
                        userLoginAuthDTO.getSenha()
                );

        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        Object usuarioLogado = authentication.getPrincipal();
        UserLoginEntity userLoginEntity = (UserLoginEntity) usuarioLogado;
        String token = tokenService.getToken(userLoginEntity);
        return token;
    }

    @GetMapping("/recuperar-login")
    public String usuarioLogado() throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        return usuarioLogadoEntity.getLogin();
    }

    @PutMapping("/trocar-senha")
    public String trocarSenha(@RequestBody @Valid UserLoginAuthDTO userLoginAuthDTO) throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        return userLoginService.trocarSenha(userLoginAuthDTO, usuarioLogadoEntity);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> adicionar(@Valid @RequestBody UserLoginCreateDTO userLoginCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.adicionar(userLoginCreateDTO));
    }

    @PutMapping("/alterar-status/{idUsuario}")
    public String desativar(@PathVariable("idUsuario") Integer id, TipoStatus opcao) throws RegraDeNegocioException {
        //log.info("Alterando um usuário com base em seu id");
        return userLoginService.desativar(id, opcao);
    }

}