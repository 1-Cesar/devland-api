package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.ComentarioDocs;
import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/comentario")
@Validated
public class ComentarioController implements ComentarioDocs {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<ComentarioDTO> list() throws RegraDeNegocioException {
        return comentarioService.list();
    }

    @Override
    public ResponseEntity<ComentarioDTO> post(Integer idPostagem, ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return null;
    }

    @Override
    public ResponseEntity<Void> delete(Integer idComenatario) throws RegraDeNegocioException {
        return null;
    }

//    @PostMapping("/{idPostagem}")
//    public ResponseEntity<ComentarioDTO> post(@PathVariable("idPostagem") Integer idPostagem,
//                                              @RequestBody @Valid ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
//        return new ResponseEntity<ComentarioDTO>(comentarioService.post(idPostagem, comentarioCreateDTO), HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{idComentario}")
//    public ResponseEntity<Void> delete(@PathVariable("idComentario") Integer idComenatario) throws RegraDeNegocioException {
//        comentarioService.delete(idComenatario);
//        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//    }
}
