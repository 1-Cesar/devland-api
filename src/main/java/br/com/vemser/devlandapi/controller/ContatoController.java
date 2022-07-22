package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.ContatoDocs;
import br.com.vemser.devlandapi.dto.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ContatoService;
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
@RequestMapping("/contato")
@Validated
public class ContatoController implements ContatoDocs {

    @Autowired
    private ContatoService contatoService;

    public ResponseEntity<List<ContatoDTO>> listar() throws RegraDeNegocioException {
        log.info("Listando todos os contatos");
        return ResponseEntity.ok(contatoService.listar());
    }

    public ResponseEntity<List<ContatoDTO>> listarContato(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um contato através de seu id");
        return ResponseEntity.ok(contatoService.listarContato(id));
    }


    public ResponseEntity<List<ContatoDTO>> listarContatoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um contato com base no id do usuário");
        return ResponseEntity.ok(contatoService.listarContatoUsuario(id));
    }


    public ResponseEntity<ContatoCreateDTO> adicionar(@PathVariable("idPessoa") Integer id,
                                                      @Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException {
        log.info("Criando um contato com base no id do usuário");
        return ResponseEntity.ok(contatoService.adicionar(id, contato));
    }


    public ResponseEntity<ContatoDTO> editar(@PathVariable("idContato") Integer id,
                                             @Valid @RequestBody ContatoDTO contatoAtualizar) throws RegraDeNegocioException {
        log.info("Alterando um contato com base em seu id");
        return ResponseEntity.ok(contatoService.editar(id, contatoAtualizar));
    }

    public void remover(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        log.info("Removendo um contato com base em seu id");
        contatoService.remover(id);
    }
}
