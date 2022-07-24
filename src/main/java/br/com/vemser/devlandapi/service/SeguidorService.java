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
                .map(this::retornarSeguidorDTO)//converte dto através de método
                .collect(Collectors.toList());
    }

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

    //DTO PARA ENTITY
    public SeguidorEntity retornarSeguidorEntity(SeguidorCreateDTO seguidorCreateDTO) {
        return objectMapper.convertValue(seguidorCreateDTO, SeguidorEntity.class);
    }

    //ENTITY PARA DTO
    public SeguidorDTO retornarSeguidorDTO(SeguidorEntity seguidor) {
        return objectMapper.convertValue(seguidor, SeguidorDTO.class);
    }
}


/*
    public SeguidorEntity verificaSeSegue (Integer id, Integer idSeguidor) throws RegraDeNegocioException {
        //SeguidorEntity seguidorRecuperado = seguidorRepository.findById(id).stream()
        SeguidorEntity seguidorRecuperado = seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getId().equals(id) && seguidor.getIdSeguidor().equals(idSeguidor))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Seguidor ja segue devia deixar a porra do codigo continuar"));

        return seguidorRecuperado;

    }
*/
    /*

    //Confere se já existe seguidor com mesmo id
    public SeguidorEntity verificaSeguidor (Integer idUsuario) throws RegraDeNegocioException {
        SeguidorEntity seguidorRecuperado = seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getId().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Seguidor não encontrado"));
        return seguidorRecuperado;
    }


    public SeguidorEntity verificaSeguidorSeguidoPorAlguem (Integer idSeguidor) throws RegraDeNegocioException {
        SeguidorEntity seguidorRecuperado = seguidorRepository.findAll().stream()
                .filter(seguidor -> seguidor.getIdSeguidor().equals(idSeguidor))
                .findFirst()
             .orElseThrow();
        return seguidorRecuperado;
    }

*/
