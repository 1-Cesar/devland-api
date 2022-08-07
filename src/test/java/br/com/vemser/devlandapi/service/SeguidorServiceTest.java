package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.seguidor.SeguidorCreateDTO;
import br.com.vemser.devlandapi.dto.seguidor.SeguidorDTO;
import br.com.vemser.devlandapi.entity.SeguidorEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.SeguidorRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class SeguidorServiceTest {

    @InjectMocks
    private SeguidorService seguidorService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SeguidorRepository seguidorRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UserLoginService userLoginService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(seguidorService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListarSeguidoresComSucesso() throws RegraDeNegocioException {
        // setup
        List<UsuarioEntity> usuarioEntities = List.of(getUsuarioEntity());
        List<SeguidorEntity> seguidorEntities = List.of(getSeguidorEntity());
        Page<SeguidorEntity> seguidorEntityPage = new PageImpl<>(seguidorEntities);

        when(seguidorRepository.filtrarQuemUsuarioSegue(any(Integer.class),any(PageRequest.class))).thenReturn(seguidorEntityPage);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        // act
        PageDTO<SeguidorDTO> paginaDeSeguidores = seguidorService.listarSeguidores(2,1, 10);

        // assert
        assertNotNull(paginaDeSeguidores);
        assertEquals(1, paginaDeSeguidores.getTotalElements().intValue());
        assertEquals(1, paginaDeSeguidores.getContent().size());
    }

    @Test
    public void deveTestarListarMeusSeguidoresComSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        Integer teste = 2;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        List<SeguidorEntity> seguidorEntities = List.of(getSeguidorEntity());
        Page<SeguidorEntity> seguidorEntityPage = new PageImpl<>(seguidorEntities);
        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        when(seguidorRepository.filtrarQuemUsuarioSegue(any(Integer.class),any(PageRequest.class))).thenReturn(seguidorEntityPage);

        PageDTO<SeguidorDTO> usuarioDTOS = seguidorService.listarMeusSeguidores(1,10);

        assertNotNull(usuarioDTOS);
        assertEquals(1, usuarioDTOS.getTotalElements().intValue());
        assertEquals(1, usuarioDTOS.getContent().size());
    }

    @Test
    public void deveTestarAdicionarComSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        Integer teste = 2;
        List<UsuarioEntity> usuarioEntities = List.of(getUsuarioEntity(), getUsuarioEntity2());
        List<SeguidorEntity> seguidorEntities = new ArrayList<>();
        SeguidorEntity seguidorEntity = getSeguidorEntity();

        SeguidorCreateDTO seguidorCreateEntities = getSeguidorCreateEntity();

        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        when(seguidorRepository.verificaSeguidor(any(Integer.class),any(Integer.class))).thenReturn(seguidorEntities);
        when(seguidorRepository.save(any(SeguidorEntity.class))).thenReturn(seguidorEntity);

        SeguidorCreateDTO seguidorCreateDTO = seguidorService.adicionar(seguidorCreateEntities);

        assertNotNull(seguidorCreateDTO);
        assertEquals(seguidorCreateEntities.getIdSeguidor(), seguidorCreateDTO.getIdSeguidor());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarAdicionarSemSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        Integer teste = 2;
        List<UsuarioEntity> usuarioEntities = List.of(getUsuarioEntity(), getUsuarioEntity2());

        SeguidorCreateDTO seguidorCreateEntities = getSeguidorCreateEntity();

        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);

        seguidorCreateEntities.setIdSeguidor(2);
        SeguidorCreateDTO seguidorCreateDTO = seguidorService.adicionar(seguidorCreateEntities);

        assertNotNull(seguidorCreateDTO);
        assertEquals(seguidorCreateEntities.getIdSeguidor(), seguidorCreateDTO.getIdSeguidor());
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarAdicionarSemSucessoJaSegue() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        Integer teste = 2;
        List<UsuarioEntity> usuarioEntities = List.of(getUsuarioEntity(), getUsuarioEntity2());
        List<SeguidorEntity> seguidorEntities = List.of(getSeguidorEntity());

        SeguidorCreateDTO seguidorCreateEntities = getSeguidorCreateEntity();

        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);
        when(seguidorRepository.verificaSeguidor(any(Integer.class),any(Integer.class))).thenReturn(seguidorEntities);

        seguidorCreateEntities.setIdSeguidor(1);
        SeguidorCreateDTO seguidorCreateDTO = seguidorService.adicionar(seguidorCreateEntities);

        assertNotNull(seguidorCreateDTO);
        assertEquals(seguidorCreateEntities.getIdSeguidor(), seguidorCreateDTO.getIdSeguidor());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity;
        Integer teste = 2;
        List<UsuarioEntity> usuarioEntities = new ArrayList<>();
        List<SeguidorEntity> seguidorEntities = List.of(getSeguidorEntity());

        userLoginEntity = getUsuarioEntity().getUserLoginEntity();
        userLoginEntity.setUsuarioEntity(getUsuarioEntity());
        userLoginEntity.setIdUsuario(teste);
        usuarioEntities.add(userLoginEntity.getUsuarioEntity());

        when(userLoginService.getIdLoggedUser()).thenReturn(teste);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(usuarioRepository.findAll()).thenReturn(usuarioEntities);
        when(seguidorRepository.findAll()).thenReturn(seguidorEntities);

        Integer idParaDeletar = 1;

        doNothing().when(seguidorRepository).delete(any(SeguidorEntity.class));

        seguidorService.delete(idParaDeletar);

        verify(seguidorRepository, times(1)).delete(any(SeguidorEntity.class));
    }

    private static SeguidorEntity getSeguidorEntity() {
        SeguidorEntity seguidor = new SeguidorEntity();

        seguidor.setIdUsuario(2);
        seguidor.setIdSeguidor(1);
        seguidor.setNomeSeguidor("Carlos");

        return seguidor;
    }

    private static SeguidorCreateDTO getSeguidorCreateEntity() {
        SeguidorCreateDTO seguidor = new SeguidorCreateDTO();

        seguidor.setIdSeguidor(1);

        return seguidor;
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
        usuarioEntity.setIdUsuario(2);
        usuarioEntity.getUserLoginEntity().setLogin("cesarr");
        usuarioEntity.getUserLoginEntity().setSenha("1234");

        return usuarioEntity;
    }

    private static UsuarioEntity getUsuarioEntity2() {
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

        return usuarioEntity;
    }
}
