package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.PostagemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/postagem")
@Validated
public class PostagemController {

    private PostagemService postagemService;

    public PostagemController(){

    }

    @PostMapping("/{idUsuario}")
    public ResponseEntity<PostagemDTO> post(@PathVariable("idUsuario") Integer idUsuario,
                                            @RequestBody @Valid PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(postagemService.post(idUsuario, postagemCreateDTO), HttpStatus.CREATED);
    }


}
