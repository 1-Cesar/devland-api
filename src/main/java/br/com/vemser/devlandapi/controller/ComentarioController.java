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
    @PostMapping("/criar/{idPostagem}/postagem/{idUsuario}/usuario")
    public ResponseEntity<ComentarioDTO> create(@PathVariable("idPostagem") Integer idPostagem,
                                                @PathVariable("idUsuario") Integer idUsuario,
                                                @RequestBody ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(comentarioService.create(idPostagem, idUsuario, comentarioCreateDTO), HttpStatus.CREATED);
    }

    @PutMapping("/editar/{idComentario}")
    public ResponseEntity<ComentarioDTO> update(@PathVariable("idComentario") Integer idComentario,
                                                @RequestBody ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<>(comentarioService.update(idComentario, comentarioCreateDTO), HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/delete/{idComentario}")
    public ResponseEntity<Void> delete(@PathVariable("idComentario") Integer idComentario) throws RegraDeNegocioException {
        comentarioService.delete(idComentario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
