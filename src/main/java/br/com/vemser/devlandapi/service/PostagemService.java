package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.dto.PostagemComentDTO;
import br.com.vemser.devlandapi.entity.PostagemEntity;
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

    private UsuarioRepository usuarioRepository;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private String strLocalDateTime;

    public List<PostagemDTO> list() throws RegraDeNegocioException {
        if (postagemRepository.findAll().isEmpty()){
            throw new RegraDeNegocioException("Nenhuma postagem encontrada");
        }
        else {
            return postagemRepository.findAll().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public PostagemDTO findByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
        return (PostagemDTO) postagemRepository.findById(idPostagem).stream().map(this::convertToDTO).toList();
    }


    public List<PostagemDTO> listByTipo(TipoPostagem tipoPostagem){
         return postagemRepository.filtrarPorTipo(tipoPostagem).stream()
                 .map(this::convertToDTO)
                 .toList();
    }

    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        if (usuarioRepository.findById(idUsuario).isEmpty()){
            throw new RegraDeNegocioException("Usuário não encontrado");
        }
        else {
            PostagemEntity postagemEntity = convertToEntity(postagemCreateDTO);

            postagemEntity.setIdUsuario(idUsuario);
            postagemEntity.setCurtidas(0);
            postagemEntity.setData(LocalDateTime.now());

            postagemRepository.save(postagemEntity);

            return convertToDTO(postagemEntity);
        }
    }

    public PostagemDTO curtir(Integer idPostagem) throws RegraDeNegocioException {

        PostagemEntity postagemEntityRecuperada = convertOptionalToEntity(postagemRepository.findById(idPostagem));

        if(postagemEntityRecuperada == null) {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
        else {

            postagemEntityRecuperada.setCurtidas(postagemEntityRecuperada.getCurtidas() + 1);

            postagemRepository.save(postagemEntityRecuperada);

            return convertToDTO(postagemEntityRecuperada);
        }
    }

    public PostagemDTO update(Integer idPostagem, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        PostagemEntity postagemEntityRecuperada = convertOptionalToEntity(postagemRepository.findById(idPostagem));

        if(postagemEntityRecuperada != null) {
            log.info("Atualizando postagem...");

            PostagemEntity postagemEntity = convertToEntity(postagemCreateDTO);
            postagemEntity.setIdPostagem(postagemEntityRecuperada.getIdPostagem());
            postagemEntity.setIdUsuario(postagemEntityRecuperada.getIdUsuario());
            postagemEntity.setCurtidas(postagemEntityRecuperada.getCurtidas());
            postagemEntity.setData(postagemEntityRecuperada.getData());

            postagemRepository.save(postagemEntity);

            log.info("PostagemEntity atualizada...");

            return convertToDTO(postagemEntity);
        }
        else {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
    }

    public void delete(Integer idPostagem) throws RegraDeNegocioException {
        PostagemEntity postagemEntityRecuperada = convertOptionalToEntity(postagemRepository.findById(idPostagem));

        if (postagemEntityRecuperada != null) {
            log.info("Deletando postagem...");

            postagemRepository.delete(postagemEntityRecuperada);
        }
        else {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
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

    public PostagemComentDTO convertToComentDTO(PostagemEntity postagemEntity) {
        return objectMapper.convertValue(postagemEntity, PostagemComentDTO.class);
    }
}
