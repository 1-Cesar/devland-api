package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoDTO;
import br.com.vemser.devlandapi.entity.*;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ContatoRepository;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ContatoServiceTest {

    @InjectMocks
    private ContatoService contatoService;

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private UserLoginService userLoginService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(contatoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListarPaginado() {
        ContatoEntity contatoEntity = getContatoEntity();

        List<ContatoEntity> contatoEntities = List.of(contatoEntity);

        Page<ContatoEntity> page = new PageImpl<>(contatoEntities);

        when(contatoRepository.findAll(any(Pageable.class))).thenReturn(page);

        PageDTO<ContatoDTO> dtoPageDTO = contatoService.listarPaginado(0, 10);

        assertNotNull(dtoPageDTO);
        assertEquals(1, dtoPageDTO.getTotalElements().intValue());
        assertEquals(1, dtoPageDTO.getContent().size());
    }

    @Test
    public void deveTestarListarContatoPorId() throws RegraDeNegocioException {
        ContatoEntity contatoRecuperado = getContatoEntity();
        when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contatoRecuperado));

        List<ContatoDTO> contatoDTOS = contatoService.listarContatoPorId(1);

        assertNotNull(contatoDTOS);
        assertTrue(contatoDTOS.size() > 0);
    }

    @Test
    public void deveTestarListarContatoUsuario() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);

        List<ContatoEntity> contatoEntities = List.of(getContatoEntity());
        when(contatoRepository.findAll()).thenReturn(contatoEntities);

        List<ContatoDTO> contatoDTOS = contatoService.listarContatoUsuario(1);

        assertNotNull(contatoDTOS);
        assertTrue(contatoDTOS.size() > 0);
    }

    @Test
    public void deveTestarListarContatoUsuarioLogado() throws RegraDeNegocioException {
        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);

        List<ContatoEntity> contatoEntities = List.of(getContatoEntity());
        when(contatoRepository.findAll()).thenReturn(contatoEntities);


        List<ContatoDTO> contatoDTOS = contatoService.listarContatoUsuarioLogado();

        assertNotNull(contatoDTOS);
        assertTrue(contatoDTOS.size() > 0);
    }

    @Test
    public void deveTestarAdicionarComSucesso() throws RegraDeNegocioException {
        ContatoCreateDTO contatoCreateDTO = getContatoCreateDTO();

        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setUserLoginEntity(usuarioLogadoEntity);
        usuarioEntity.setIdUsuario(usuarioLogadoEntity.getIdUsuario());
        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);

        ContatoEntity contatoEntity = getContatoEntity();
        when(contatoRepository.save(any(ContatoEntity.class))).thenReturn(contatoEntity);


        ContatoDTO contatoDTO = contatoService.adicionar(contatoCreateDTO);


        assertNotNull(contatoDTO);
        assertEquals(1, contatoDTO.getIdContato().intValue());
        assertEquals("111", contatoDTO.getNumero());
        assertEquals("Teste de contato", contatoDTO.getDescricao());
        assertEquals(TipoClassificacao.RESIDENCIAL, contatoDTO.getTipo());
    }

    @Test
    public void deveTestarEditarComSucesso() throws RegraDeNegocioException {
        ContatoDTO contatoDTO1 = getContatoDTO();

        ContatoEntity contatoRecuperado = getContatoEntity();
        when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contatoRecuperado));

        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        ContatoEntity contatoEntity = getContatoEntity();
        when(contatoRepository.findByIdContatoAndIdUsuario(anyInt(), anyInt())).thenReturn(contatoEntity);

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioService.localizarUsuario(anyInt())).thenReturn(usuarioEntity);

        when(contatoRepository.save(any(ContatoEntity.class))).thenReturn(contatoEntity);


        ContatoDTO contatoDTO = contatoService.editar(1, contatoDTO1);

        assertNotNull(contatoDTO);
        assertEquals(1, contatoDTO.getIdContato().intValue());
        assertEquals("111", contatoDTO.getNumero());
        assertEquals("Teste de contato", contatoDTO.getDescricao());
        assertEquals(TipoClassificacao.RESIDENCIAL, contatoDTO.getTipo());
    }

    @Test
    public void deveTestarRemoverComSucesso() throws RegraDeNegocioException {
        ContatoEntity contatoRecuperado = getContatoEntity();
        when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contatoRecuperado));

        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        doNothing().when(contatoRepository).delete(any(ContatoEntity.class));


        contatoService.remover(1);


        verify(contatoRepository, times(1)).delete(any(ContatoEntity.class));
    }

    // Util

    private static ContatoCreateDTO getContatoCreateDTO() {
    ContatoCreateDTO contatoCreateDTO = new ContatoCreateDTO();

    contatoCreateDTO.setIdUsuario(1);
    contatoCreateDTO.setNumero("111");
    contatoCreateDTO.setDescricao("Teste de contato");
    contatoCreateDTO.setTipo(TipoClassificacao.RESIDENCIAL);

    return contatoCreateDTO;
    }

    private static ContatoEntity getContatoEntity() {
        ContatoEntity contatoEntity = new ContatoEntity();

        contatoEntity.setIdContato(1);
        contatoEntity.setIdUsuario(1);
        contatoEntity.setTipo(TipoClassificacao.RESIDENCIAL);
        contatoEntity.setNumero("111");
        contatoEntity.setDescricao("Teste de contato");
        contatoEntity.setUsuario(getUsuarioEntity());

        return contatoEntity;
    }

    private static ContatoDTO getContatoDTO() {
        ContatoDTO contatoDTO = new ContatoDTO();

        contatoDTO.setIdContato(1);
        contatoDTO.setIdUsuario(1);
        contatoDTO.setTipo(TipoClassificacao.RESIDENCIAL);
        contatoDTO.setNumero("111");
        contatoDTO.setDescricao("Teste de contato");

        return contatoDTO;
    }

    private static UserLoginEntity getUserLoginEntity() {
        UserLoginEntity userLoginEntity = new UserLoginEntity();

        userLoginEntity.setIdUserLogin(1);
        userLoginEntity.setIdUsuario(1);
        userLoginEntity.setLogin("Joao");
        userLoginEntity.setSenha("123");
        userLoginEntity.setStatus(true);
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());

        CargoEntity cargoEntity = new CargoEntity();
        cargoEntity.setIdCargo(1);
        List<UserLoginEntity> userLoginEntities = List.of(userLoginEntity);
        cargoEntity.setUserLogins(userLoginEntities);
        cargoEntity.setNome("admin");
        List<CargoEntity> cargoEntities = List.of(cargoEntity);

        userLoginEntity.setCargos(cargoEntities);

        return userLoginEntity;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setIdUsuario(1);
        usuario.setNome("Joao");
        usuario.setEmail("joao@email.com");
        usuario.setAreaAtuacao("java");
        usuario.setCpfCnpj("15441774145");
        usuario.setFoto("minha foto");
        usuario.setGenero(Genero.MASCULINO);
        usuario.setTipoUsuario(TipoUsuario.DEV);
        usuario.setContatos(null);
        usuario.setSeguidores(null);
        usuario.setEnderecos(null);
        usuario.setTecnologias(null);
        usuario.setPostagens(null);
        usuario.setComentarios(null);
        usuario.setUserLoginEntity(null);

        return usuario;
    }
}
