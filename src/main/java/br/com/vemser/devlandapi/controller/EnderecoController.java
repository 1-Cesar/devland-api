package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.EnderecoDocs;
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
public class EnderecoController implements EnderecoDocs {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException {
        log.info("Listando todos os endereços");
        return ResponseEntity.ok(enderecoService.listar());
    }

    @GetMapping("/{idEndereco}")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um endereço com base em seu id");
        return ResponseEntity.ok(enderecoService.listarEndereco(id));
    }

    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<List<EnderecoDTO>> listarEnderecoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um endereço com base no id do usuário");
        return ResponseEntity.ok(enderecoService.listarEnderecoUsuario(id));
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<EnderecoCreateDTO> adicionar(@PathVariable("idUsuario") Integer id,
                                                       @Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando um endereço com base no id do usuário");
        return ResponseEntity.ok(enderecoService.adicionar(id, enderecoCreateDTO));
    }

    @PutMapping("/{idEndereco}")
    public ResponseEntity<EnderecoDTO> editar(@PathVariable("idEndereco") Integer id,
                                              @Valid @RequestBody EnderecoDTO enderecoAtualizar) throws RegraDeNegocioException {
        log.info("Modificando um endereço com base em seu id");
        return ResponseEntity.ok(enderecoService.editar(id, enderecoAtualizar));
    }

    @DeleteMapping("/{idEndereco}")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        log.info("Excluindo um endereço com base em seu id");
        enderecoService.delete(id);
    }

}
