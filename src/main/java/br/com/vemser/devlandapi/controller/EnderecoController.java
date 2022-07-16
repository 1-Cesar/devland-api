package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;

import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.EnderecoService;

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

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.listar());
    }

    @GetMapping("/{idEndereco}")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.listarEndereco(id));
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<EnderecoCreateDTO> adicionar(@PathVariable("idUsuario") Integer id,
                                                       @Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.adicionar(id, enderecoCreateDTO));
    }

    @PutMapping("/{idEndereco}")
    public ResponseEntity<EnderecoDTO> editar(@PathVariable("idEndereco") Integer id,
                                                  @Valid @RequestBody EnderecoDTO enderecoAtualizar) throws RegraDeNegocioException {
        return ResponseEntity.ok(enderecoService.editar(id, enderecoAtualizar));
    }

    @DeleteMapping("/{idEndereco}")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        enderecoService.delete(id);
    }

}
