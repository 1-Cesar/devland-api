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

import java.util.List;

@Service
@Slf4j
public class ComentarioService {

    @Autowired
    private PostagemRepository postagemRepository;

//    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ComentarioDTO> list() throws RegraDeNegocioException {
        return comentarioRepository.findAll().stream()
                .map(this::convertToDTO).toList();
    }

//    public ComentarioDTO post(Integer idPostagem, ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException {
//
//        if (postagemRepository.findByIdPostagem(idPostagem) == null) {
//            throw new RegraDeNegocioException("PostagemEntity não encontrada");
//        } else if (usuarioRepository.listarUsuario(comentarioCreateDTO.getIdUsuario()).isEmpty()) {
//            throw new RegraDeNegocioException("Usuário não encontrado");
//        } else {
//            log.info("Adicionando comentário...");
//
//            Usuario usuario = usuarioRepository.listarUsuario(comentarioCreateDTO.getIdUsuario()).get(0);
//
//            String strLocalDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
//
//            ComentarioEntity comentarioEntity = convertToEntity(comentarioCreateDTO);
//            comentarioEntity.setIdPostagem(idPostagem);
//            comentarioEntity.setCurtidas(0);
//            comentarioEntity.setData(strLocalDateTime);
//
//            comentarioRepository.post(comentarioEntity);
//
//            log.info("Comentário criado...");
//
//            UsuarioDTO usuarioDTO = convertUsuarioDTO(usuario);
//
//            ComentarioDTO comentarioDTO = convertToDTO(comentarioEntity);
//
//            comentarioDTO.setUsuario(usuarioDTO);
//
//            return comentarioDTO;
//        }
//    }
//
//    public void delete(Integer idComentario) throws RegraDeNegocioException {
//
//        if (comentarioRepository.findById(idComentario) != null) {
//            log.info("Deletando comentário...");
//
//            postagemRepository.delete(idComentario);
//
//            log.info("PostagemEntity removida...");
//        }
//        else {
//            throw new RegraDeNegocioException("Comentário não encontrado");
//        }
//    }


    public ComentarioEntity convertToEntity(ComentarioCreateDTO comentarioCreateDTO) {
        return objectMapper.convertValue(comentarioCreateDTO, ComentarioEntity.class);
    }

    public ComentarioDTO convertToDTO(ComentarioEntity comentarioEntity) {
        return objectMapper.convertValue(comentarioEntity, ComentarioDTO.class);
    }

    public UsuarioDTO convertUsuarioDTO(UsuarioEntity usuario) {
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

}
