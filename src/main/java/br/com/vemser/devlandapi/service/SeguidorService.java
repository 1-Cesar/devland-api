package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.SeguidorDTO;
import br.com.vemser.devlandapi.entity.SeguidorEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.SeguidorRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    //LIST FOLLOWERS
    public List<SeguidorDTO> listarSeguidor(Integer id) throws RegraDeNegocioException {
        localizarUsuario(id);
        return seguidorRepository.findById(id).stream()
                .filter(seguidor -> seguidor.getIdUsuario().equals(id))
                .map(this::retornarSeguidorDTO)
                .collect(Collectors.toList());
    }

    //==================================================================================================================
    //ADICIONAR

    public SeguidorCreateDTO adicionar(Integer id, SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioLocalizado = localizarUsuario(seguidorCreateDTO.getIdSeguidor());

        seguidorCreateDTO.setIdUsuario(id);
        seguidorCreateDTO.setNomeSeguidor(usuarioLocalizado.getNome());

        if (!seguidorCreateDTO.getIdSeguidor().equals(id)) {
            localizarUsuario(seguidorCreateDTO.getIdSeguidor());

           // SeguidorEntity seguidorJaSegue = verificaSeguidor(id); todo verificar regras de negócio

            //if ( usuarioLocalizado.getIdUsuario() .equals(seguidorJaSegue.getIdSeguidor())) {
            //    throw new RegraDeNegocioException("Este seguidor já está seguindo");
            //}
            // if (verificaSeguidor(seguidorCreateDTO.getIdSeguidor(), id))   {
            //    throw new RegraDeNegocioException("Este seguidor já está seguindo");
            // }

        } else {
            throw new RegraDeNegocioException("Você não pode seguir você mesmo");
        }

        //SeguidorEntity seguidor = seguidorRepository.adicionar(id, objectMapper.convertValue(seguidorCreateDTO, SeguidorEntity.class));
        //return objectMapper.convertValue(seguidor, SeguidorCreateDTO.class);

        SeguidorEntity seguidorEntity = retornarSeguidorEntity(seguidorCreateDTO);
        seguidorEntity.setUsuario(usuarioLocalizado);
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
                .filter(seguidor -> seguidor.getId().equals(id) && seguidor.getIdSeguidor().equals(idSeguidor))
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


    //Confere se já existe seguidor com mesmo id
    public SeguidorEntity verificaSeguidor (Integer idUsuario) throws RegraDeNegocioException {
        SeguidorEntity seguidorRecuperado = seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getId().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Seguidor não encontrado"));
        return seguidorRecuperado;
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
