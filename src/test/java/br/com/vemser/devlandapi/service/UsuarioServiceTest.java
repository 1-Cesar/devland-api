package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPersonalizadoDevDTO;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.LogUsuario;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.LogUsuarioRepository;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

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
    private LogUsuarioRepository logUsuarioRepository;

    @Mock
    private EmailServiceTest emailService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(usuarioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarUsuarioDevCreateComSucesso() throws RegraDeNegocioException {

        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        LogUsuario logUsuario = getLogUsuarioEntity();

        when(userLoginService.criptografarSenha(anyString())).thenReturn(userLoginCreateDTO.getSenha());

        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        when(userLoginRepository.save(any(UserLoginEntity.class))).thenReturn(usuarioEntity.getUserLoginEntity());

        when(logUsuarioRepository.save(any(LogUsuario.class))).thenReturn(logUsuario);

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
        assertEquals("123", userLoginCreateDTO.getSenha());
        assertNotNull(usuarioDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUsuarioCreateAdminSemSucesso() throws RegraDeNegocioException {

        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        usuarioEntity.setTipoUsuario(TipoUsuario.ADMIN);
        userLoginCreateDTO.getUsuarioCreateDTO().setTipoUsuario(TipoUsuario.ADMIN);
        usuarioEntity.setCpfCnpj("11111111111");
        userLoginCreateDTO.getUsuarioCreateDTO().setCpfCnpj("11111111111");

        UsuarioDTO usuarioDTO = usuarioService.adicionar(userLoginCreateDTO);

        assertNotNull(usuarioDTO);
        assertEquals("11111111111", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.ADMIN, usuarioDTO.getTipoUsuario());
        assertEquals("cesar", userLoginCreateDTO.getLogin());
        assertEquals("123", userLoginCreateDTO.getSenha());
        assertNotNull(usuarioDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUsuarioDevCreateSemSucesso() throws RegraDeNegocioException {

        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        usuarioEntity.setCpfCnpj("11111111111");
        userLoginCreateDTO.getUsuarioCreateDTO().setCpfCnpj("11111111111");

        UsuarioDTO usuarioDTO = usuarioService.adicionar(userLoginCreateDTO);

        assertNotNull(usuarioDTO);
        assertEquals("11111111111", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.ADMIN, usuarioDTO.getTipoUsuario());
        assertEquals("cesar", userLoginCreateDTO.getLogin());
        assertEquals("123", userLoginCreateDTO.getSenha());
        assertNotNull(usuarioDTO);
    }

    @Test
    public void deveTestarUsuarioEmpresaCreateComSucesso() throws RegraDeNegocioException {

        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        usuarioEntity.setTipoUsuario(TipoUsuario.EMPRESA);
        userLoginCreateDTO.getUsuarioCreateDTO().setTipoUsuario(TipoUsuario.EMPRESA);
        usuarioEntity.setCpfCnpj("06526412000146");
        userLoginCreateDTO.getUsuarioCreateDTO().setCpfCnpj("06526412000146");

        when(userLoginService.criptografarSenha(anyString())).thenReturn(userLoginCreateDTO.getSenha());

        when(usuarioRepository.save(any(UsuarioEntity.class))).thenReturn(usuarioEntity);

        when(userLoginRepository.save(any(UserLoginEntity.class))).thenReturn(usuarioEntity.getUserLoginEntity());

        UsuarioDTO usuarioDTO = usuarioService.adicionar(userLoginCreateDTO);

        assertNotNull(usuarioDTO);
        assertEquals("06526412000146", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.EMPRESA, usuarioDTO.getTipoUsuario());
        assertEquals("cesar", userLoginCreateDTO.getLogin());
        assertEquals("123", userLoginCreateDTO.getSenha());
        assertNotNull(usuarioDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUsuarioEmpresaCreateSemSucesso() throws RegraDeNegocioException {

        UserLoginCreateDTO userLoginCreateDTO = getUserLoginCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        usuarioEntity.setTipoUsuario(TipoUsuario.EMPRESA);
        userLoginCreateDTO.getUsuarioCreateDTO().setTipoUsuario(TipoUsuario.EMPRESA);
        usuarioEntity.setCpfCnpj("06526400000146");
        userLoginCreateDTO.getUsuarioCreateDTO().setCpfCnpj("06526400000146");

        UsuarioDTO usuarioDTO = usuarioService.adicionar(userLoginCreateDTO);

        assertNotNull(usuarioDTO);
        assertEquals("06526400000146", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.EMPRESA, usuarioDTO.getTipoUsuario());
        assertEquals("cesar", userLoginCreateDTO.getLogin());
        assertEquals("123", userLoginCreateDTO.getSenha());
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
    public void deveTestarListarNomeComSucesso() {
        // setup
        List<UsuarioEntity> usuarioEntities = List.of(getUsuarioEntity());
        UsuarioCreateDTO usuarioDTO = getUsuarioCreateDTO();
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        // act
        List<UsuarioDTO> usuarioDTOS = usuarioService.listarPorNome(usuarioDTO.getNome());

        // assert
        assertNotNull(usuarioDTOS);
        assertTrue(!usuarioDTOS.isEmpty());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarListarSemSucesso() throws RegraDeNegocioException {
        // setup

        when(usuarioRepository.findAll()).thenReturn(Collections.emptyList());

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
                .relatorioStack("Java", 1,10);

        // assert
        assertNotNull(relatorioDeUsuario);
        assertEquals(1, relatorioDeUsuario.getTotalElements().intValue());
        assertEquals(1, relatorioDeUsuario.getContent().size());
    }

    @Test
    public void deveTestarGerarRelatorioGeneroComSucesso() {
        // setup
        List<RelatorioPersonalizadoDevDTO> usuarioEntities = List.of(getRelatorioPersonalizado());
        Page<RelatorioPersonalizadoDevDTO> pageUsuarioEntity = new PageImpl<>(usuarioEntities);

        when(usuarioRepository.relatorioPersonalizadoDevGeneroDTO(any(Genero.class),any(Pageable.class)))
                .thenReturn(pageUsuarioEntity);

        // act
        PageDTO<RelatorioPersonalizadoDevDTO> relatorioDeUsuario = usuarioService
                .relatorioGenero(Genero.FEMININO, 1,10);

        // assert
        assertNotNull(relatorioDeUsuario);
        assertEquals(1, relatorioDeUsuario.getTotalElements().intValue());
        assertEquals(1, relatorioDeUsuario.getContent().size());
    }

    @Test
    public void deveTestarListarProprioComSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        Integer teste = 1;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.ofNullable(userLoginEntity.getUsuarioEntity()));

        List<UsuarioDTO> usuarioDTOS = usuarioService.listarProprio();

        assertNotNull(usuarioDTOS);
        assertTrue(!usuarioDTOS.isEmpty());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        // setup

        Integer idParaDeletar = 1;
        List<UsuarioEntity> usuarioEntities2 = List.of(getUsuarioEntity());

        when(usuarioRepository.findAll()).thenReturn(usuarioEntities2);
        doNothing().when(usuarioRepository).delete(any(UsuarioEntity.class));

        // act
        usuarioService.delete(idParaDeletar);

        // assert
        verify(usuarioRepository, times(1)).delete(any(UsuarioEntity.class));
    }

    @Test
    public void deveTestarUpdateDevComSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        Integer teste = 1;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        UsuarioDTO usuarioDTO = usuarioService.editarProprio(usuarioCreateDTO);

        // assert
        assertNotNull(usuarioDTO);
        assertEquals("35351148293", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.DEV, usuarioDTO.getTipoUsuario());
        assertNotNull(usuarioDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateDevSemSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        Integer teste = 1;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());
        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);

        usuarioCreateDTO.setCpfCnpj("123");
        usuarioEntity.setCpfCnpj("123");

        UsuarioDTO usuarioDTO = usuarioService.editarProprio(usuarioCreateDTO);

        // assert
        assertNotNull(usuarioDTO);
        assertEquals("35351148293", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.DEV, usuarioDTO.getTipoUsuario());
        assertNotNull(usuarioDTO);
    }

    @Test
    public void deveTestarUpdateEmpresaComSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        Integer teste = 1;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);


        usuarioCreateDTO.setTipoUsuario(TipoUsuario.EMPRESA);
        usuarioEntity.setTipoUsuario(TipoUsuario.EMPRESA);
        usuarioCreateDTO.setCpfCnpj("06526412000146");
        usuarioEntity.setCpfCnpj("06526412000146");

        UsuarioDTO usuarioDTO = usuarioService.editarProprio(usuarioCreateDTO);

        // assert
        assertNotNull(usuarioDTO);
        assertEquals("06526412000146", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.DEV, usuarioDTO.getTipoUsuario());
        assertNotNull(usuarioDTO);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarUpdateEmpresaSemSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        UsuarioCreateDTO usuarioCreateDTO = getUsuarioCreateDTO();
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        Integer teste = 1;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);

        usuarioCreateDTO.setTipoUsuario(TipoUsuario.EMPRESA);
        usuarioEntity.setTipoUsuario(TipoUsuario.EMPRESA);
        usuarioCreateDTO.setCpfCnpj("526412000146");
        usuarioEntity.setCpfCnpj("526412000146");

        UsuarioDTO usuarioDTO = usuarioService.editarProprio(usuarioCreateDTO);

        // assert
        assertNotNull(usuarioDTO);
        assertEquals("06526412000146", usuarioDTO.getCpfCnpj());
        assertEquals("cesar@teste.com.br", usuarioDTO.getEmail());
        assertEquals("cesar", usuarioDTO.getNome());
        assertEquals("foto", usuarioDTO.getFoto());
        assertEquals(Genero.MASCULINO, usuarioDTO.getGenero());
        assertEquals("Java", usuarioDTO.getAreaAtuacao());
        assertEquals(TipoUsuario.DEV, usuarioDTO.getTipoUsuario());
        assertNotNull(usuarioDTO);
    }

    @Test
    public void deveTestarValidaCpf() {

        UsuarioEntity usuarioEntity = getUsuarioEntity();

        Boolean teste = UsuarioService.ValidaCPF.isCPF(usuarioEntity.getCpfCnpj());

        String cpf = UsuarioService.ValidaCPF.imprimeCPF(usuarioEntity.getCpfCnpj());

        Assertions.assertTrue(teste);
        Assertions.assertEquals("353.511.482-93", cpf);
    }

    @Test
    public void deveTestarValidaCpfErro() {

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setCpfCnpj("123");
        Boolean teste = UsuarioService.ValidaCPF.isCPF(usuarioEntity.getCpfCnpj());

        usuarioEntity.setCpfCnpj("12345678912");
        UsuarioService.ValidaCPF.isCPF(usuarioEntity.getCpfCnpj());

        usuarioEntity.setCpfCnpj("10000000000");
        UsuarioService.ValidaCPF.isCPF(usuarioEntity.getCpfCnpj());

        Assertions.assertFalse(teste);
    }

    @Test
    public void deveTestarValidaCnpj() {

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setCpfCnpj("06526412000146");

        Boolean teste = UsuarioService.ValidaCNPJ.isCNPJ(usuarioEntity.getCpfCnpj());

        String cnpj = UsuarioService.ValidaCNPJ.imprimeCNPJ(usuarioEntity.getCpfCnpj());

        Assertions.assertTrue(teste);
        Assertions.assertEquals("06.526.412.0001-46", cnpj);
    }

    @Test
    public void deveTestarValidaCnpjErro() {

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setCpfCnpj("12345678912345");

        Boolean teste = UsuarioService.ValidaCNPJ.isCNPJ(usuarioEntity.getCpfCnpj());

        usuarioEntity.setCpfCnpj("1234567891234");

        UsuarioService.ValidaCNPJ.isCNPJ(usuarioEntity.getCpfCnpj());

        usuarioEntity.setCpfCnpj("98765432000000");

        UsuarioService.ValidaCNPJ.isCNPJ(usuarioEntity.getCpfCnpj());

        usuarioEntity.setCpfCnpj("98765432190000");

        UsuarioService.ValidaCNPJ.isCNPJ(usuarioEntity.getCpfCnpj());

        Assertions.assertFalse(teste);
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

    private static LogUsuario getLogUsuarioEntity() {
        LogUsuario logUsuario = new LogUsuario();

        logUsuario.setNome("cesar");
        logUsuario.setGenero(Genero.MASCULINO);
        logUsuario.setAreaAtuacao("Java");
        logUsuario.setTipoUsuario(TipoUsuario.DEV);
        logUsuario.setIdUsuario(1);

        return logUsuario;
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
