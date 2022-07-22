package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.ComentarioNotas;
import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioRespDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface ComentarioDocs {
    @ComentarioNotas
    @Operation(summary = "Adicionar comentário", description = "Adicionará um novo comentário à banco de dados")
    public ResponseEntity<ComentarioRespDTO> post(@PathVariable("idPostagem") Integer idPostagem,@RequestBody @Valid ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException;

    @ComentarioNotas
    @Operation(summary = "Deletar comentário", description = "Deletará o comentário do banco de dados com base na sua identificação")
    public ResponseEntity<Void> delete(@PathVariable("idComentario") Integer idComenatario) throws RegraDeNegocioException;

}
