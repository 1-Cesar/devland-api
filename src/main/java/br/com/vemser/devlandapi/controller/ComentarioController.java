package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioRespDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ComentarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/comentario")
@Validated
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @Operation(summary = "Adicionar comentário", description = "Adicionará um novo comentário à banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Sucesso! Novo comentário adicionado com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Permissão negada! Você não possui permissão para utilizar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Erro! Durante a execução, foi gerada uma exceção")
            }
    )
    @PostMapping("/{idPostagem}")
    public ResponseEntity<ComentarioRespDTO> post(@PathVariable("idPostagem") Integer idPostagem,
                                                  @RequestBody @Valid ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
        return new ResponseEntity<ComentarioRespDTO>(comentarioService.post(idPostagem, comentarioCreateDTO), HttpStatus.OK);
    }

    @Operation(summary = "Deletar comentário", description = "Deletará o comentário do banco de dados com base na sua identificação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Sucesso! O comentário foi removido com sucesso"),
                    @ApiResponse(responseCode = "403", description = "Permissão negada! Você não possui permissão para utilizar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Erro! Durante a execução, foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{idComentario}")
    public ResponseEntity<Void> delete(@PathVariable("idComentario") Integer idComenatario) throws RegraDeNegocioException {
        comentarioService.delete(idComenatario);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
