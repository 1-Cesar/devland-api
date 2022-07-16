package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.PostagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/postagem")
@Validated
public class PostagemController {

    @Autowired
    private PostagemService postagemService;

    public PostagemController(){

    }

    @GetMapping
    public ResponseEntity<List<PostagemDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.list(), HttpStatus.OK);
    }

    @GetMapping("/{tipoPostagem}/tipo")
    public ResponseEntity<List<PostagemDTO>> litByTipo(@PathVariable("tipoPostagem") Integer tipoPostagem) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.listByTipo(tipoPostagem), HttpStatus.OK);
    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<PostagemDTO> post(@PathVariable("idUsuario") Integer idUsuario,
                                            @RequestBody @Valid PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.post(idUsuario, postagemCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{idPostagem}")
    public ResponseEntity<PostagemDTO> update(@PathVariable() Integer idPostagem,
                                           @RequestBody @Valid PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.update(idPostagem, postagemCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idPostagem}")
    public ResponseEntity<Void> delete(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException {
        postagemService.delete(idPostagem);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
