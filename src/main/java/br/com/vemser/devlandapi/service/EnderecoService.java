package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.Endereco;
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

    public List<EnderecoDTO> listar() throws RegraDeNegocioException {
        try {
            return enderecoRepository.listar().stream()
                    .map(endereco -> objectMapper.convertValue(endereco, EnderecoDTO.class))
                    .collect(Collectors.toList());
        } catch (RegraDeNegocioException e) {
            throw new RegraDeNegocioException("Nenhum endereço encontrado");
        }
    }

    public List<EnderecoDTO> listarEndereco(Integer id) throws RegraDeNegocioException {
        try {
            return enderecoRepository.listarEndereco(id).stream()
                    .filter(endereco -> endereco.getIdEndereco().equals(id))
                    .map(endereco -> objectMapper.convertValue(endereco, EnderecoDTO.class))
                    .collect(Collectors.toList());
        } catch (RegraDeNegocioException e) {
            throw new RegraDeNegocioException("Nenhum usuario encontrado");
        }
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        enderecoRepository.remover(id);
    }

    public EnderecoDTO editar(Integer id, EnderecoDTO enderecoDTO) throws RegraDeNegocioException {
        Endereco endereco = enderecoRepository.editar(id, objectMapper.convertValue(enderecoDTO, Endereco.class));
        enderecoDTO =  objectMapper.convertValue(endereco, EnderecoDTO.class);
        return enderecoDTO;
    }

    public EnderecoCreateDTO adicionar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {

        Endereco endereco = enderecoRepository.adicionar(id, objectMapper.convertValue(enderecoCreateDTO, Endereco.class));

        return objectMapper.convertValue(endereco, EnderecoDTO.class);
    }
}
