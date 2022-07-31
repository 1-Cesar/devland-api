package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.contato.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.entity.ContatoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ContatoRepository;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserLoginService userLoginService;


    //==================================================================================================================
    //LISTAR TUDO - PAGINADO

    public PageDTO<ContatoDTO> listarPaginado(Integer pagina,
                                              Integer registroPorPagina) {
        Pageable pageable = PageRequest.of(pagina, registroPorPagina);
        Page<ContatoEntity> page = contatoRepository.findAll(pageable);

        List<ContatoDTO> contatoDTOS = page.getContent().stream()
                .map(this::retornarContatoDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registroPorPagina, contatoDTOS);
    }

    //==================================================================================================================
    //LIST CONTATO POR ID

    public List<ContatoDTO> listarContatoPorId(Integer id) throws RegraDeNegocioException {
        localizarContato(id);
        return contatoRepository.findById(id).stream()
                .filter(contato -> contato.getIdContato().equals(id))
                .map(this::retornarContatoDTO)//converte dto através de método
                .collect(Collectors.toList());
    }

    //==================================================================================================================
    //LIST CONTATO USUÁRIO

    public List<ContatoDTO> listarContatoUsuario(Integer id) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        return contatoRepository.findAll().stream()
                .filter(contato -> contato.getIdUsuario().equals(id))
                .map(this::retornarContatoDTO)
                .collect(Collectors.toList());
    }

    //==================================================================================================================
    //ADICIONAR

    public ContatoCreateDTO adicionar(Integer id, ContatoCreateDTO contatoDTO) throws RegraDeNegocioException {
        //recupera usuário
        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(id);

        //converte
        ContatoEntity contatoEntity = retornarContatoEntity(contatoDTO);

        //seta no usuário
        contatoEntity.setUsuario(usuarioRecuperado);

        ContatoEntity contatoCriado = contatoRepository.save(contatoEntity);

        return retornarContatoDTO(contatoCriado);
    }

    //==================================================================================================================
    //EDITAR

    public ContatoDTO editar(Integer id,
                             ContatoDTO contatoDTO) throws RegraDeNegocioException {
        ContatoEntity contatoRecuperado = localizarContato(id);

        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(contatoDTO.getIdUsuario());

        contatoRecuperado.setTipo(contatoDTO.getTipo());
        contatoRecuperado.setNumero(contatoDTO.getNumero());
        contatoRecuperado.setDescricao(contatoDTO.getDescricao());
        contatoRecuperado.setUsuario(usuarioRecuperado);

        return retornarContatoDTO(contatoRepository.save(contatoRecuperado));
    }

    //==================================================================================================================
    //EXCLUIR
    public void remover(Integer id) throws RegraDeNegocioException {

        ContatoEntity contatoEntityRecuperado = localizarContato(id);
        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(contatoEntityRecuperado.getIdUsuario());

        // log
        contatoRepository.delete(contatoEntityRecuperado);
    }

    //==================================================================================================================
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================

    public List<ContatoDTO> listarContatoUsuarioLogado() throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        usuarioService.localizarUsuario(id);
        return contatoRepository.findAll().stream()
                .filter(contato -> contato.getIdUsuario().equals(id))
                .map(this::retornarContatoDTO)
                .collect(Collectors.toList());
    }

    //TODO - ADICIONAR CONTATO NO USUÁRIO QUE ESTÁ LOGADO

    public ContatoCreateDTO adicionar(ContatoCreateDTO contatoDTO) throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(id);

        //converte
        ContatoEntity contatoEntity = retornarContatoEntity(contatoDTO);

        //seta no usuário
        contatoEntity.setUsuario(usuarioRecuperado);

        ContatoEntity contatoCriado = contatoRepository.save(contatoEntity);

        return retornarContatoDTO(contatoCriado);
    }















    //TODO - EDITAR CONTATO EM CONTATOS DO USUÁRIO LOGADO



    //------------------------------------------------------------------------------------------------------------------
    //TODO - DELETAR CONTATO EM CONTATOS DO USUARIO LOGADO


    //==================================================================================================================
    //                                             MÉTODOS AUXILIARES
    //==================================================================================================================

    //LOCALIZAR CONTATO
    public ContatoEntity localizarContato(Integer idContato) throws RegraDeNegocioException {
        ContatoEntity contatoRecuperado = contatoRepository.findById(idContato).stream()
                .filter(contato -> contato.getIdContato().equals(idContato))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Contato não encontrado"));
        return contatoRecuperado;
    }

    //DTO PARA ENTITY
    public ContatoEntity retornarContatoEntity(ContatoCreateDTO contatoCreateDTO) {
        return objectMapper.convertValue(contatoCreateDTO, ContatoEntity.class);
    }

    //ENTITY PARA DTO
    public ContatoDTO retornarContatoDTO(ContatoEntity contato) {
        return objectMapper.convertValue(contato, ContatoDTO.class);
    }

}
