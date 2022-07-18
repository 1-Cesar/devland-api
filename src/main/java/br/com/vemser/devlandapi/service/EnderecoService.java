package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.Endereco;
import br.com.vemser.devlandapi.entity.Usuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.EnderecoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnderecoService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<EnderecoDTO> listar() throws RegraDeNegocioException {
        if (enderecoRepository.listar().size() == 0) {
            throw new RegraDeNegocioException("Nenhum endereço encontrado");
        } else {
            return enderecoRepository.listar().stream()
                    .map(endereco -> objectMapper.convertValue(endereco, EnderecoDTO.class))
                    .collect(Collectors.toList());
        }
    }

    public List<EnderecoDTO> listarEndereco(Integer id) throws RegraDeNegocioException {
        localizarEndereco(id);
        return enderecoRepository.listarEndereco(id).stream()
                .filter(endereco -> endereco.getIdEndereco().equals(id))
                .map(endereco -> objectMapper.convertValue(endereco, EnderecoDTO.class))
                .collect(Collectors.toList());
    }

    public List<EnderecoDTO> listarEnderecoUsuario(Integer id) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        return enderecoRepository.listarEnderecoUsuario(id).stream()
                .filter(endereco -> endereco.getIdUsuario().equals(id))
                .map(endereco -> objectMapper.convertValue(endereco, EnderecoDTO.class))
                .collect(Collectors.toList());
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        localizarEndereco(id);
        enderecoRepository.remover(id);
    }

    public EnderecoDTO editar(Integer id, EnderecoDTO enderecoDTO) throws RegraDeNegocioException {
        Endereco enderecoRecuperado = localizarEndereco(id);
        enderecoDTO.setIdUsuario(enderecoRecuperado.getIdUsuario());

        enderecoRecuperado = enderecoRepository.editar(id, objectMapper.convertValue(enderecoDTO, Endereco.class));
        enderecoDTO =  objectMapper.convertValue(enderecoRecuperado, EnderecoDTO.class);
        return enderecoDTO;
    }

    public EnderecoCreateDTO adicionar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        Endereco endereco = enderecoRepository.adicionar(id, objectMapper.convertValue(enderecoCreateDTO, Endereco.class));

        return objectMapper.convertValue(endereco, EnderecoDTO.class);
    }

    public Endereco localizarEndereco (Integer idEndereco) throws RegraDeNegocioException {
        Endereco enderecoRecuperado = enderecoRepository.listar().stream()
                .filter(endereco -> endereco.getIdEndereco().equals(idEndereco))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Endereco não encontrado"));
        return enderecoRecuperado;
    }
}
