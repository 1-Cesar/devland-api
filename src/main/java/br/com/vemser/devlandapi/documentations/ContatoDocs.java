package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.ContatoNotas;
import br.com.vemser.devlandapi.dto.contato.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface ContatoDocs {

    @ContatoNotas
    @Operation(summary = "listar todos os contatos por páginas", description = "Lista paginada de todos contatos")
    public PageDTO<ContatoDTO> listarPaginado(Integer pagina, Integer quantidadeRegistros) throws RegraDeNegocioException;

    @ContatoNotas
    @Operation(summary = "listar contato por id do contato", description = "recupera um contato do banco de dados atraves de seu id")
    //@GetMapping("/{idContato}")
    public ResponseEntity<List<ContatoDTO>> listarContato(@PathVariable("idContato") Integer id) throws RegraDeNegocioException;

    @ContatoNotas
    @Operation(summary = "listar contato por id do usuario", description = "recupera um contato do banco de dados atraves do id do usuario")
    public ResponseEntity<List<ContatoDTO>> listarContatoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @ContatoNotas
    @Operation(summary = "criar contato atraves do usuário logado", description = "cria um contato dentro do banco de dados com base no usuário logado")
    public ResponseEntity<ContatoDTO> adicionar(@Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException;

    @ContatoNotas
    @Operation(summary = "altera um contato de usuário logado por id", description = "altera os registros de um contato de usuário logado atraves do seu id de contato")
    public ResponseEntity<ContatoDTO> editar(@PathVariable("idContato") Integer id, @Valid @RequestBody ContatoDTO contatoAtualizar) throws RegraDeNegocioException;

    @ContatoNotas
    @Operation(summary = "deleta contato", description = "deleta um contato do banco de dados atraves de seu id")
    public void remover(@PathVariable("idContato") Integer id) throws RegraDeNegocioException;

    @ContatoNotas
    @Operation(summary = "Listar os contatos do prórpio usuário.", description = "Exibe os contatos do próprio usuário logado.")
    public List<ContatoDTO> listarContatosUsuarioLogado() throws RegraDeNegocioException;

}
