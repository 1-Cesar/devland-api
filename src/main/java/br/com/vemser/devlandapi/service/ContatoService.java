package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.entity.Contato;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ContatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public List<ContatoDTO> listar() throws RegraDeNegocioException {
        try {
            return contatoRepository.listar().stream()
                    .map(contato -> objectMapper.convertValue(contato, ContatoDTO.class))
                    .collect(Collectors.toList());
        } catch (RegraDeNegocioException e) {
            throw new RegraDeNegocioException("Nenhum contato encontrado");
        }
    }

    public ContatoCreateDTO adicionar(Integer id, ContatoCreateDTO contato) throws RegraDeNegocioException {
        Contato idContato = contatoRepository.adicionar(id, objectMapper.convertValue(contato, Contato.class));
        return objectMapper.convertValue(idContato, ContatoDTO.class);
    }

    public ContatoDTO editar(Integer id,
                             ContatoDTO contatoDTO) throws RegraDeNegocioException {
        Contato contato = contatoRepository.editar(id, objectMapper.convertValue(contatoDTO, Contato.class));
        contatoDTO =  objectMapper.convertValue(contato, ContatoDTO.class);
        return contatoDTO;
    }

    public void remover(Integer id) throws RegraDeNegocioException {
        contatoRepository.remover(id);
    }
}
