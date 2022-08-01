package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.postagem.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.postagem.PostagemDTO;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPostagemDTO;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public PageDTO<PostagemDTO> list(Integer pagina, Integer quantRegistros) throws RegraDeNegocioException {
        PageRequest pageRequest = PageRequest.of(pagina, quantRegistros);
        Page<PostagemEntity> page = postagemRepository.findAll(pageRequest);
        List<PostagemDTO> postagensDTO = page.getContent().stream()
                .map(comentario -> objectMapper.convertValue(comentario, PostagemDTO.class))
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantRegistros, postagensDTO);
    }

    public PageDTO<RelatorioPostagemDTO> relatorioPostagem(TipoPostagem tipoPostagem, Integer pagina, Integer quantRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantRegistros);
        Page<RelatorioPostagemDTO> page = postagemRepository.relatorioPostagem(tipoPostagem, pageable);
        List<RelatorioPostagemDTO> relatorioPostagemDTOS = page.getContent().stream()
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantRegistros, relatorioPostagemDTOS);
    }

    public PostagemDTO findByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
        return objectMapper.convertValue(postagemRepository.findById(idPostagem), PostagemDTO.class);
    }

    public List<PostagemDTO> findByTitulo(String titulo){
         return postagemRepository.findAll().stream()
                .filter(postagem -> postagem.getTitulo().toLowerCase()
                        .contains(titulo.toLowerCase()))
                 .map(this::convertToDTO)
                 .toList();
    }


    public PageDTO<PostagemDTO> findByTipo(TipoPostagem tipoPostagem, Integer pagina, Integer quantRegistros) throws RegraDeNegocioException {
        PageRequest pageRequest = PageRequest.of(pagina, quantRegistros);
        Page<PostagemEntity> page = postagemRepository.filtrarPorTipo(tipoPostagem, pageRequest);
        List<PostagemDTO> postagensDTO = page.getContent().stream()
                .map(postagem -> objectMapper.convertValue(postagem, PostagemDTO.class))
                .toList();

        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantRegistros, postagensDTO);
    }

    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioValid = objectMapper.convertValue(usuarioRepository.findById(idUsuario), UsuarioEntity.class);

        PostagemEntity postagemEntity = convertToEntity(postagemCreateDTO);

        postagemEntity.setIdUsuario(idUsuario);
        postagemEntity.setCurtidas(0);
        postagemEntity.setData(LocalDateTime.now());
        postagemEntity.setUsuario(usuarioValid);

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

}
