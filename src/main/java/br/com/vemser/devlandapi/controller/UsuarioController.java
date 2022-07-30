package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.UsuarioDocs;
import br.com.vemser.devlandapi.dto.*;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usuario")
@Validated
public class UsuarioController implements UsuarioDocs {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() throws RegraDeNegocioException {
        log.info("Listando todos os usuários");
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um usuário com base em seu id");
        return ResponseEntity.ok(usuarioService.listarUsuario(id));
    }

    @GetMapping("/byname") // localhost:8080/pessoa/byname?nome=Rafa
    public ResponseEntity<List<UsuarioDTO>> listarPorNome(@RequestParam("nome") String nome) throws RegraDeNegocioException {
       log.info("REcuperando um usuário através do nome");

        return ResponseEntity.ok(usuarioService.listarPorNome(nome));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> adicionar(@Valid @RequestBody UsuarioCreateDTO usuarioCreateDTO, @Valid @RequestBody UserLoginCreateDTO userLoginCreateDTO) throws RegraDeNegocioException {
        log.info("Criando um usuário");
        return ResponseEntity.ok(usuarioService.adicionar(usuarioCreateDTO));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> editar(@PathVariable("idUsuario") Integer id,
                                             @Valid @RequestBody UsuarioCreateDTO usuarioAtualizar) throws RegraDeNegocioException {
        log.info("Alterando um usuário com base em seu id");
        return ResponseEntity.ok(usuarioService.editar(id, usuarioAtualizar));
    }

    @DeleteMapping("/{idUsuario}")
    public void delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Deletando um usuário com base em seu id");
        usuarioService.delete(id);
    }

    @GetMapping("/paginacao-tipo-usuario")
    public PageDTO<UsuarioDTO> getUsuarioByTipo(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) TipoUsuario tipoUsuario) {
        return usuarioService.paginacaoTipo(tipoUsuario, pagina, quantidadeRegistros);
    }

    @GetMapping("/relatorio-stack-usuario")
    public PageDTO<RelatorioPersonalizadoDevDTO> getUsuarioByGenero(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) String stack) {
        return usuarioService.relatorioStack(stack, pagina, quantidadeRegistros);
    }

    @GetMapping("/relatorio-genero-usuario")
    public PageDTO<RelatorioPersonalizadoDevDTO> getUsuarioByGenero(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) Genero genero) {
        return usuarioService.relatorioGenero(genero, pagina, quantidadeRegistros);
    }
}
