package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.SeguidorDTO;
import br.com.vemser.devlandapi.entity.Seguidor;
import br.com.vemser.devlandapi.entity.Usuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.SeguidorRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeguidorService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private SeguidorRepository seguidorRepository;

    public SeguidorCreateDTO adicionar(Integer id, SeguidorCreateDTO seguidorCreateDTO) throws RegraDeNegocioException {
        Usuario usuarioLocalizado = localizarUsuario(seguidorCreateDTO.getIdSeguidor());

        seguidorCreateDTO.setIdUsuario(id);
        seguidorCreateDTO.setNomeSeguidor(usuarioLocalizado.getNome());

        if (!seguidorCreateDTO.getIdSeguidor().equals(id)) {
            localizarUsuario(seguidorCreateDTO.getIdSeguidor());
            if (seguidorRepository.VerificarSeguidor(seguidorCreateDTO.getIdSeguidor(), id)){
                throw new RegraDeNegocioException("Este seguidor já está seguindo");
            }
        } else {
            throw new RegraDeNegocioException("Você não pode seguir você mesmo");
        }

        Seguidor seguidor = seguidorRepository.adicionar(id, objectMapper.convertValue(seguidorCreateDTO, Seguidor.class));
        return objectMapper.convertValue(seguidor, SeguidorCreateDTO.class);
    }

    public List<SeguidorDTO> listarSeguidor(Integer id) throws RegraDeNegocioException {
        localizarUsuario(id);
        return seguidorRepository.listarSeguidor(id).stream()
                .filter(seguidor -> seguidor.getIdUsuario().equals(id))
                .map(seguidor -> objectMapper.convertValue(seguidor, SeguidorDTO.class))
                .collect(Collectors.toList());
    }

    public void delete(Integer id, Integer idSeguidor) throws RegraDeNegocioException {
        localizarUsuario(id);
        seguidorRepository.remover(id, idSeguidor);
    }

    public Usuario localizarUsuario (Integer idUsuario) throws RegraDeNegocioException {
        Usuario usuarioRecuperado = usuarioRepository.listar().stream()
                .filter(usuario -> usuario.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        return usuarioRecuperado;
    }
}
