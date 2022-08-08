package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.EnderecoNotas;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

public interface EnderecoDocs {
    @EnderecoNotas
    @Operation(summary = "Listar endereços", description = "Recupera todos os endereços do banco de dados")
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "Listar endereço por id", description = "Recupera um endereço do banco de dados atraves de seu id")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "Listar endereço por id do usuario", description = "Recupera um endereço do banco de dados atraves do id do usuario")
    public ResponseEntity<List<EnderecoDTO>> listarEnderecoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "Exibir um relatório paginado de endereços filtrados por Pais", description = "Relatório paginado de endereços filtrados por Pais")
    public PageDTO<EnderecoDTO> getRelatorioPaginadoPais(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) String pais);

    @EnderecoNotas
    @Operation(summary = "Alterar um endereço por id", description = "Altera os registros de um endereço no banco de dados atraves de seu id")
    public EnderecoDTO editar(@PathVariable("idEndereco") Integer id, @Valid @RequestBody EnderecoCreateDTO enderecoAtualizar) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "Deletar endereço", description = "Deleta um endereço do banco de dados atraves de seu id")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException;

    @EnderecoNotas
    @Operation(summary = "Exibir os endereços do próprio usuário", description = "Exibe uma lista com os endereços do usuário logado.")
    public List<UsuarioDTO> listarEnderecosUsuarioLogado() throws RegraDeNegocioException;

}
