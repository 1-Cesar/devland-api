package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.EnderecoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.EnderecoRepository;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EnderecoService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserLoginService userLoginService;


    public List<EnderecoDTO> listar() throws RegraDeNegocioException {
        if (enderecoRepository.findAll().size() == 0) {
            throw new RegraDeNegocioException("Nenhum endereço encontrado");
        } else {
            return enderecoRepository.findAll().stream()
                    .map(enderecoEntity -> {
                        EnderecoDTO enderecoDTO = retornarDTO(enderecoEntity);
                        enderecoDTO.setUsuarios(enderecoEntity.getUsuarios().stream()
                                .map(usuarioEntity -> objectMapper.convertValue(usuarioEntity, UsuarioDTO.class))
                                .toList());
                        return enderecoDTO;
                    }).toList();
        }
    }

    public List<EnderecoDTO> listarEndereco(int idEndereco) throws RegraDeNegocioException {
//        localizarEndereco(id);
        return enderecoRepository.findById(idEndereco).stream()
                .map(enderecoEntity -> {
                    EnderecoDTO enderecoDTO = retornarDTO(enderecoEntity);
                    enderecoDTO.setUsuarios(enderecoEntity.getUsuarios().stream()
                            .map(usuarioEntity -> objectMapper.convertValue(usuarioEntity, UsuarioDTO.class))
                            .toList());
                    return enderecoDTO;
                }).toList();
    }

    //=========================================================================================
    //OBS método alterado para retornar somente a lista de endereços do usuario informado, sem retornar o proprio usuario
    public List<EnderecoDTO> listarEnderecoUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuario).get();
        List<EnderecoDTO> enderecoDTOS = usuarioEntity.getEnderecos().stream().map(this::retornarDTO).toList();
        return enderecoDTOS;
    }
//=========================================================================================

    public void delete(Integer id) throws RegraDeNegocioException {
        EnderecoEntity enderecoEntity = enderecoRepository.findById(id).get();
        enderecoRepository.delete(enderecoEntity);
    }

    public EnderecoDTO adicionar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);
        EnderecoEntity enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);
        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(usuarioEntity);
        enderecoEntity.setUsuarios(usuarios);
        enderecoRepository.save(enderecoEntity);
        return retornarDTO(enderecoEntity);
    }


//    public EnderecoEntity validaAlteracoes(EnderecoEntity enderecoEntity, EnderecoCreateDTO enderecoCreateDTO) {
//
//        enderecoEntity.setTipo(enderecoCreateDTO.getTipo());
//        enderecoEntity.setLogradouro(enderecoCreateDTO.getLogradouro());
//        enderecoEntity.setNumero(enderecoCreateDTO.getNumero());
//        enderecoEntity.setComplemento(enderecoCreateDTO.getComplemento());
//        enderecoEntity.setCep(enderecoCreateDTO.getCep());
//        enderecoEntity.setCidade(enderecoCreateDTO.getCidade());
//        enderecoEntity.setEstado(enderecoCreateDTO.getEstado());
//        enderecoEntity.setPais(enderecoCreateDTO.getPais());
//
//        return enderecoEntity;
//    }

    public PageDTO<EnderecoDTO> paginacaoPais(String pais, Integer pagina, Integer quantidadeRegistros) {
        Sort ordenacao = Sort.by("pais");
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros, ordenacao);
        Page<EnderecoEntity> page = enderecoRepository.paginacaoPais(pais, pageable);
        List<EnderecoDTO> enderecoDTOS = page.getContent().stream()
                .map(this::retornarDTO)
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeRegistros, enderecoDTOS);
    }

    //==================================================================================================================
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================

    public List<UsuarioDTO> listarEnderecosUsuarioLogado() throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        Integer id = usuarioLogadoEntity.getIdUsuario();
        return usuarioRepository.findById(id).stream()
                .map(pessoaEntity -> {
                    UsuarioDTO usuarioDTO = objectMapper.convertValue(pessoaEntity, UsuarioDTO.class);
                    usuarioDTO.setEnderecoDTOS(pessoaEntity.getEnderecos().stream()
                            .map(enderecoEntity -> retornarDTO(enderecoEntity))
                            .toList());
                    return usuarioDTO;
                }).toList();
    }

    public EnderecoDTO adicionarProprio(EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        Integer id = usuarioLogadoEntity.getIdUsuario();
        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);
        EnderecoEntity enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);
        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(usuarioEntity);
        enderecoEntity.setUsuarios(usuarios);
        enderecoRepository.save(enderecoEntity);
        return retornarDTO(enderecoEntity);
    }

    public EnderecoDTO editarProprio(Integer idEndereco, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        Integer idUsuarioLogado = usuarioLogadoEntity.getIdUsuario();
        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(idUsuarioLogado);
        List<UsuarioEntity> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(usuarioEntity);
        List<EnderecoEntity> enderecos = usuarioEntity.getEnderecos();
        int contador = 0;
        for (EnderecoEntity end : enderecos) {
            if (end.getIdEndereco().equals(idEndereco)) {
                contador += 1;
            }
        }
        if (contador > 0) {
            EnderecoEntity enderecoEntity = enderecoRepository.findById(idEndereco).get();
            enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);
            enderecoEntity.setIdEndereco(idEndereco);
            enderecoEntity.setUsuarios(listaUsuarios);
            return retornarDTO(enderecoRepository.save(enderecoEntity));
        } else {
            throw new RegraDeNegocioException("Endereco nao pertence ao usuario logado.");
        }
    }
    //------------------------------------------------------------------------------------------------------------------

    public String deletarProprio(Integer idEndereco) throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        Integer idUsuarioLogado = (Integer) usuarioLogadoEntity.getIdUsuario();
        Optional<UsuarioEntity> optionalUsuarioEntity = usuarioRepository.findById(idUsuarioLogado);
        UsuarioEntity usuarioEntity = optionalUsuarioEntity.get();
        List<UsuarioEntity> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(usuarioEntity);

        List<EnderecoEntity> enderecos = usuarioEntity.getEnderecos().stream().toList();
        int contador = 0;
        for (EnderecoEntity end : enderecos) {
            if (end.getIdEndereco().equals(idEndereco)) {
                contador += 1;
            }
        }
        if (contador > 0) {
            EnderecoEntity enderecoEntity = enderecoRepository.findById(idEndereco).get();
            enderecoRepository.delete(enderecoEntity);
            return "Endereco deletado.";
        } else {
            throw new RegraDeNegocioException("Endereco nao pertence ao usuario logado.");
        }
    }

    //==================================================================================================================
    //                                             MÉTODOS AUXILIARES
    //==================================================================================================================
    private EnderecoDTO retornarDTO(EnderecoEntity enderecoEntity) {
        return objectMapper.convertValue(enderecoEntity, EnderecoDTO.class);
    }

    private EnderecoEntity retornarEnderecoEntity(EnderecoCreateDTO enderecoDTO) {
        return objectMapper.convertValue(enderecoDTO, EnderecoEntity.class);
    }
}