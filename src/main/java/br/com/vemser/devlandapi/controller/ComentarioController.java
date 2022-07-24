package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.ComentarioDocs;
import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
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

    //TODO - completar os return
    @Override
    @PostMapping("/criar/{idPostagem}")
    public ResponseEntity<ComentarioDTO> create(@PathVariable("idPostagem") Integer idPostagem, @RequestBody ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return null;
    }

    @PutMapping("/editar/{idComentario}")
    public ResponseEntity<ComentarioDTO> update(@PathVariable("idPostagem") Integer idPostagem, @RequestBody ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return null;
    }

    @Override
    @DeleteMapping("/delete/{idComentario}")
    public ResponseEntity<Void> delete(@PathVariable("idComentario") Integer idComentario) throws RegraDeNegocioException {
        return null;
    }
}
