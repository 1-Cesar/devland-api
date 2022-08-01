package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.SeguidorDocs;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.seguidor.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.seguidor.SeguidorDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.SeguidorService;
import br.com.vemser.devlandapi.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/seguidor")
@Validated
public class SeguidorController implements SeguidorDocs {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private SeguidorService seguidorService;

    @GetMapping("/{idUsuario}")
    public PageDTO<SeguidorDTO> listarSeguidores(@PathVariable("idUsuario") Integer id, Integer pagina, Integer quantidadeRegistros)
            throws RegraDeNegocioException {
        return seguidorService.listarSeguidores(id, pagina, quantidadeRegistros);
    }

    @GetMapping("/meus-seguidores")
    public PageDTO<SeguidorDTO> listarSeguidores(Integer pagina, Integer quantidadeRegistros)
            throws RegraDeNegocioException {
        return seguidorService.listarMeusSeguidores(pagina, quantidadeRegistros);
    }

    @PostMapping("/seguir-usuario")
    public ResponseEntity<SeguidorCreateDTO> adicionar(@Valid @RequestBody SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {
        log.info("Adicionando seguidor com base no id do usuário");
        return ResponseEntity.ok(seguidorService.adicionar(seguidorCreateDTO));
    }

    @DeleteMapping("/{idSeguidor}")
    public void delete(@PathVariable("idSeguidor") Integer idSeguidor) throws RegraDeNegocioException {
        log.info("Excluindo com base no id do usuario e id do seguidor");
        seguidorService.delete(idSeguidor);
    }
}
