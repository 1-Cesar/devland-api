package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.SeguidorNotas;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.SeguidorDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

public interface SeguidorDocs {
    /*
    @SeguidorNotas
    @Operation(summary = "recupera os seguidores com base no id do usuário", description = "recupera os seguidores presentes no banco de dados com base no id do usuário")
    public ResponseEntity<List<SeguidorDTO>> listarSeguidores(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;
*/

    @SeguidorNotas
    @Operation(summary = "lista os seguidores com base no id do usuário em páginas", description = "recupera os seguidores presentes no banco de dados com base no id do usuário e exibe em páginas")
    public PageDTO<SeguidorDTO> listarSeguidores(Integer pagina, Integer quantidadeRegistros, @PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;


    @SeguidorNotas
    @Operation(summary = "Adiciona seguidor com base no id do usuário", description = "Adiciona seguidor no banco de dados com base no id do usuário")
    public ResponseEntity<SeguidorCreateDTO> adicionar(@PathVariable("idUsuario") Integer id, @Valid @RequestBody SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException;

    @SeguidorNotas
    @Operation(summary = "Deleta seguidor com base no id do usuário e id do seguidor", description = "Deleta seguidor presente no banco de dados com base no id do usuário e id do seguidor")
    public void delete(@PathVariable("idUsuario") Integer id, @PathVariable("idSeguidor") Integer idSeguidor) throws RegraDeNegocioException;


}
