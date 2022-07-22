package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.ComentarioDocs;
import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioRespDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comentario")
@Validated
public class ComentarioController implements ComentarioDocs {

    @Autowired
    private ComentarioService comentarioService;

    @PostMapping("/{idPostagem}")
    public ResponseEntity<ComentarioRespDTO> post(@PathVariable("idPostagem") Integer idPostagem,
                                                  @RequestBody @Valid ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<ComentarioRespDTO>(comentarioService.post(idPostagem, comentarioCreateDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{idComentario}")
    public ResponseEntity<Void> delete(@PathVariable("idComentario") Integer idComenatario) throws RegraDeNegocioException {
        comentarioService.delete(idComenatario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
