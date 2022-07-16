package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.PostagemDTO;
import br.com.vemser.devlandapi.entity.Postagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.PostagemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@Slf4j
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Date data = new java.sql.Date(System.currentTimeMillis());


    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        log.info("Adicionando postagem...");

        Postagem postagemEntity =  convertToEntity(postagemCreateDTO);
        postagemEntity.setIdUsuario(idUsuario);
        postagemEntity.setUps(0);
        postagemEntity.setDowns(0);
        postagemEntity.setViews(0);
        postagemEntity.setData(data);

        postagemRepository.post(postagemEntity);

        log.info("Postagem criada...");

        return convertToDTO(postagemEntity);

    }

    public PostagemDTO update(Integer idPostagem, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        log.info("Atualizando postagem...");

        Postagem postagemRecuperada = postagemRepository.findByIdPostagem(idPostagem);

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

    public Postagem convertToEntity(PostagemCreateDTO postagemCreateDTO) {
        return objectMapper.convertValue(postagemCreateDTO, Postagem.class);
    }

    public PostagemDTO convertToDTO(Postagem postagem) {
        return objectMapper.convertValue(postagem, PostagemDTO.class);
    }
}
