package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;

import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/endereco")
@Validated
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @Operation(summary = "listar endereços", description = "recupera todos os endereços do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de endereços"),
                    @ApiResponse(responseCode = "400", description = "Nenhum endereço encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException {
        log.info("Listando todos os endereços");
        return ResponseEntity.ok(enderecoService.listar());
    }

    @Operation(summary = "listar endereço por id", description = "recupera um endereço do banco de dados atraves de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna endereço por id"),
                    @ApiResponse(responseCode = "400", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idEndereco}")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um endereço com base em seu id");
        return ResponseEntity.ok(enderecoService.listarEndereco(id));
    }

    @Operation(summary = "listar endereço por id do usuario", description = "recupera um endereço do banco de dados atraves do id do usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna endereço por id do usuario"),
                    @ApiResponse(responseCode = "400", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<List<EnderecoDTO>> listarEnderecoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um endereço com base no id do usuário");
        return ResponseEntity.ok(enderecoService.listarEnderecoUsuario(id));
    }

    @Operation(summary = "criar endereço atraves do id do usuario", description = "cria um endereço dentro do banco de dados com base no id do usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Adiciona um endereço atraves do id do usuario"),
                    @ApiResponse(responseCode = "400", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/{idUsuario}")
    public ResponseEntity<EnderecoCreateDTO> adicionar(@PathVariable("idUsuario") Integer id,
                                                       @Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando um endereço com base no id do usuário");
        return ResponseEntity.ok(enderecoService.adicionar(id, enderecoCreateDTO));
    }

    @Operation(summary = "altera um endereço por id", description = "altera os registros de um endereço no banco de dados atraves de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Modifica um endereço por id"),
                    @ApiResponse(responseCode = "400", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idEndereco}")
    public ResponseEntity<EnderecoDTO> editar(@PathVariable("idEndereco") Integer id,
                                              @Valid @RequestBody EnderecoDTO enderecoAtualizar) throws RegraDeNegocioException {
        log.info("Modificando um endereço com base em seu id");
        return ResponseEntity.ok(enderecoService.editar(id, enderecoAtualizar));
    }

    @Operation(summary = "deleta endereço", description = "deleta um endereço do banco de dados atraves de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deleta um endereço por id"),
                    @ApiResponse(responseCode = "400", description = "Endereço não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idEndereco}")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        log.info("Excluindo um endereço com base em seu id");
        enderecoService.delete(id);
    }

}
