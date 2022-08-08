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
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================

    public List<ContatoDTO> listarContatoUsuarioLogado() throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = usuarioLogadoEntity.getIdUsuario();

        usuarioService.localizarUsuario(id);
        return contatoRepository.findAll().stream()
                .filter(contato -> contato.getIdUsuario().equals(id))
                .map(this::retornarContatoDTO)
                .collect(Collectors.toList());
    }

    //------------------------------------------------------------------------------------------------------------------

    public ContatoDTO adicionar(ContatoCreateDTO contatoDTO) throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(id);

        //converte
        ContatoEntity contatoEntity = retornarContatoEntity(contatoDTO);

        //seta no usuário
        contatoEntity.setUsuario(usuarioRecuperado);


        return retornarContatoDTO(contatoRepository.save(contatoEntity));
    }

    //------------------------------------------------------------------------------------------------------------------

    public ContatoDTO editar(Integer id,
                             ContatoDTO contatoDTO) throws RegraDeNegocioException {
        //Buscando se contato existe
        ContatoEntity contatoRecuperado = localizarContato(id);

        //Buscando usuário logado
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer idUsuarioLogado = (Integer) usuarioLogadoEntity.getIdUsuario();

        //Verificando se o contato pertence ao usuário
        ContatoEntity verificaContatoUsuarioLogado = localizarContatoUsuarioLogado(id, idUsuarioLogado);

        //Buscando os dados do usuário
        UsuarioEntity usuarioContato = usuarioService.localizarUsuario(idUsuarioLogado);

        //Setando as alterações
        contatoRecuperado.setTipo(contatoDTO.getTipo());
        contatoRecuperado.setNumero(contatoDTO.getNumero());
        contatoRecuperado.setDescricao(contatoDTO.getDescricao());
        contatoRecuperado.setUsuario(usuarioContato);

        return retornarContatoDTO(contatoRepository.save(contatoRecuperado));
    }

    //------------------------------------------------------------------------------------------------------------------

    public void remover(Integer id) throws RegraDeNegocioException {

        ContatoEntity contatoEntityRecuperado = localizarContato(id);

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer idUsuarioLogado = (Integer) usuarioLogadoEntity.getIdUsuario();

        // UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(contatoEntityRecuperado.getIdUsuario());

        //Verificando se o contato pertence ao usuário
        ContatoEntity verificaContatoUsuarioLogado = localizarContatoUsuarioLogado(id, idUsuarioLogado);

        contatoRepository.delete(contatoEntityRecuperado);
    }


    //==================================================================================================================
    //                                             MÉTODOS AUXILIARES
    //==================================================================================================================

    //LOCALIZAR CONTATO
    public ContatoEntity localizarContato(Integer idContato) throws RegraDeNegocioException {
        return contatoRepository.findById(idContato)
                .orElseThrow(() -> new RegraDeNegocioException("Contato não encontrado"));
    }

    //DTO PARA ENTITY
    public ContatoEntity retornarContatoEntity(ContatoCreateDTO contatoCreateDTO) {
        return objectMapper.convertValue(contatoCreateDTO, ContatoEntity.class);
    }

    //ENTITY PARA DTO
    public ContatoDTO retornarContatoDTO(ContatoEntity contato) {
        return objectMapper.convertValue(contato, ContatoDTO.class);
    }

    public ContatoEntity localizarContatoUsuarioLogado(Integer idContato, Integer idUsuario) throws RegraDeNegocioException {
        return contatoRepository.findByIdContatoAndIdUsuario(idContato, idUsuario);
    }

}
