package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.PostagemDocs;
import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.PostagemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/postagem")
@Validated
public class PostagemController implements PostagemDocs {

    @Autowired
    private PostagemService postagemService;

    //TODO -- PAGINAÇÃO
    @GetMapping
    public ResponseEntity<List<PostagemDTO>> list() throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.list(), HttpStatus.OK);
    }

    @GetMapping("/{tipoPostagem}/tipo")
    public ResponseEntity<List<PostagemDTO>> listByTipo(@PathVariable("tipoPostagem") TipoPostagem tipoPostagem) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.listByTipo(tipoPostagem), HttpStatus.OK);
    }

    @GetMapping("{idPostagem}")
    public ResponseEntity<PostagemDTO> listByIdPostagem(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.findByIdPostagem(idPostagem),HttpStatus.OK);
    }

    // fazer igual a USUARIO post - inserir exemple
    @Override
    @PostMapping("/criar/{idUsuario}")
    public ResponseEntity<PostagemDTO> criar(@PathVariable("idUsuario") Integer idUsuario, @RequestBody PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.post(idUsuario,postagemCreateDTO),HttpStatus.OK);
    }

    @Override
    @PutMapping("/curtir/{idPostagem}")
    public ResponseEntity<PostagemDTO> curtir(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.curtir(idPostagem),HttpStatus.OK);
    }

    @Override
    @PutMapping("/editar/{idPostagem}")
    public ResponseEntity<PostagemDTO> update(@PathVariable("idPostagem") Integer idPostagem,@RequestBody PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.update(idPostagem, postagemCreateDTO),HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/deletar/{idPostagem}")
    public ResponseEntity<Void> delete(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException {
        postagemService.delete(idPostagem);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
