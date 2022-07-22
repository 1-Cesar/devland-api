package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.SeguidorNotas;
import br.com.vemser.devlandapi.dto.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.SeguidorDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface SeguidorDocs {
    @SeguidorNotas
    @Operation(summary = "recupera os seguidores com base no id do usuário", description = "recupera os seguidores presentes no banco de dados com base no id do usuário")
    public ResponseEntity<List<SeguidorDTO>> listarSeguidores(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @SeguidorNotas
    @Operation(summary = "Adiciona seguidor com base no id do usuário", description = "Adiciona seguidor no banco de dados com base no id do usuário")
    public ResponseEntity<SeguidorCreateDTO> adicionar(@PathVariable("idUsuario") Integer id, @Valid @RequestBody SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException;

    @SeguidorNotas
    @Operation(summary = "Deleta seguidor com base no id do usuário e id do seguidor", description = "Deleta seguidor presente no banco de dados com base no id do usuário e id do seguidor")
    public void delete(@PathVariable("idUsuario") Integer id, @PathVariable("idSeguidor") Integer idSeguidor) throws RegraDeNegocioException;


}
