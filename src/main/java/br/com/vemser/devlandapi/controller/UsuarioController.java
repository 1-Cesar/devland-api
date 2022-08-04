package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.UsuarioDocs;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPersonalizadoDevDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.UserLoginService;
import br.com.vemser.devlandapi.service.UsuarioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usuario")
@Validated
public class UsuarioController implements UsuarioDocs {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserLoginService userLoginService;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> listarTodos() throws RegraDeNegocioException {
        log.info("Listando todos os usuários");
        return ResponseEntity.ok(usuarioService.listar());
    }

    @GetMapping("/{idUsuario}/byId")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um usuário com base em seu id");
        return ResponseEntity.ok(usuarioService.listarUsuario(id));
    }

    @GetMapping("/byname") // localhost:8080/pessoa/byname?nome=Rafa
    public ResponseEntity<List<UsuarioDTO>> listarPorNome(@RequestParam("nome") String nome) throws RegraDeNegocioException {
        log.info("Recuperando um usuário através do nome");

        return ResponseEntity.ok(usuarioService.listarPorNome(nome));
    }


    @DeleteMapping("/{idUsuario}")
    public void delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Deletando um usuário com base em seu id");
        usuarioService.delete(id);
    }

    @GetMapping("/relatorio-stack-usuario")
    public PageDTO<RelatorioPersonalizadoDevDTO> getUsuarioByStack(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) String stack) {
        return usuarioService.relatorioStack(stack, pagina, quantidadeRegistros);
    }

    @GetMapping("/relatorio-genero-usuario")
    public PageDTO<RelatorioPersonalizadoDevDTO> getUsuarioByGenero(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) Genero genero) {
        return usuarioService.relatorioGenero(genero, pagina, quantidadeRegistros);
    }

    //==================================================================================================================
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================
    @GetMapping("/listar-se")
    public List<UsuarioDTO> listarProprio() throws RegraDeNegocioException {
        return usuarioService.listarProprio();
    }

    @PutMapping("/editar-se")
    public UsuarioDTO editarProprio(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        return usuarioService.editarProprio(usuarioCreateDTO);
    }
}
