package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.ComentarioDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.ComentarioEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ComentarioRepository;
import br.com.vemser.devlandapi.repository.PostagemRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ComentarioService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ComentarioDTO> list() throws RegraDeNegocioException {
        return comentarioRepository.findAll().stream()
                .map(this::convertToDTO).toList();
    }

    public ComentarioDTO create(Integer idPostagem, Integer idUsuario,ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException{

        UsuarioEntity usuarioValid = convertOptionalToUsuarioEntity(usuarioRepository.findById(idUsuario));
        ComentarioEntity comentarioEntity = convertToEntity(comentarioCreateDTO);

        comentarioEntity.setIdPostagem(idPostagem);
        comentarioEntity.setIdUsuario(idUsuario);
        comentarioEntity.setUsuarioEntity(usuarioValid);
        comentarioEntity.setCurtidas(0);
        comentarioEntity.setData(LocalDateTime.now());


       return convertToDTO(comentarioRepository.save(comentarioEntity));

    }

    public ComentarioDTO update (Integer idComentario,
                                 ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException{

        ComentarioEntity comentarioValid = convertOptionalToComentarioEntity(comentarioRepository.findById(idComentario));
        UsuarioEntity usuario = convertOptionalToUsuarioEntity(usuarioRepository.findById(comentarioValid.getIdUsuario()));

        comentarioValid.setDescricao(comentarioCreateDTO.getDescricao());
        comentarioValid.setUsuarioEntity(usuario);

        System.out.println("ComentarioValid = "+comentarioValid);

        comentarioRepository.save(comentarioValid);
        return convertToDTO(comentarioValid);
    }

    public void delete ( Integer idComentario){
        ComentarioEntity comentarioValid = convertOptionalToComentarioEntity(comentarioRepository.findById(idComentario));
        comentarioRepository.delete(comentarioValid);
    }

//    ========================= CONVERSÕES =========================

    public ComentarioEntity convertToEntity(ComentarioCreateDTO comentarioCreateDTO) {
        return objectMapper.convertValue(comentarioCreateDTO, ComentarioEntity.class);
    }

    public ComentarioDTO convertToDTO(ComentarioEntity comentarioEntity) {
        return objectMapper.convertValue(comentarioEntity, ComentarioDTO.class);
    }

    public UsuarioDTO convertUsuarioDTO(UsuarioEntity usuario) {
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

    public UsuarioEntity convertOptionalToUsuarioEntity(Optional usuario) {
        return objectMapper.convertValue(usuario, UsuarioEntity.class);
    }

    public ComentarioEntity convertOptionalToComentarioEntity(Optional comentario) {
        return objectMapper.convertValue(comentario, ComentarioEntity.class);
    }

}
