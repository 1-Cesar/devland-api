package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.EnderecoNotas;
import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface EnderecoDocs {
    @EnderecoNotas
    @Operation(summary = "listar endereços", description = "recupera todos os endereços do banco de dados")
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "listar endereço por id", description = "recupera um endereço do banco de dados atraves de seu id")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "listar endereço por id do usuario", description = "recupera um endereço do banco de dados atraves do id do usuario")
    public ResponseEntity<List<EnderecoDTO>> listarEnderecoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "criar endereço atraves do id do usuario", description = "cria um endereço dentro do banco de dados com base no id do usuario")
    public ResponseEntity<EnderecoCreateDTO> adicionar(@PathVariable("idUsuario") Integer id, @Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "altera um endereço por id", description = "altera os registros de um endereço no banco de dados atraves de seu id")
    public ResponseEntity<EnderecoDTO> editar(@PathVariable("idEndereco") Integer id, @Valid @RequestBody EnderecoDTO enderecoAtualizar) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "deleta endereço", description = "deleta um endereço do banco de dados atraves de seu id")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException;

}
