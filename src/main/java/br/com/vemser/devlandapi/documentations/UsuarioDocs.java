package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.UsuarioNotas;
import br.com.vemser.devlandapi.dto.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface UsuarioDocs {
    @UsuarioNotas
    @Operation(summary = "listar usuarios", description = "recupera todas os usuarios do banco de dados")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "listar usuario por id", description = "recupera um usuario do banco de dados atraves de seu id")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "criar usuario", description = "cria um usuario dentro do banco de dados")
    public ResponseEntity<UsuarioCreateDTO> adicionar(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "altera um usuario por id", description = "altera os registros de um usuario no banco de dados atraves de seu id")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable("idUsuario") Integer id, @Valid @RequestBody UsuarioDTO usuarioAtualizar) throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "deleta um usuario", description = "deleta um usuario do banco de dados atraves de seu id")
    public void delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;
}
