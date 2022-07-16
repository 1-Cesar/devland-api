package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.entity.Postagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.PostagemRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
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
    private ObjectMapper objectMapper;

    private Date data = new java.sql.Date(System.currentTimeMillis());

    public List<PostagemDTO> list() throws RegraDeNegocioException {
        return postagemRepository.list().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PostagemDTO> listByTipo(Integer tipoPostagem) throws RegraDeNegocioException {
        return postagemRepository.listByTipo(tipoPostagem).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        if (usuarioRepository.listarUsuario(idUsuario).isEmpty()){
            throw new RegraDeNegocioException("Usuário não encontrado");
        }
        else {
            log.info("Adicionando postagem...");

            Postagem postagemEntity = convertToEntity(postagemCreateDTO);
            postagemEntity.setIdUsuario(idUsuario);
            postagemEntity.setUps(0);
            postagemEntity.setDowns(0);
            postagemEntity.setViews(0);
            postagemEntity.setData(data);

            postagemRepository.post(postagemEntity);

            log.info("Postagem criada...");

            return convertToDTO(postagemEntity);
        }
    }

    public PostagemDTO update(Integer idPostagem, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {

        Postagem postagemRecuperada = postagemRepository.findByIdPostagem(idPostagem);

        if(postagemRecuperada != null) {
            log.info("Atualizando postagem...");

            Postagem postagemEntity = convertToEntity(postagemCreateDTO);
            postagemEntity.setIdPostagem(postagemRecuperada.getIdPostagem());
            postagemEntity.setIdUsuario(postagemRecuperada.getIdUsuario());
            postagemEntity.setUps(postagemRecuperada.getUps());
            postagemEntity.setDowns(postagemRecuperada.getViews());
            postagemEntity.setViews(postagemRecuperada.getViews());
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

}
