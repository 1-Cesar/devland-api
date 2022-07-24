package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
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
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String strLocalDateTime;

    public List<PostagemDTO> list() throws RegraDeNegocioException {
        if (postagemRepository.findAll().isEmpty()) {
            throw new RegraDeNegocioException("Nenhuma postagem encontrada");
        } else {
            return postagemRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .toList();
        }
    }

    public PostagemDTO findByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
        return (PostagemDTO) postagemRepository.findById(idPostagem).stream().map(this::convertToDTO).toList();
    }


    public List<PostagemDTO> listByTipo(TipoPostagem tipoPostagem) {
        return postagemRepository.filtrarPorTipo(tipoPostagem).stream()
                .map(this::convertToDTO)
                .toList();
    }

    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioValid = objectMapper.convertValue(usuarioRepository.findById(idUsuario), UsuarioEntity.class);

        PostagemEntity postagemEntity = convertToEntity(postagemCreateDTO);

        postagemEntity.setIdUsuario(idUsuario);
        postagemEntity.setCurtidas(0);
        postagemEntity.setData(LocalDateTime.now());

        postagemRepository.save(postagemEntity);

        return convertToDTO(postagemEntity);
    }

    public PostagemDTO curtir(Integer idPostagem) throws RegraDeNegocioException {

        PostagemEntity postagemEntityValid = convertOptionalToEntity(postagemRepository.findById(idPostagem));

        if (postagemEntityValid == null) {
            throw new RegraDeNegocioException("Postagem não encontrada");
        } else {

            postagemEntityValid.setCurtidas(postagemEntityValid.getCurtidas() + 1);

            postagemRepository.save(postagemEntityValid);

            return convertToDTO(postagemEntityValid);
        }
    }

    public PostagemDTO update(Integer idPostagem, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        PostagemEntity postagemEntityValid = convertOptionalToEntity(postagemRepository.findById(idPostagem));

        PostagemEntity postagemEntityPersist = convertToEntity(postagemCreateDTO);
        postagemEntityPersist.setIdPostagem(postagemEntityValid.getIdPostagem());
        postagemEntityPersist.setIdUsuario(postagemEntityValid.getIdUsuario());
        postagemEntityPersist.setCurtidas(postagemEntityValid.getCurtidas());
        postagemEntityPersist.setData(postagemEntityValid.getData());

        postagemRepository.save(postagemEntityPersist);

        return convertToDTO(postagemEntityPersist);
    }

    public void delete(Integer idPostagem) throws RegraDeNegocioException {
        PostagemEntity postagemEntityValid = convertOptionalToEntity(postagemRepository.findById(idPostagem));

        postagemRepository.delete(postagemEntityValid);
    }

    public PostagemEntity convertOptionalToEntity(Optional postagemCreateDTO) {
        return objectMapper.convertValue(postagemCreateDTO, PostagemEntity.class);
    }

    public PostagemDTO convertOptionalToDTO(Optional postagemCreateDTO) {
        return objectMapper.convertValue(postagemCreateDTO, PostagemDTO.class);
    }

    public PostagemEntity convertToEntity(PostagemCreateDTO postagemCreateDTO) {
        return objectMapper.convertValue(postagemCreateDTO, PostagemEntity.class);
    }

    public PostagemDTO convertToDTO(PostagemEntity postagemEntity) {
        return objectMapper.convertValue(postagemEntity, PostagemDTO.class);
    }

//    public PostagemComentDTO convertToComentDTO(PostagemEntity postagemEntity) {
//        return objectMapper.convertValue(postagemEntity, PostagemComentDTO.class);
//    }
}
