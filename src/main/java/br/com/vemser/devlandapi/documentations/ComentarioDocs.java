package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.ComentarioNotas;
import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ComentarioDocs {
    @ComentarioNotas
    @Operation(summary = "Listar todos os comentários", description = "Listar todos os comentários sem filtro.")
    List<ComentarioDTO> list() throws RegraDeNegocioException;

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

}
