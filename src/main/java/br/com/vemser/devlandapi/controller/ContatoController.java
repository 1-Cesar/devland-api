package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ContatoService;
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
@RequestMapping("/contato")
@Validated
public class ContatoController {

    @Autowired
    private ContatoService contatoService;

    @Operation(summary = "listar todos os contatos", description = "recupera todos os contatos do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna a lista de contatos"),
                    @ApiResponse(responseCode = "400", description = "Nenhum contato encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    public ResponseEntity<List<ContatoDTO>> listar() throws RegraDeNegocioException {
        log.info("Listando todos os contatos");
        return ResponseEntity.ok(contatoService.listar());
    }

    @Operation(summary = "listar contato por id", description = "recupera um contato do banco de dados atraves de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna um contato através de seu id"),
                    @ApiResponse(responseCode = "400", description = "Nenhum contato encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idContato}")
    public ResponseEntity<List<ContatoDTO>> listarContato(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um contato através de seu id");
        return ResponseEntity.ok(contatoService.listarContato(id));
    }

    @Operation(summary = "listar contato por id do usuario", description = "recupera um contato do banco de dados atraves do id do usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Retorna contato através do id do usuário"),
                    @ApiResponse(responseCode = "400", description = "Nenhum contato encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<List<ContatoDTO>> listarContatoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um contato com base no id do usuário");
        return ResponseEntity.ok(contatoService.listarContatoUsuario(id));
    }

    @Operation(summary = "criar contato atraves do id do usuario", description = "cria um contato dentro do banco de dados com base no id do usuario")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Adiciona contato através do id do usuário"),
                    @ApiResponse(responseCode = "400", description = "Nenhum usuário encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/{idPessoa}")
    public ResponseEntity<ContatoCreateDTO> adicionar(@PathVariable("idPessoa") Integer id,
                                                      @Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException {
        log.info("Criando um contato com base no id do usuário");
        return ResponseEntity.ok(contatoService.adicionar(id, contato));
    }

    @Operation(summary = "altera um contato por id", description = "altera os registros de um contato no banco de dados atraves de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Modifica um contato através do seu id"),
                    @ApiResponse(responseCode = "400", description = "Nenhum contato encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{idContato}")
    public ResponseEntity<ContatoDTO> editar(@PathVariable("idContato") Integer id,
                                             @Valid @RequestBody ContatoDTO contatoAtualizar) throws RegraDeNegocioException {
        log.info("Alterando um contato com base em seu id");
        return ResponseEntity.ok(contatoService.editar(id, contatoAtualizar));
    }

    @Operation(summary = "deleta contato", description = "deleta um contato do banco de dados atraves de seu id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deleta um contato através de seu id"),
                    @ApiResponse(responseCode = "400", description = "Nenhum contato encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idContato}")
    public void remover(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        log.info("Removendo um contato com base em seu id");
        contatoService.remover(id);
    }
}
