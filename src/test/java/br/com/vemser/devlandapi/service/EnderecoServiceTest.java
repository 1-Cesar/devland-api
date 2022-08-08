package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoCreateDTO;
import br.com.vemser.devlandapi.dto.endereco.EnderecoDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.EnderecoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.EnderecoRepository;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EnderecoServiceTest {
    @InjectMocks
    private EnderecoService enderecoService;
    @Mock
    private EnderecoRepository enderecoRepository;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UserLoginService userLoginService;
    @Mock
    private UserLoginRepository userLoginRepository;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(enderecoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListarComSucesso() throws RegraDeNegocioException {
        EnderecoEntity enderecoEnt = getEnderecoEntity();
        EnderecoEntity enderecoEnt1 = getEnderecoEntity();

        List<EnderecoEntity> listaEnt = new ArrayList<>();

        listaEnt.add(enderecoEnt);
        listaEnt.add(enderecoEnt1);

        when(enderecoRepository.findAll()).thenReturn(listaEnt);
        List<EnderecoDTO> listar = enderecoService.listar();

        assertNotNull(listar);
        assertEquals(2, listar.size());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarListarComFalha() throws RegraDeNegocioException {
        EnderecoEntity enderecoEnt = getEnderecoEntity();
        EnderecoEntity enderecoEnt1 = getEnderecoEntity();

        List<EnderecoEntity> listaEnt = new ArrayList<>();
//        deixando a lista zerada
//        listaEnt.add(enderecoEnt);
//        listaEnt.add(enderecoEnt1);

        when(enderecoRepository.findAll()).thenReturn(listaEnt);
        List<EnderecoDTO> listar = enderecoService.listar();

        if (listar.isEmpty()) {
            throw new RegraDeNegocioException("Nenhum endereço encontrado");
        }
    }

    @Test
    public void deveTestarListarEndereco() throws RegraDeNegocioException {
        EnderecoEntity enderecoEnt = getEnderecoEntity();
        List<EnderecoEntity> listaEnt = new ArrayList<>();
        listaEnt.add(enderecoEnt);
        Integer id = 1;

        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(enderecoEnt));

        List<EnderecoDTO> enderecoDTOList = enderecoService.listarEndereco(id);

        assertFalse(enderecoDTOList.isEmpty());
        assertEquals(1, enderecoDTOList.size());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        EnderecoEntity enderecoEnt = getEnderecoEntity();
        Integer id = 1;

        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(enderecoEnt));
        doNothing().when(enderecoRepository).delete(any(EnderecoEntity.class));

        enderecoService.delete(id);
        verify(enderecoRepository, times(1)).delete(any(EnderecoEntity.class));
    }

    @Test
    public void deveTestarAdicionarComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EnderecoEntity enderecoEntity = getEnderecoEntity();

        Integer id = 1;
        EnderecoCreateDTO enderecoCreateDTO = getEnderecoCreateDTO();


        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);

        EnderecoDTO enderecoDTO = enderecoService.adicionar(id, enderecoCreateDTO);

        assertNotNull(enderecoDTO);
        assertEquals(TipoClassificacao.RESIDENCIAL, enderecoDTO.getTipo());
    }

    @Test
    public void deveTestarPaginacaoPaisComSucesso() {
        // setup
        List<EnderecoEntity> enderecoEntityList = List.of(getEnderecoEntity());

        Page<EnderecoEntity> page = new PageImpl<>(enderecoEntityList);
        String pais = "Brasil";

        when(enderecoRepository.paginacaoPais(anyString(), any(Pageable.class))).thenReturn(page);

        // act
        PageDTO<EnderecoDTO> list = enderecoService.paginacaoPais(pais, 0, 12);

        // assert
        assertNotNull(list);
        assertEquals(1, list.getTotalElements().intValue());
        assertEquals(1, list.getContent().size());
    }

    // EXCLUSIVO DEV EMPRESA
    @Test
    public void deveTestarListarEnderecosUsuarioLogado() throws RegraDeNegocioException {
        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = new UserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        List<UsuarioDTO> usuarioDTOList = enderecoService.listarEnderecosUsuarioLogado();

        assertNotNull(usuarioDTOList);
    }

    @Test
    public void deveTestarAdicionarProprioComSucesso() throws RegraDeNegocioException {
        // EXCLUSIVO DEV EMPRESA
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EnderecoEntity enderecoEntity = getEnderecoEntity();
        List<UsuarioEntity> usuarioEntityList = List.of(getUsuarioEntity());

        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setUsuarioEntity(usuarioEntity);

        Integer id = 1;
        EnderecoCreateDTO enderecoCreateDTO = getEnderecoCreateDTO();

        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);

        EnderecoDTO enderecoDTO = enderecoService.adicionarProprio(enderecoCreateDTO);

        assertNotNull(enderecoDTO);
        assertEquals(TipoClassificacao.RESIDENCIAL, enderecoDTO.getTipo());
    }

    @Test
    public void deveTestarEditarProprioComSucesso() throws RegraDeNegocioException {
        // EXCLUSIVO DEV EMPRESA
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EnderecoEntity enderecoEntity = getEnderecoEntity();
        List<UsuarioEntity> usuarioEntityList = new ArrayList<>();

        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setUsuarioEntity(usuarioEntity);
        userLoginEntity.setIdUsuario(1);
        userLoginEntity.setIdUserLogin(1);
        userLoginEntity.setLogin("cesar");
        userLoginEntity.setSenha("123");

        usuarioEntity.setUserLoginEntity(userLoginEntity);

        Integer idEndereco = 1;
        Integer idLoggedUser = 22;

        usuarioEntityList.add(usuarioEntity);

        EnderecoCreateDTO enderecoCreateDTO = getEnderecoCreateDTO();

        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(enderecoEntity));
        when(enderecoRepository.save(any(EnderecoEntity.class))).thenReturn(enderecoEntity);

        EnderecoDTO enderecoDTO = enderecoService.editarProprio(idEndereco, enderecoCreateDTO);

        assertNotNull(enderecoDTO);
        assertEquals(TipoClassificacao.RESIDENCIAL, enderecoDTO.getTipo());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarEditarProprioComFalha() throws RegraDeNegocioException {
        // EXCLUSIVO DEV EMPRESA
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EnderecoEntity enderecoEntity = getEnderecoEntity();
        List<UsuarioEntity> usuarioEntityList = new ArrayList<>();

        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setUsuarioEntity(usuarioEntity);
        userLoginEntity.setIdUsuario(1);
        userLoginEntity.setIdUserLogin(1);
        userLoginEntity.setLogin("cesar");
        userLoginEntity.setSenha("123");

        usuarioEntity.setUserLoginEntity(userLoginEntity);

        Integer idEndereco = 50;
        Integer idLoggedUser = 22;

        usuarioEntityList.add(usuarioEntity);

        EnderecoCreateDTO enderecoCreateDTO = getEnderecoCreateDTO();

        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);

        EnderecoDTO enderecoDTO = enderecoService.editarProprio(idEndereco, enderecoCreateDTO);
        if (!usuarioEntity.getEnderecos().contains(enderecoRepository.findById(idEndereco))) {
            throw new RegraDeNegocioException("Endereco nao pertence ao usuario logado.");
        }
    }

    @Test
    public void deveTestarDeletarProprioComSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EnderecoEntity enderecoEntity = getEnderecoEntity();
        List<UsuarioEntity> usuarioEntityList = List.of(getUsuarioEntity());
        Integer idEndereco = 1;

        UserLoginEntity usuarioLogadoEntity = new UserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(1);
        usuarioLogadoEntity.setUsuarioEntity(usuarioEntity);
        Integer id = usuarioLogadoEntity.getIdUsuario();

        usuarioEntity.setUserLoginEntity(usuarioLogadoEntity);

        when(userLoginService.getIdLoggedUser()).thenReturn(id);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));
        when(enderecoRepository.findById(anyInt())).thenReturn(Optional.of(enderecoEntity));
        doNothing().when(enderecoRepository).delete(any(EnderecoEntity.class));

        enderecoService.deletarProprio(idEndereco);
        verify(enderecoRepository, times(1)).delete(any(EnderecoEntity.class));
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarDeletarProprioComFalha() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        EnderecoEntity enderecoEntity = getEnderecoEntity();
        List<UsuarioEntity> usuarioEntityList = List.of(getUsuarioEntity());
        Integer idEndereco = 50;

        UserLoginEntity usuarioLogadoEntity = new UserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(1);
        usuarioLogadoEntity.setUsuarioEntity(usuarioEntity);
        Integer id = usuarioLogadoEntity.getIdUsuario();
        usuarioEntity.setUserLoginEntity(usuarioLogadoEntity);

        when(userLoginService.getIdLoggedUser()).thenReturn(id);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        enderecoService.deletarProprio(idEndereco);

        if (!usuarioEntity.getEnderecos().contains(enderecoRepository.findById(idEndereco))) {
            throw new RegraDeNegocioException("Endereco nao pertence ao usuario logado.");
        }
    }

    @Test
    public void deveTestarListarEnderecoUsuario() throws RegraDeNegocioException {
        Integer idUsuario = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        List<EnderecoDTO> enderecoDTOS = enderecoService.listarEnderecoUsuario(idUsuario);

        assertFalse(enderecoDTOS.isEmpty());
        assertEquals(1, enderecoDTOS.size());

    }


    //    =================== METODOS AUXILIARES =================================
    private static EnderecoEntity getEnderecoEntity() {
        List<UsuarioEntity> usuarioEntityList = List.of(getUsuarioEntity());

        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setIdEndereco(1);
        enderecoEntity.setCep("64001210");
        enderecoEntity.setUsuarios(usuarioEntityList);
        enderecoEntity.setCidade("Teresina");
        enderecoEntity.setLogradouro("rua");
        enderecoEntity.setNumero("12");
        enderecoEntity.setEstado("Pi");
        enderecoEntity.setTipo(TipoClassificacao.RESIDENCIAL);
        enderecoEntity.setComplemento(null);

        return enderecoEntity;
    }

    private static EnderecoDTO getEnderecoDTO() {
        EnderecoDTO enderecoDTO = new EnderecoDTO();
        enderecoDTO.setIdEndereco(1);
        enderecoDTO.setCep("64001210");
        enderecoDTO.setUsuarios(null);
        enderecoDTO.setCidade("Teresina");
        enderecoDTO.setLogradouro("rua");
        enderecoDTO.setNumero("12");
        enderecoDTO.setEstado("Pi");
        enderecoDTO.setTipo(TipoClassificacao.RESIDENCIAL);
        enderecoDTO.setComplemento(null);

        return enderecoDTO;
    }

    private static EnderecoCreateDTO getEnderecoCreateDTO() {

        EnderecoCreateDTO enderecoCreateDTO = new EnderecoCreateDTO();
        enderecoCreateDTO.setCep("64001210");
        enderecoCreateDTO.setCidade("Teresina");
        enderecoCreateDTO.setLogradouro("rua");
        enderecoCreateDTO.setNumero("12");
        enderecoCreateDTO.setEstado("Pi");
        enderecoCreateDTO.setTipo(TipoClassificacao.RESIDENCIAL);
        enderecoCreateDTO.setComplemento(null);

        return enderecoCreateDTO;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();

        UserLoginEntity userLoginEntity = new UserLoginEntity();
        userLoginEntity.setUsuarioEntity(usuarioEntity);
        userLoginEntity.setIdUsuario(1);
        userLoginEntity.setIdUserLogin(1);
        usuarioEntity.setUserLoginEntity(userLoginEntity);

        List<EnderecoEntity> listaEndEnt = new ArrayList<>();
        EnderecoEntity enderecoEntity = new EnderecoEntity();
        enderecoEntity.setIdEndereco(1);
        enderecoEntity.setCep("64001210");
        enderecoEntity.setCidade("Teresina");
        enderecoEntity.setLogradouro("rua");
        enderecoEntity.setNumero("12");
        enderecoEntity.setEstado("Pi");
        enderecoEntity.setTipo(TipoClassificacao.RESIDENCIAL);
        enderecoEntity.setComplemento(null);
        listaEndEnt.add(enderecoEntity);
        usuarioEntity.setEnderecos(listaEndEnt);

        usuarioEntity.setCpfCnpj("35351148293");
        usuarioEntity.setEmail("cesar@teste.com.br");
        usuarioEntity.setNome("cesar");
        usuarioEntity.setFoto("foto");
        usuarioEntity.setGenero(Genero.MASCULINO);
        usuarioEntity.setAreaAtuacao("Java");
        usuarioEntity.setTipoUsuario(TipoUsuario.DEV);
        usuarioEntity.setIdUsuario(1);
        usuarioEntity.getUserLoginEntity().setLogin("cesar");
        usuarioEntity.getUserLoginEntity().setSenha("123");


        return usuarioEntity;
    }

    private static UsuarioCreateDTO getUsuarioCreateDTO() {

        UsuarioCreateDTO usuarioCreateDTO = new UsuarioCreateDTO();

        usuarioCreateDTO.setCpfCnpj("35351148293");
        usuarioCreateDTO.setEmail("cesar@teste.com.br");
        usuarioCreateDTO.setNome("cesar");
        usuarioCreateDTO.setFoto("foto");
        usuarioCreateDTO.setGenero(Genero.MASCULINO);
        usuarioCreateDTO.setAreaAtuacao("Java");
        usuarioCreateDTO.setTipoUsuario(TipoUsuario.DEV);

        return usuarioCreateDTO;
    }
}
