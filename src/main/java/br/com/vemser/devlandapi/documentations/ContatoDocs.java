package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.ComentarioNotas;
import br.com.vemser.devlandapi.dto.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface ContatoDocs {

    @ComentarioNotas
    @Operation(summary = "listar todos os contatos por páginas", description = "recupera todos os contatos do banco de dados e os exibe em páginas")
    public PageDTO<ContatoDTO> listarPaginado(Integer pagina, Integer quantidadeRegistros) throws RegraDeNegocioException;
/*
------------------------------------------------------------------------------------------------------------------------

    SUBSTITUÍDO POR GET PAGINADO

    @ComentarioNotas
    @Operation(summary = "listar todos os contatos", description = "recupera todos os contatos do banco de dados")
    public ResponseEntity<List<ContatoDTO>> listar() throws RegraDeNegocioException;


------------------------------------------------------------------------------------------------------------------------
*/
    @ComentarioNotas
    @Operation(summary = "listar contato por id do contato", description = "recupera um contato do banco de dados atraves de seu id")
    //@GetMapping("/{idContato}")
    public ResponseEntity<List<ContatoDTO>> listarContato(@PathVariable("idContato") Integer id) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "listar contato por id do usuario", description = "recupera um contato do banco de dados atraves do id do usuario")
    public ResponseEntity<List<ContatoDTO>> listarContatoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "criar contato atraves do id do usuario", description = "cria um contato dentro do banco de dados com base no id do usuario")
    public ResponseEntity<ContatoCreateDTO> adicionar(@PathVariable("idPessoa") Integer id, @Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "altera um contato por id", description = "altera os registros de um contato no banco de dados atraves de seu id")
    public ResponseEntity<ContatoDTO> editar(@PathVariable("idContato") Integer id, @Valid @RequestBody ContatoDTO contatoAtualizar) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "deleta contato", description = "deleta um contato do banco de dados atraves de seu id")
    public void remover(@PathVariable("idContato") Integer id) throws RegraDeNegocioException;

}
