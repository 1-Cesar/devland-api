package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.PostagemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    public ResponseEntity<List<PostagemDTO>> getAll() throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{idPostagem}")
    public ResponseEntity<PostagemDTO> getByIdPostagem(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.getByIdPostagem(idPostagem), HttpStatus.OK);
    }

    @GetMapping("/{tipoPostagem}/tipo")
    public ResponseEntity<List<PostagemDTO>> getByTipo(@PathVariable("tipoPostagem") Integer tipoPostagem) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.getByTipo(tipoPostagem), HttpStatus.OK);
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
