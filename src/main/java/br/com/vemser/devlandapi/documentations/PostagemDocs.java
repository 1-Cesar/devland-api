package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.PostagemNotas;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

public interface PostagemDocs {

    @PostagemNotas
    @Operation(summary = "Lista paginada com todas as postagens")
    public ResponseEntity<PageDTO<PostagemDTO>> list(@RequestParam Integer pagina,@RequestParam Integer quantRegistros) throws RegraDeNegocioException;
    @PostagemNotas
    @Operation(summary = "Lista paginada com postagens por tipo", description = "Realizará a listagem de todas as postagens do banco de dados filtrando pelo tipo")
    public PageDTO<PostagemDTO> listByTipo(TipoPostagem tipoPostagem, Integer pagina, Integer quantRegistros) throws RegraDeNegocioException;

    @PostagemNotas
    @Operation(summary = "listagem por IdPostagem")
    public ResponseEntity<PostagemDTO> listByIdPostagem(@PathVariable("idPostagem") Integer idPostagem) throws RegraDeNegocioException;

    @PostagemNotas
    @Operation(summary = "Adicionar postagem", description = "Adicionará uma nova postagem ao banco de dados")
    public ResponseEntity<PostagemDTO> criar(@PathVariable("idUsuario") Integer idUsuario, @RequestBody @Valid PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException;


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
