package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.AuthNotas;
import br.com.vemser.devlandapi.annotations.UsuarioNotas;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginAuthDTO;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.enums.TipoStatus;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface AuthDocs {
    @AuthNotas
    @Operation(summary = "Gerar o token de acesso", description = "Valida usuario e senha, e gera um token de acesso.")
    public String auth(@RequestBody @Valid UserLoginAuthDTO userLoginAuthDTO) throws RegraDeNegocioException;

    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Nenhum usuário logado."),
                    @ApiResponse(responseCode = "500", description = "Erro! Durante a execução, foi gerada uma exceção")
            }
    )
    @Operation(summary = "Exibir o login do usuario", description = "Retorna o login do usuario que esta logado.")
    public String usuarioLogado() throws RegraDeNegocioException;
    @AuthNotas
    @Operation(summary = "Trocar a senha usuario", description = "Permite que apenas o usuario troque sua senha.")
    public String trocarSenha(@RequestBody @Valid UserLoginAuthDTO userLoginAuthDTO) throws RegraDeNegocioException;

    @AuthNotas
    @Operation(summary = "Criar um usuário com login e senha", description = "Cria um usuário completo login e senha.")
    public ResponseEntity<UsuarioDTO> adicionar(@Valid @RequestBody UserLoginCreateDTO userLoginCreateDTO) throws RegraDeNegocioException;

    @AuthNotas
    @Operation(summary = "Alterar o status", description = "Permite ao ADMIN trocar o status do usuario.")
    public String desativar(@PathVariable("idUsuario") Integer id, TipoStatus opção) throws RegraDeNegocioException;
}
