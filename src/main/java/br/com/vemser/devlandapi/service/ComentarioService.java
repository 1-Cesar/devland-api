package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.*;
import br.com.vemser.devlandapi.entity.ComentarioEntity;
import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ComentarioRepository;
import br.com.vemser.devlandapi.repository.PostagemRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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

    public PageDTO<ComentarioDTO> list(Integer pagina,Integer quantRegistros) throws RegraDeNegocioException {
        PageRequest pageRequest = PageRequest.of(pagina, quantRegistros);
        Page<ComentarioEntity> page = comentarioRepository.findAll(pageRequest);
        List<ComentarioDTO> comentariosDTO = page.getContent().stream()
                .map(comentario -> objectMapper.convertValue(comentario, ComentarioDTO.class))
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantRegistros, comentariosDTO);
    }

    public ComentarioDTO create(Integer idPostagem, Integer idUsuario,ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException{

        UsuarioEntity usuarioValid = convertOptionalToUsuarioEntity(usuarioRepository.findById(idUsuario));
        ComentarioEntity comentarioEntity = convertToEntity(comentarioCreateDTO);

        comentarioEntity.setIdPostagem(idPostagem);
        comentarioEntity.setIdUsuario(idUsuario);
        comentarioEntity.setUsuario(usuarioValid);
        comentarioEntity.setCurtidasComentario(0);
        comentarioEntity.setDataComentario(LocalDateTime.now());


        return convertToDTO(comentarioRepository.save(comentarioEntity));

    }

    public ComentarioDTO update (Integer idComentario,
                                 ComentarioCreateDTO comentarioCreateDTO) throws RegraDeNegocioException{
        ComentarioEntity comentarioValid = convertOptionalToComentarioEntity(comentarioRepository.findById(idComentario));
        System.out.println("ComentarioValid = "+comentarioValid);

        UsuarioEntity usuario = convertOptionalToUsuarioEntity(usuarioRepository.findById(comentarioValid.getIdUsuario()));

        //------------------------------------------------------------------------------------------------------------------
        PostagemEntity postagem = convertOptionalToPostagemEntity(postagemRepository.findById(comentarioValid.getIdPostagem()));

        comentarioValid.setPostagem(postagem);
        //------------------------------------------------------------------------------------------------------------------

        comentarioValid.setDescricaoComentarios(comentarioCreateDTO.getDescricao());
        comentarioValid.setUsuario(usuario);

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

    //-----------------------------------------------------------------------------------------------------------------
    public PostagemEntity convertOptionalToPostagemEntity(Optional postagem) {
        return objectMapper.convertValue(postagem, PostagemEntity.class);
    }
}
