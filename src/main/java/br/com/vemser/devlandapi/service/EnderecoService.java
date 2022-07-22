package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.EnderecoDTO;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.entity.EnderecoEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.EnderecoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if (enderecoRepository.findAll().size() == 0) {
            throw new RegraDeNegocioException("Nenhum endereço encontrado");
        } else {
            return enderecoRepository.findAll().stream()
                    .map(this::retornarDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<EnderecoDTO> listarEndereco(int id) throws RegraDeNegocioException {
        localizarEndereco(id);
        return enderecoRepository.findById(id).stream()
                .filter(endereco -> endereco.getIdEndereco().equals(id))
                .map(this::retornarDTO)
                .collect(Collectors.toList());
    }

    public List<EnderecoDTO> listarEnderecoUsuario(Integer id) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);
        return enderecoRepository.findAll().stream()
                .filter(endereco -> endereco.getUsuarios().contains(usuarioEntity))
                .map(this::retornarDTO)
                .toList();
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        EnderecoEntity enderecoEntity = localizarEndereco(id);
        enderecoRepository.delete(enderecoEntity);
    }

    public EnderecoDTO editar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {

        EnderecoEntity enderecoEntity = localizarEndereco(id);

        enderecoEntity = validaAlteracoes(enderecoEntity, enderecoCreateDTO);

        enderecoRepository.save(enderecoEntity);

        return retornarDTO(enderecoEntity);
    }

    public EnderecoCreateDTO adicionar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {

        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);

        EnderecoEntity enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);

        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(usuarioEntity);

        enderecoEntity.setUsuarios(usuarios);

        enderecoRepository.save(enderecoEntity);

        return retornarDTO(enderecoEntity);
    }

    public EnderecoEntity localizarEndereco (Integer idEndereco) throws RegraDeNegocioException {
        return enderecoRepository.findAll().stream()
                .filter(endereco -> endereco.getIdEndereco().equals(idEndereco))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Endereço não localizado"));
    }

    public EnderecoDTO retornarDTO (EnderecoEntity enderecoEntity) {
        return objectMapper.convertValue(enderecoEntity, EnderecoDTO.class);
    }

    public EnderecoEntity retornarEnderecoEntity (EnderecoCreateDTO enderecoDTO) {
        return objectMapper.convertValue(enderecoDTO, EnderecoEntity.class);
    }

    public EnderecoEntity validaAlteracoes(EnderecoEntity enderecoEntity, EnderecoCreateDTO enderecoCreateDTO) {

        enderecoEntity.setTipo(enderecoCreateDTO.getTipo());
        enderecoEntity.setLogradouro(enderecoCreateDTO.getLogradouro());
        enderecoEntity.setNumero(enderecoCreateDTO.getNumero());
        enderecoEntity.setComplemento(enderecoCreateDTO.getComplemento());
        enderecoEntity.setCep(enderecoCreateDTO.getCep());
        enderecoEntity.setCidade(enderecoCreateDTO.getCidade());
        enderecoEntity.setEstado(enderecoCreateDTO.getEstado());
        enderecoEntity.setPais(enderecoCreateDTO.getPais());

        return enderecoEntity;
    }

    public PageDTO<EnderecoDTO> paginacaoPais(String pais, Integer pagina, Integer quantidadeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros);
        Page<EnderecoEntity> page = enderecoRepository.paginacaoPais(pais, pageable);
        List<EnderecoDTO> enderecoDTOS = page.getContent().stream()
                .map(this::retornarDTO)
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeRegistros, enderecoDTOS);
    }
}
