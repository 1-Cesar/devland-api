package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.PostagemNotas;
import br.com.vemser.devlandapi.dto.PostagemComentDTO;
import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface PostagemDocs {
    @PostagemNotas
    @Operation(summary = "Listar postagens por tipo", description = "Realizará a listagem de todas as postagens do banco de dados pelo tipo")
    public ResponseEntity<List<PostagemDTO>> litByTipo(@PathVariable("tipoPostagem") Integer tipoPostagem) throws RegraDeNegocioException;

    @PostagemNotas
    @Operation(summary = "listagem por IdPostagem")
    public ResponseEntity<PostagemComentDTO> listByIdPostagem(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException;

    @PostagemNotas
    @Operation(summary = "Adicionar postagem", description = "Adicionará uma nova postagem ao banco de dados")
    public ResponseEntity<PostagemDTO> post(@PathVariable("idUsuario") Integer idUsuario, @RequestBody @Valid PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException;


    @PostagemNotas
    @Operation(summary = "Adicionar curtida à postagem", description = "Adicionará uma curtida à postagem no banco de dados")
    public ResponseEntity<PostagemDTO> curtir(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException;

    @PostagemNotas
    @Operation(summary = "Atualizar postagem", description = "Realizará a atualização da postagem no banco de dados")
    public ResponseEntity<PostagemDTO> update(@PathVariable() Integer idPostagem,@RequestBody @Valid PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException;

    @PostagemNotas
    @Operation(summary = "Deletar postagem", description = "Deletará a postagem do banco de dados com base na sua identificação")
    public ResponseEntity<Void> delete(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException;

}
