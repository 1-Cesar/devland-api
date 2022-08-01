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

    public List<EnderecoDTO> listarEndereco(int id) throws RegraDeNegocioException {
        localizarEndereco(id);
        return enderecoRepository.findById(id).stream()
                .map(enderecoEntity -> {
                    EnderecoDTO enderecoDTO = retornarDTO(enderecoEntity);
                    enderecoDTO.setUsuarios(enderecoEntity.getUsuarios().stream()
                            .map(usuarioEntity -> objectMapper.convertValue(usuarioEntity, UsuarioDTO.class))
                            .toList());
                    return enderecoDTO;
                }).toList();
    }


    public void delete(Integer id) throws RegraDeNegocioException {
        EnderecoEntity enderecoEntity = localizarEndereco(id);
        enderecoRepository.delete(enderecoEntity);
    }

    /*
    public EnderecoDTO editar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {

        EnderecoEntity enderecoEntity = localizarEndereco(id);

        enderecoEntity = validaAlteracoes(enderecoEntity, enderecoCreateDTO);

        enderecoRepository.save(enderecoEntity);

        return retornarDTO(enderecoEntity);
    }
    */


    public EnderecoDTO adicionar(Integer id, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {

        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);

        EnderecoEntity enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);

        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(usuarioEntity);

        enderecoEntity.setUsuarios(usuarios);

        enderecoRepository.save(enderecoEntity);

        return retornarDTO(enderecoEntity);
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

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();
        usuarioService.localizarUsuario(id);
        return usuarioRepository.findById(id).stream()
                .map(pessoaEntity -> {
                    UsuarioDTO usuarioDTO = usuarioService.retornarDTO(pessoaEntity);
                    usuarioDTO.setEnderecoDTOS(pessoaEntity.getEnderecos().stream()
                            .map(enderecoEntity -> objectMapper.convertValue(enderecoEntity, EnderecoDTO.class))
                            .toList());
                    return usuarioDTO;
                }).toList();
    }

    //------------------------------------------------------------------------------------------------------------------

    public EnderecoDTO adicionar(EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);

        EnderecoEntity enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);

        List<UsuarioEntity> usuarios = new ArrayList<>();
        usuarios.add(usuarioEntity);

        enderecoEntity.setUsuarios(usuarios);

        enderecoRepository.save(enderecoEntity);

        return retornarDTO(enderecoEntity);
    }

    //------------------------------------------------------------------------------------------------------------------



    public EnderecoDTO editarProprio(Integer idEndereco, EnderecoCreateDTO enderecoCreateDTO) throws RegraDeNegocioException {
        //Buscando usuário logado
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        Integer idUsuarioLogado = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuarioLogado).get();
        List<UsuarioEntity> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(0,usuarioEntity);

        List<EnderecoEntity> enderecos = usuarioEntity.getEnderecos().stream().toList();
        int contador = 0;
        for (EnderecoEntity end: enderecos
             ) {
            if(end.getIdEndereco().equals(idEndereco)){
            contador +=1;
            }
        }
        if (contador>0) {
            EnderecoEntity enderecoEntity = convertEnderecoOptionalToEntity(enderecoRepository.findById(idEndereco));
            enderecoEntity = retornarEnderecoEntity(enderecoCreateDTO);
            enderecoEntity.setIdEndereco(idEndereco);
            enderecoEntity.setUsuarios(listaUsuarios);
            return retornarDTO(enderecoRepository.save(enderecoEntity));
        } else {
            throw new RegraDeNegocioException("Endereco nao pertence ao usuario logado.");
        }
    }


    //------------------------------------------------------------------------------------------------------------------


    // TODO - DELETAR ENDEREÇO EM ENDEREÇOS DO USUARIO LOGADO

    public String deletarProprio(Integer idEndereco) throws RegraDeNegocioException{
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);
        Integer idUsuarioLogado = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioEntity = usuarioRepository.findById(idUsuarioLogado).get();
        List<UsuarioEntity> listaUsuarios = new ArrayList<>();
        listaUsuarios.add(0,usuarioEntity);

        List<EnderecoEntity> enderecos = usuarioEntity.getEnderecos().stream().toList();
        int contador = 0;
        for (EnderecoEntity end: enderecos
        ) {
            if(end.getIdEndereco().equals(idEndereco)){
                contador +=1;
            }
        }
        if (contador>0) {
            EnderecoEntity enderecoEntity = convertEnderecoOptionalToEntity(enderecoRepository.findById(idEndereco));
            enderecoRepository.delete(enderecoEntity);
            return "Endereco deletado.";
        } else {
            throw new RegraDeNegocioException("Endereco nao pertence ao usuario logado.");
        }
    }


    //==================================================================================================================
    //                                             MÉTODOS AUXILIARES
    //==================================================================================================================

    public EnderecoEntity localizarEndereco(Integer idEndereco) throws RegraDeNegocioException {
        return enderecoRepository.findAll().stream()
                .filter(endereco -> endereco.getIdEndereco().equals(idEndereco))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Endereço não localizado"));
    }


    public List<UsuarioDTO> listarEnderecoUsuario(Integer id) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        return usuarioRepository.findById(id).stream()
                .map(pessoaEntity -> {
                    UsuarioDTO usuarioDTO = usuarioService.retornarDTO(pessoaEntity);
                    usuarioDTO.setEnderecoDTOS(pessoaEntity.getEnderecos().stream()
                            .map(enderecoEntity -> objectMapper.convertValue(enderecoEntity, EnderecoDTO.class))
                            .toList());
                    return usuarioDTO;
                }).toList();
    }

    public EnderecoDTO retornarDTO(EnderecoEntity enderecoEntity) {
        return objectMapper.convertValue(enderecoEntity, EnderecoDTO.class);
    }

    public EnderecoEntity retornarEnderecoEntity(EnderecoCreateDTO enderecoDTO) {
        return objectMapper.convertValue(enderecoDTO, EnderecoEntity.class);
    }

    public EnderecoEntity convertEnderecoOptionalToEntity(Optional enderecoOptional) {
        return objectMapper.convertValue(enderecoOptional, EnderecoEntity.class);
    }


}

