package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.enums.TipoStatus;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.QuantidadeUsuarioService;
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
@RequestMapping("/logs")
public class LogsController {

    @Autowired
    private QuantidadeUsuarioService quantidadeUsuarioService;

    /*@GetMapping
    public ResponseEntity<String> listarQtdDevs() {
        log.info("exibindo qtd de devs");
        return ResponseEntity.ok(quantidadeUsuarioService.retornarQtdDevs());
    }*/

    /*@GetMapping
    public ResponseEntity<String> listarQtdEmpresas() {
        log.info("exibindo qtd de empresas");
        return ResponseEntity.ok(quantidadeUsuarioService.retornarQtdEmpresas());
    }*/

    @GetMapping("Log-Quantidade-Usuario")
    public String logQuantidade(TipoUsuario tipoUsuario) throws RegraDeNegocioException {
        log.info("exibindo qtd de usuario");
        return quantidadeUsuarioService.retornarQtdUsuario(tipoUsuario);
    }
}
