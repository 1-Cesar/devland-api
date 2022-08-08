package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.EnderecoDocs;
import br.com.vemser.devlandapi.dto.*;
import br.com.vemser.devlandapi.dto.endereco.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.EnderecoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/endereco")
@Validated
public class EnderecoController implements EnderecoDocs {

    @Autowired
    private EnderecoService enderecoService;

    @GetMapping
    public ResponseEntity<List<EnderecoDTO>> listarTodos() throws RegraDeNegocioException {
        log.info("Listando todos os endereços");
        return ResponseEntity.ok(enderecoService.listar());
    }

    @GetMapping("/{idEndereco}")
    public ResponseEntity<List<EnderecoDTO>> listarEndereco(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        log.info("Recuperando um endereço com base em seu id");
        return ResponseEntity.ok(enderecoService.listarEndereco(id));
    }

    @GetMapping("usuario/{idUsuario}")
    public ResponseEntity<List<EnderecoDTO>> listarEnderecoUsuario(@PathVariable("idUsuario") Integer idUsuario) throws RegraDeNegocioException {
        log.info("Recuperando uma lista de endereços com base no id do usuário");
        return ResponseEntity.ok(enderecoService.listarEnderecoUsuario(idUsuario));
    }

    @DeleteMapping("/{idEndereco}")
    public void delete(@PathVariable("idEndereco") Integer id) throws RegraDeNegocioException {
        log.info("Excluindo um endereço com base em seu id");
        enderecoService.delete(id);
    }

    @GetMapping("/paginacao-pais")
    public PageDTO<EnderecoDTO> getRelatorioPaginadoPais(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) String pais) {
        return enderecoService.paginacaoPais(pais, pagina, quantidadeRegistros);
    }

    //==================================================================================================================
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================

    @GetMapping("/listar-seus-enderecos")
    public List<UsuarioDTO> listarEnderecosUsuarioLogado() throws RegraDeNegocioException {
        return enderecoService.listarEnderecosUsuarioLogado();
    }

    @PutMapping("/editar-se/{idEndereco}")
    public EnderecoDTO editar(@PathVariable("idEndereco") Integer idEndereco,
                              @Valid @RequestBody EnderecoCreateDTO enderecoAtualizar) throws RegraDeNegocioException {
        log.info("Modificando um endereço com base em seu id");
        return enderecoService.editarProprio(idEndereco, enderecoAtualizar);
    }

    @DeleteMapping("/deletar-se/{idEndereco}")
    public String deletar(@PathVariable("idEndereco") Integer idEndereco) throws RegraDeNegocioException {
        return enderecoService.deletarProprio(idEndereco);
    }

    @PostMapping("/adicionar-meu-endereco")
    public ResponseEntity<EnderecoDTO> adicionar(@Valid @RequestBody EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        log.info("Criando um endereço com base no usuário logado");
        return ResponseEntity.ok(enderecoService.adicionarProprio(enderecoCreateDTO));
    }
}
