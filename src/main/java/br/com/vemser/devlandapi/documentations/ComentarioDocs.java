package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.ComentarioNotas;
import br.com.vemser.devlandapi.dto.comentario.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.comentario.ComentarioDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ComentarioDocs {
    @ComentarioNotas
    @Operation(summary = "Lista paginada de todos os comentários", description = "Listar todos os comentários sem filtro.")
    public PageDTO<ComentarioDTO> list(@RequestParam Integer pagina, @RequestParam Integer quantRegistros) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "Adicionar um comentário", description = "Adicionará um novo comentário ao banco de dados.")
    ResponseEntity<ComentarioDTO> create(@PathVariable("idPostagem") Integer idPostagem,
                                         @PathVariable("idUsuario") Integer idUsuario,
                                         @RequestBody ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "Editar um comentário", description = "Edita um comentário especificado por seu id.")
    public ResponseEntity<ComentarioDTO> update(Integer idPostagem, ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "Deletar comentário", description = "Deletará do banco de dados um comentário especificado pelo id.")
    public ResponseEntity<Void> delete(Integer idComenatario) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "Listar os comentários por Postagem", description = "Exibe uma lista de comentários filtrados por postagem.")
    public ResponseEntity<List<ComentarioDTO>> listByPostagem(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException;

}
