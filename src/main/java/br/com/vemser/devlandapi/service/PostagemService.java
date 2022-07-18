package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.ComentarioRespDTO;
import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.dto.PostagemComentDTO;
import br.com.vemser.devlandapi.entity.Comentario;
import br.com.vemser.devlandapi.entity.Postagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ComentarioRepository;
import br.com.vemser.devlandapi.repository.PostagemRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        if (postagemRepository.list().isEmpty()){
            throw new RegraDeNegocioException("Nenhuma postagem encontrada");
        }
        else {
            return postagemRepository.list().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<PostagemDTO> listByTipo(Integer tipoPostagem) throws RegraDeNegocioException {
        if (postagemRepository.listByTipo(tipoPostagem).isEmpty()){
            throw new RegraDeNegocioException("Nenhuma postagem encontrada");
        }
        else {
            return postagemRepository.listByTipo(tipoPostagem).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        }
    }

    public PostagemComentDTO listById(Integer idPostagem) throws RegraDeNegocioException {
        Postagem postagemRecuperada = postagemRepository.findByIdPostagem(idPostagem);

        if (postagemRecuperada == null) {
            throw new RegraDeNegocioException("Postagem não encontrada");
        } else {

            List<Comentario> comentariosRecuperados = comentarioRepository.listByIdPostagem(idPostagem);

            List<ComentarioRespDTO> comentarioRespDTO = comentariosRecuperados.stream()
                    .map(comentario -> comentarioService.convertToDTO(comentario))
                    .collect(Collectors.toList());

            PostagemComentDTO postagemComentDTO = convertToComentDTO(postagemRecuperada);
            postagemComentDTO.setComentarios(comentarioRespDTO);

            return postagemComentDTO;
        }
    }

    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        if (usuarioRepository.listarUsuario(idUsuario).isEmpty()){
            throw new RegraDeNegocioException("Usuário não encontrado");
        }
        else {
            log.info("Adicionando postagem...");

            Postagem postagemEntity = convertToEntity(postagemCreateDTO);
            postagemEntity.setIdUsuario(idUsuario);
            postagemEntity.setCurtidas(0);
            postagemEntity.setData(LocalDateTime.now());

            postagemRepository.post(postagemEntity);

            log.info("Postagem criada...");

            return convertToDTO(postagemEntity);
        }
    }

    public PostagemDTO curtir(Integer idPostagem) throws RegraDeNegocioException {

        Postagem postagemRecuperada = postagemRepository.findByIdPostagem(idPostagem);

        if(postagemRecuperada == null) {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
        else {

            postagemRecuperada.setCurtidas(postagemRecuperada.getCurtidas() + 1);

            postagemRepository.curtir(postagemRecuperada);

            return convertToDTO(postagemRecuperada);
        }
    }

    public PostagemDTO update(Integer idPostagem, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        Postagem postagemRecuperada = postagemRepository.findByIdPostagem(idPostagem);

        if(postagemRecuperada != null) {
            log.info("Atualizando postagem...");

            Postagem postagemEntity = convertToEntity(postagemCreateDTO);
            postagemEntity.setIdPostagem(postagemRecuperada.getIdPostagem());
            postagemEntity.setIdUsuario(postagemRecuperada.getIdUsuario());
            postagemEntity.setCurtidas(postagemRecuperada.getCurtidas());
            postagemEntity.setData(postagemRecuperada.getData());

            postagemRepository.update(idPostagem, postagemEntity);

            log.info("Postagem atualizada...");

            return convertToDTO(postagemEntity);
        }
        else {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
    }

    public void delete(Integer idPostagem) throws RegraDeNegocioException {

        if (postagemRepository.findByIdPostagem(idPostagem) != null) {
            log.info("Deletando postagem...");

            postagemRepository.delete(idPostagem);

            log.info("Postagem removida...");
        }
        else {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
    }

    public Postagem convertToEntity(PostagemCreateDTO postagemCreateDTO) {
        return objectMapper.convertValue(postagemCreateDTO, Postagem.class);
    }

    public PostagemDTO convertToDTO(Postagem postagem) {
        return objectMapper.convertValue(postagem, PostagemDTO.class);
    }

    public PostagemComentDTO convertToComentDTO(Postagem postagem) {
        return objectMapper.convertValue(postagem, PostagemComentDTO.class);
    }
}
