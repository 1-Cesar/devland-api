package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.SeguidorDTO;
import br.com.vemser.devlandapi.entity.ContatoEntity;
import br.com.vemser.devlandapi.entity.SeguidorEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.SeguidorRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
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
public class SeguidorService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SeguidorRepository seguidorRepository;

    //==================================================================================================================
    //LIST FOLLOWERS - PAGINADO
    public PageDTO<SeguidorDTO> listarSeguidores(Integer pagina,
                                               Integer registroPorPagina, Integer id) throws RegraDeNegocioException {
        localizarUsuario(id);
        Pageable pageable = PageRequest.of(pagina, registroPorPagina);
        Page<SeguidorEntity> page = seguidorRepository.findAll(pageable);

        List<SeguidorDTO> seguidorDTOS = page.getContent().stream()
                .filter(seguidor -> seguidor.getIdUsuario().equals(id))
                .map(this::retornarSeguidorDTO)
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, registroPorPagina, seguidorDTOS);
    }

/*
    //==================================================================================================================
    //LIST FOLLOWERS
    public List<SeguidorDTO> listarSeguidor(Integer id) throws RegraDeNegocioException {
        localizarUsuario(id);
        return seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getIdUsuario().equals(id))
                .map(this::retornarSeguidorDTO)//converte dto através de método
                .collect(Collectors.toList());
    }
*/
    //==================================================================================================================
    //ADICIONAR

    public SeguidorCreateDTO adicionar(Integer id, SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {

        //recupera usuário
        UsuarioEntity usuarioRecuperado = localizarUsuario(id);

        //converte
        SeguidorEntity seguidorEntity = retornarSeguidorEntity(seguidorCreateDTO);

        //seta no usuário
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

    public SeguidorEntity seguidorQueSeraDeletado (Integer id, Integer idSeguidor) throws RegraDeNegocioException {
        SeguidorEntity seguidorRecuperado = seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getIdUsuario().equals(id) && seguidor.getIdSeguidor().equals(idSeguidor))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Seguidor não encontrado para deixar de seguir"));

        return seguidorRecuperado;

    }

    public UsuarioEntity localizarUsuario (Integer idUsuario) throws RegraDeNegocioException {
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

