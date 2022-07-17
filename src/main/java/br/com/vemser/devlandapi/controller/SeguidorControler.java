package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.SeguidorDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.SeguidorService;
import br.com.vemser.devlandapi.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/seguidor")
@Validated
public class SeguidorControler {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SeguidorService seguidorService;


    @Operation(summary = "recupera os seguidores com base no id do usuário", description = "recupera os seguidores presentes no banco de dados com base no id do usuário")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Recupera seguidor por id de usuário"),
                    @ApiResponse(responseCode = "400", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<SeguidorDTO>> listarSeguidores(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando seguidores com base no id do usuário");
        return ResponseEntity.ok(seguidorService.listarSeguidor(id));
    }

    @Operation(summary = "Adiciona seguidor com base no id do usuário", description = "Adiciona seguidor no banco de dados com base no id do usuário")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Adiciona seguidor por id de usuário"),
                    @ApiResponse(responseCode = "400", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping("/{idUsuario}")
    public ResponseEntity<SeguidorCreateDTO> adicionar(@PathVariable("idUsuario") Integer id,
                                                      @Valid @RequestBody SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {
        log.info("Adicionando seguidor com base no id do usuário");
        return ResponseEntity.ok(seguidorService.adicionar(id, seguidorCreateDTO));
    }

    @Operation(summary = "Deleta seguidor com base no id do usuário e id do seguidor", description = "Deleta seguidor presente no banco de dados com base no id do usuário e id do seguidor")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Deleta seguidor"),
                    @ApiResponse(responseCode = "400", description = "Usuário não encontrado"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
   @DeleteMapping("/{idUsuario}/{idSeguidor}")
    public void delete(@PathVariable("idUsuario") Integer id, @PathVariable("idSeguidor") Integer idSeguidor) throws RegraDeNegocioException {
       log.info("Excluindo com base no id do usuario e id do seguidor");
        seguidorService.delete(id, idSeguidor);
    }
}
