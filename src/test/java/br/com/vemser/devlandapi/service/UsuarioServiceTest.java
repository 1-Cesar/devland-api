package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPersonalizadoDevDTO;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.GeneratedValue;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UserLoginRepository userLoginRepository;

    @Mock
    private UserLoginService userLoginService;

    @Mock
    private SecurityContextHolder securityContextHolder;

    @Mock
    private EmailService emailService;


    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarUsuarioCreateComSucesso() throws RegraDeNegocioException {

        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        when(userLoginRepository.save(any(UserLoginEntity.class))).thenReturn(usuarioEntity.getUserLoginEntity());

        UsuarioDTO usuarioDTO = usuarioService.adicionar(userLoginCreateDTO);

        assertNotNull(usuarioDTO);
        assertEquals("35351148293", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.DEV, usuarioDTO.getTipoUsuario());
        assertEquals("cesar", userLoginCreateDTO.getLogin());
        assertNotNull(usuarioDTO);
    }

    @Test
    public void deveTestarListarComSucesso() throws RegraDeNegocioException {
        // setup
        List<UsuarioEntity> usuarioEntities = List.of(getUsuarioEntity());
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);
        // act
        List<UsuarioDTO> usuarioDTOS = usuarioService.listar();
        // assert
        assertNotNull(usuarioDTOS);
        assertTrue(!usuarioDTOS.isEmpty());
    }

    @Test
    public void deveTestarListarUsuarioComSucesso() throws RegraDeNegocioException {
        // setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        List<UsuarioEntity> usuarioEntities2 = List.of(getUsuarioEntity());

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities2);
        // act
        List<UsuarioDTO> usuarioDTOS = usuarioService.listarUsuario(usuarioEntity.getIdUsuario());
        // assert
        assertNotNull(usuarioDTOS);
        assertTrue(!usuarioDTOS.isEmpty());
    }

    @Test
    public void deveTestarGerarRelatorioPersonalizadoComSucesso() {
        // setup
        List<RelatorioPersonalizadoDevDTO> usuarioEntities = List.of(getRelatorioPersonalizado());
        Page<RelatorioPersonalizadoDevDTO> pageUsuarioEntity = new PageImpl<>(usuarioEntities);

        when(usuarioRepository.relatorioPersonalizadoDevDTO(any(String.class),any(Pageable.class)))
                .thenReturn(pageUsuarioEntity);

        // act
        PageDTO<RelatorioPersonalizadoDevDTO> relatorioDeUsuario = usuarioService
                .relatorioStack("java", 1,10);

        // assert
        assertNotNull(relatorioDeUsuario);
        assertEquals(1, relatorioDeUsuario.getContent().size());
    }

    @Test
    public void deveTestarListarProprioComSucesso() throws RegraDeNegocioException {
        // setup
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        UserLoginEntity userLoginEntity = new UserLoginEntity();

        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);

        // act
        List<UsuarioDTO> usuarioDTOS = usuarioService.listarProprio();
        // assert
        assertNotNull(usuarioDTOS);
        assertTrue(!usuarioDTOS.isEmpty());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        // setup
        Integer idParaDeletar = 1;
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));
        doNothing().when(usuarioRepository).delete(any(UsuarioEntity.class));

        // act
        usuarioService.delete(idParaDeletar);

        // assert
        verify(usuarioRepository, times(1)).delete(any(UsuarioEntity.class));
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException {
        // setup
        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        //when(userLoginService.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));
        //when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        // act
        UsuarioDTO usuarioDTO = usuarioService.editarProprio(userLoginCreateDTO.getUsuarioCreateDTO());

        // assert
        assertNotNull(usuarioDTO);
        assertEquals(1, usuarioDTO.getIdUsuario().intValue());
        assertEquals("35351148293", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
    }

    private static UserLoginCreateDTO getUserLoginCreateDTO() {
        UserLoginCreateDTO userLoginCreateDTO = new UserLoginCreateDTO();
        UsuarioCreateDTO usuarioCreateDTO = new UsuarioCreateDTO();
        userLoginCreateDTO.setUsuarioCreateDTO(usuarioCreateDTO);
        userLoginCreateDTO.getUsuarioCreateDTO().setCpfCnpj("35351148293");
        userLoginCreateDTO.getUsuarioCreateDTO().setEmail("cesar@teste.com.br");
        userLoginCreateDTO.getUsuarioCreateDTO().setNome("cesar");
        userLoginCreateDTO.getUsuarioCreateDTO().setFoto("foto");
        userLoginCreateDTO.getUsuarioCreateDTO().setGenero(Genero.MASCULINO);
        userLoginCreateDTO.getUsuarioCreateDTO().setAreaAtuacao("Java");
        userLoginCreateDTO.getUsuarioCreateDTO().setTipoUsuario(TipoUsuario.DEV);
        userLoginCreateDTO.setLogin("cesar");
        userLoginCreateDTO.setSenha("123");
        return userLoginCreateDTO;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        UserLoginEntity userLoginEntity = new UserLoginEntity();
        usuarioEntity.setUserLoginEntity(userLoginEntity);
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

    private static RelatorioPersonalizadoDevDTO getRelatorioPersonalizado() {
        RelatorioPersonalizadoDevDTO relatorio = new RelatorioPersonalizadoDevDTO();

        relatorio.setTipoUsuario(TipoUsuario.DEV);
        relatorio.setEmail("cesar@teste.com.br");
        relatorio.setNome("cesar");
        relatorio.setFoto("foto");
        relatorio.setGenero(Genero.MASCULINO);
        relatorio.setAreaAtuacao("Java");
        relatorio.setTipoUsuario(TipoUsuario.DEV);
        relatorio.setCidade("Sao Paulo");
        relatorio.setEstado("SP");
        relatorio.setPais("Brasil");
        relatorio.setNumero("12345678");
        relatorio.setDescricao("Whatsapp");
        relatorio.setNomeTecnologia("HTML");
        relatorio.setTipo(TipoClassificacao.COMERCIAL);

        return relatorio;
    }
}
