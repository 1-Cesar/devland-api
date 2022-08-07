package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.ContatoDocs;
import br.com.vemser.devlandapi.dto.contato.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.ContatoService;
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


    @GetMapping
    public PageDTO<ContatoDTO> listarPaginado(Integer pagina, Integer quantidadeRegistros) {
        return contatoService.listarPaginado(pagina, quantidadeRegistros);
    }

    @GetMapping("/{idContato}")
    public ResponseEntity<List<ContatoDTO>> listarContato(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um contato através de seu id");
        return ResponseEntity.ok(contatoService.listarContatoPorId(id));
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ContatoDTO>> listarContatoUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um contato com base no id do usuário");
        return ResponseEntity.ok(contatoService.listarContatoUsuario(id));
    }

    //==================================================================================================================
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================

    @GetMapping("/listar-se")
    public List<ContatoDTO> listarContatosUsuarioLogado() throws RegraDeNegocioException {
        return contatoService.listarContatoUsuarioLogado();
    }

    @PostMapping("/adicionar-se")
    public ResponseEntity<ContatoDTO> adicionar(@Valid @RequestBody ContatoCreateDTO contato) throws RegraDeNegocioException {
        log.info("Criando um contato com base no usuário logado");
        return ResponseEntity.ok(contatoService.adicionar(contato));
    }

    @PutMapping("/editar-se/{idContato}")
    public ResponseEntity<ContatoDTO> editar(@PathVariable("idContato") Integer id,
                                             @Valid @RequestBody ContatoDTO contatoAtualizar) throws RegraDeNegocioException {
        log.info("Alterando contato de usuário logado com base em seu idContato");
        return ResponseEntity.ok(contatoService.editar(id, contatoAtualizar));
    }

    @DeleteMapping("/deletar-se/{idContato}")
    public void remover(@PathVariable("idContato") Integer id) throws RegraDeNegocioException {
        log.info("Removendo um contato com base em seu id");
        contatoService.remover(id);
    }
}

