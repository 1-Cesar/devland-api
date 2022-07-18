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

    @Autowired
    private UsuarioService usuarioService;

    public List<ContatoDTO> listar() throws RegraDeNegocioException {
        if (contatoRepository.listar().size() == 0) {
            throw new RegraDeNegocioException("Nenhum contato encontrado");
        } else {
            return contatoRepository.listar().stream()
                    .map(contato -> objectMapper.convertValue(contato, ContatoDTO.class))
                    .collect(Collectors.toList());
        }
    }

    public List<ContatoDTO> listarContato(Integer id) throws RegraDeNegocioException {
        localizarContato(id);
        return contatoRepository.listarContato(id).stream()
                .filter(contato -> contato.getIdContato().equals(id))
                .map(contato -> objectMapper.convertValue(contato, ContatoDTO.class))
                .collect(Collectors.toList());
    }

    public List<ContatoDTO> listarContatoUsuario(Integer id) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        return contatoRepository.listarContatoUsuario(id).stream()
                .filter(contato -> contato.getIdUsuario().equals(id))
                .map(contato -> objectMapper.convertValue(contato, ContatoDTO.class))
                .collect(Collectors.toList());
    }

    public ContatoCreateDTO adicionar(Integer id, ContatoCreateDTO contato) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        Contato idContato = contatoRepository.adicionar(id, objectMapper.convertValue(contato, Contato.class));
        return objectMapper.convertValue(idContato, ContatoDTO.class);
    }

    public ContatoDTO editar(Integer id,
                             ContatoDTO contatoDTO) throws RegraDeNegocioException {
        Contato contatoRecuperado = localizarContato(id);
        contatoDTO.setIdUsuario(contatoRecuperado.getIdUsuario());

        contatoRecuperado = contatoRepository.editar(id, objectMapper.convertValue(contatoDTO, Contato.class));
        contatoDTO =  objectMapper.convertValue(contatoRecuperado, ContatoDTO.class);
        return contatoDTO;
    }

    public void remover(Integer id) throws RegraDeNegocioException {
        localizarContato(id);
        contatoRepository.remover(id);
    }

    public Contato localizarContato (Integer idContato) throws RegraDeNegocioException {
        Contato contatoRecuperado = contatoRepository.listar().stream()
                .filter(contato -> contato.getIdContato().equals(idContato))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Contato não encontrado"));
        return contatoRecuperado;
    }
}
