package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.Usuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioCreateDTO adicionar(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        Usuario usuario = usuarioRepository.adicionar(objectMapper.convertValue(usuarioCreateDTO, Usuario.class));
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }
}
