package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usuario")
@Validated
public class UsuarioController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @PostMapping
    public ResponseEntity<UsuarioCreateDTO> adicionar(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.adicionar(usuarioCreateDTO));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable("idUsuario") Integer id,
                                            @Valid @RequestBody UsuarioDTO usuarioAtualizar) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.editar(id, usuarioAtualizar));
    }

    @DeleteMapping("/{idUsuario}")
    public void delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        usuarioService.delete(id);
    }
}
