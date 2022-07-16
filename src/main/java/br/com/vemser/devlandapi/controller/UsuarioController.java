package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
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
    private UsuarioService usuarioService;

    @Operation(summary = "listar usuarios", description = "recupera todas os usuarios do banco de dados")
    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.listar());
    }

    @Operation(summary = "listar usuario por id", description = "recupera um usuario do banco de dados atraves de seu id")
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.listarUsuario(id));
    }

    @Operation(summary = "criar usuario", description = "cria um usuario dentro do banco de dados")
    @PostMapping
    public ResponseEntity<UsuarioCreateDTO> adicionar(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.adicionar(usuarioCreateDTO));
    }

    @Operation(summary = "altera um usuario por id", description = "altera os registros de um usuario no banco de dados atraves de seu id")
    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable("idUsuario") Integer id,
                                            @Valid @RequestBody UsuarioDTO usuarioAtualizar) throws RegraDeNegocioException {
        return ResponseEntity.ok(usuarioService.editar(id, usuarioAtualizar));
    }

    @Operation(summary = "deleta um usuario", description = "deleta um usuario do banco de dados atraves de seu id")
    @DeleteMapping("/{idUsuario}")
    public void delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        usuarioService.delete(id);
    }
}
