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
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostagemService {

    @Autowired
    private PostagemRepository postagemRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Date data = new java.sql.Date(System.currentTimeMillis());


    public List<PostagemDTO> getAll() throws RegraDeNegocioException {
        return postagemRepository.getAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostagemDTO getByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
        Postagem postagemRecuperada = postagemRepository.findByIdPostagem(idPostagem);

        if (postagemRecuperada != null) {
            return convertToDTO(postagemRecuperada);
        } else {
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
    }

    public List<PostagemDTO> getByTipo(Integer tipoPostagem) throws RegraDeNegocioException {
        return postagemRepository.getByTipo(tipoPostagem).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO) throws RegraDeNegocioException {
        //verificar o idUsuario
        
        log.info("Adicionando postagem...");
        Postagem postagemEntity =  convertToEntity(postagemCreateDTO);
        postagemEntity.getUsuario().setIdUsuario(idUsuario);
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
        if(postagemRecuperada != null) {

            Postagem postagemEntity = convertToEntity(postagemCreateDTO);
            postagemEntity.setIdPostagem(postagemRecuperada.getIdPostagem());
            postagemEntity.getUsuario().setIdUsuario(postagemRecuperada.getUsuario().getIdUsuario());
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
        log.info("Deletando postagem...");

        if (postagemRepository.findByIdPostagem(idPostagem) != null) {
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
