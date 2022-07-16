package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ContatoService;
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

    @GetMapping
    public ResponseEntity<List<ContatoDTO>> listar() throws RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.listar());
    }

    @GetMapping("/{idContato}")
    public ResponseEntity<List<ContatoDTO>> listarContato(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.listarContato(id));
    }

    @PostMapping("/{idPessoa}")
    public ResponseEntity<ContatoCreateDTO> adicionar(@PathVariable("idPessoa") Integer id, @Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.adicionar(id, contato));
    }

    @PutMapping("/{idContato}")
    public ResponseEntity<ContatoDTO> editar(@PathVariable("idContato") Integer id,
                                             @Valid @RequestBody ContatoDTO contatoAtualizar) throws RegraDeNegocioException {
        return ResponseEntity.ok(contatoService.editar(id, contatoAtualizar));
    }

    @DeleteMapping("/{idContato}")
    public void remover(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        contatoService.remover(id);
    }
}
