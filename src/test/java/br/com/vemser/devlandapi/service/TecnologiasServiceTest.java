package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasCreateDTO;
import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasDTO;
import br.com.vemser.devlandapi.entity.CargoEntity;
import br.com.vemser.devlandapi.entity.TecnologiasEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.TecnologiasRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TecnologiasServiceTest {

    @InjectMocks
    private TecnologiasService tecnologiasService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private TecnologiasRepository tecnologiasRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailServiceTest emailService;

    @Mock
    private UserLoginService userLoginService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(tecnologiasService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException {
        // setup
        TecnologiasCreateDTO tecnologiasCreateDTO = getTecnologiaCreateDTO();

        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        usuarioEntity.setIdUsuario(usuarioLogadoEntity.getIdUsuario());
        List<UsuarioEntity> usuarioEntities = List.of(usuarioEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        TecnologiasEntity tecnologiasEntity = getTecnologiaEntity();
        when(tecnologiasRepository.save(any(TecnologiasEntity.class))).thenReturn(tecnologiasEntity);

        // act
        TecnologiasDTO tecnologiasDTO = tecnologiasService.create(tecnologiasCreateDTO);

        // assert
        assertNotNull(tecnologiasDTO);
        assertEquals(1, tecnologiasDTO.getIdTecnologias().intValue());
        assertEquals("Java", tecnologiasDTO.getNomeTecnologia());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
        Integer idTecnologia = 1;

        TecnologiasEntity tecnologiasEntityRecuperada = getTecnologiaEntity();
        when(tecnologiasRepository.findById(anyInt())).thenReturn(Optional.of(tecnologiasEntityRecuperada));

        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        List<TecnologiasEntity> tecnologiasEntities = List.of(tecnologiasEntityRecuperada);
        when(tecnologiasRepository.findAll()).thenReturn(tecnologiasEntities);

        doNothing().when(tecnologiasRepository).delete(any(TecnologiasEntity.class));


        tecnologiasService.delete(idTecnologia);


        verify(tecnologiasRepository, times(1)).delete(any(TecnologiasEntity.class));
    }

    @Test
    public void deveTestarLocalizarTecnologiaByIdComSucesso() throws RegraDeNegocioException {
        TecnologiasEntity tecnologiasEntity = getTecnologiaEntity();
        Integer id = 1;

        when(tecnologiasRepository.findById(anyInt())).thenReturn(Optional.of(tecnologiasEntity));


        TecnologiasEntity tecnologiasEntityRecuperada = tecnologiasService.localizarTecnologiaById(id);

        assertNotNull(tecnologiasEntityRecuperada);
        assertNotNull(tecnologiasEntityRecuperada.getUsuario());
        assertEquals(1, tecnologiasEntityRecuperada.getIdTecnologias().intValue());
        assertEquals("Java", tecnologiasEntityRecuperada.getNomeTecnologia());
        assertEquals(1, tecnologiasEntityRecuperada.getIdUsuario().intValue());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarLocalizarTecnologiaByIdSemSucesso() throws RegraDeNegocioException {
        Integer idTecnologia = 10;

        TecnologiasEntity tecnologiasEntityRecuperado = tecnologiasService.localizarTecnologiaById(idTecnologia);
    }

    @Test
    public void deveTestarListarProprioComSucesso() throws RegraDeNegocioException {

        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        List<TecnologiasDTO> tecnologiasDTOS = tecnologiasService.listarProprio();

        assertNotNull(tecnologiasDTOS);
    }




    // util

    private static TecnologiasCreateDTO getTecnologiaCreateDTO() {
        TecnologiasCreateDTO tecnologiasCreateDTO = new TecnologiasCreateDTO();

        tecnologiasCreateDTO.setNomeTecnologia("Java");

        return tecnologiasCreateDTO;
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

    private static TecnologiasEntity getTecnologiaEntity() {
        TecnologiasEntity tecnologias = new TecnologiasEntity();

        tecnologias.setIdTecnologias(1);
        tecnologias.setIdUsuario(1);
        tecnologias.setNomeTecnologia("Java");
        tecnologias.setUsuario(getUsuarioEntity());

        return tecnologias;
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
