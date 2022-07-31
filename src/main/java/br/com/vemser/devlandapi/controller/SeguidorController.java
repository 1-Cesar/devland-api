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

    @PostMapping("/{idUsuario}")
    public ResponseEntity<SeguidorCreateDTO> adicionar(@PathVariable("idUsuario") Integer id,
                                                       @Valid @RequestBody SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {
        log.info("Adicionando seguidor com base no id do usuário");
        return ResponseEntity.ok(seguidorService.adicionar(id, seguidorCreateDTO));
    }

    @DeleteMapping("/{idUsuario}/{idSeguidor}")
    public void delete(@PathVariable("idUsuario") Integer id, @PathVariable("idSeguidor") Integer idSeguidor) throws RegraDeNegocioException {
        log.info("Excluindo com base no id do usuario e id do seguidor");
        seguidorService.delete(id, idSeguidor);
    }
}
