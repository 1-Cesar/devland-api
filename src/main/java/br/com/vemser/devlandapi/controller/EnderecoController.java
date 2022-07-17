package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;

import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.EnderecoService;

import io.swagger.v3.oas.annotations.Operation;
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
    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.listar());
    }

    @Operation(summary = "listar endereço por id", description = "recupera um endereço do banco de dados atraves de seu id")
    @GetMapping("/{idEndereco}")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.listarEndereco(id));
    }

    @Operation(summary = "listar endereço por id do usuario", description = "recupera um endereço do banco de dados atraves do id do usuario")
    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<List<EnderecoDTO>> listarEnderecoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.listarEnderecoUsuario(id));
    }

    @Operation(summary = "criar endereço atraves do id do usuario", description = "cria um endereço dentro do banco de dados com base no id do usuario")
    @PostMapping("/{idUsuario}")
    public ResponseEntity<EnderecoCreateDTO> adicionar(@PathVariable("idUsuario") Integer id,
                                                       @Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.adicionar(id, enderecoCreateDTO));
    }

    @Operation(summary = "altera um endereço por id", description = "altera os registros de um endereço no banco de dados atraves de seu id")
    @PutMapping("/{idEndereco}")
    public ResponseEntity<EnderecoDTO> editar(@PathVariable("idEndereco") Integer id,
                                              @Valid @RequestBody EnderecoDTO enderecoAtualizar) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.editar(id, enderecoAtualizar));
    }

    @Operation(summary = "deleta endereço", description = "deleta um endereço do banco de dados atraves de seu id")
    @DeleteMapping("/{idEndereco}")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        enderecoService.delete(id);
    }

}
