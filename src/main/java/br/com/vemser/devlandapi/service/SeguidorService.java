package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.seguidor.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.seguidor.SeguidorDTO;
import br.com.vemser.devlandapi.entity.SeguidorEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.SeguidorRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SeguidorService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SeguidorRepository seguidorRepository;

    @Autowired
    private UserLoginService userLoginService;

    //==================================================================================================================
    //LIST FOLLOWERS - PAGINADO
    public PageDTO<SeguidorDTO> listarSeguidores(Integer id, Integer pagina,
                                                 Integer registroPorPagina) throws RegraDeNegocioException {
        localizarUsuario(id);
        PageRequest pageRequest = PageRequest.of(pagina, registroPorPagina);
        Page<SeguidorEntity> page = seguidorRepository.filtrarQuemUsuarioSegue(id, pageRequest);

        List<SeguidorDTO> seguidorDTOS = page.getContent().stream()
                .map(this::retornarSeguidorDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registroPorPagina, seguidorDTOS);
    }

    public PageDTO<SeguidorDTO> listarMeusSeguidores(Integer pagina,
                                                 Integer registroPorPagina) throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        localizarUsuario(id);
        PageRequest pageRequest = PageRequest.of(pagina, registroPorPagina);
        Page<SeguidorEntity> page = seguidorRepository.filtrarQuemUsuarioSegue(id, pageRequest);

        List<SeguidorDTO> seguidorDTOS = page.getContent().stream()
                .map(this::retornarSeguidorDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registroPorPagina, seguidorDTOS);
    }

    //==================================================================================================================
    //ADICIONAR

    public SeguidorCreateDTO adicionar(SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioRecuperado = localizarUsuario(id);

        UsuarioEntity seguidorRecuperado = localizarUsuario(seguidorCreateDTO.getIdSeguidor());

        SeguidorEntity novoSeguidor = new SeguidorEntity();

        novoSeguidor.setNomeSeguidor(usuarioRecuperado.getNome());
        novoSeguidor.setIdUsuario(usuarioRecuperado.getIdUsuario());
        //recupera usuário

        List<SeguidorEntity> seguidorEntities = new ArrayList<>();
        seguidorEntities.add(novoSeguidor);

        seguidorRecuperado.setSeguidores(seguidorEntities);

        //converte
        SeguidorEntity seguidorEntity = retornarSeguidorEntity(seguidorCreateDTO);

        //seta no usuário
        seguidorEntity.setNomeSeguidor(seguidorRecuperado.getNome());
        seguidorEntity.setUsuario(usuarioRecuperado);


        if (id.equals(seguidorCreateDTO.getIdSeguidor())) {
            throw new RegraDeNegocioException("Nao pode seguir voce mesmo");
        } else if (seguidorRepository.verificaSeguidor(id, seguidorCreateDTO.getIdSeguidor()).size() > 0) {
            throw new RegraDeNegocioException("Você já segue este usuario");
        }

        SeguidorEntity seguidorCriado = seguidorRepository.save(seguidorEntity);

        return retornarSeguidorDTO(seguidorCriado);

    }

    //==================================================================================================================
    //EXCLUIR

    public void delete(Integer id, Integer idSeguidor) throws RegraDeNegocioException {
        localizarUsuario(id);

        SeguidorEntity seguidorEntityRecuperado = seguidorQueSeraDeletado(id, idSeguidor);

        seguidorRepository.delete(seguidorEntityRecuperado);
    }

    //==================================================================================================================
    //MÉTODOS AUXILIARES

    public SeguidorEntity seguidorQueSeraDeletado(Integer id, Integer idSeguidor) throws RegraDeNegocioException {
        SeguidorEntity seguidorRecuperado = seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getIdUsuario().equals(id) && seguidor.getIdSeguidor().equals(idSeguidor))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Seguidor não encontrado para deixar de seguir"));

        return seguidorRecuperado;

    }

    public UsuarioEntity localizarUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioRecuperado = usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        return usuarioRecuperado;
    }

    //DTO PARA ENTITY
    public SeguidorEntity retornarSeguidorEntity(SeguidorCreateDTO seguidorCreateDTO) {
        return objectMapper.convertValue(seguidorCreateDTO, SeguidorEntity.class);
    }

    //ENTITY PARA DTO
    public SeguidorDTO retornarSeguidorDTO(SeguidorEntity seguidor) {
        return objectMapper.convertValue(seguidor, SeguidorDTO.class);
    }
}

