package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.entity.CargoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
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
public class UserLoginServiceTest {

    @InjectMocks
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
        ReflectionTestUtils.setField(userLoginService, "objectMapper", objectMapper);
    }


    @Test
    public void deveTestarFindByLoginAndSenha() {

        UserLoginEntity userLoginEntity = getUserLoginEntity();
        when(userLoginRepository.findByLoginAndSenha(anyString(), anyString())).thenReturn(Optional.of(userLoginEntity));

        Optional<UserLoginEntity> userLogin = userLoginService.findByLoginAndSenha(userLoginEntity.getLogin(), userLoginEntity.getSenha());

        assertNotNull(userLogin);
        assertEquals("Joao", userLogin.get().getLogin());
        assertEquals("123", userLogin.get().getSenha());
    }

    @Test
    public void deveTestarFindByLogin() {

        UserLoginEntity userLoginEntity = getUserLoginEntity();
        when(userLoginRepository.findByLogin(anyString())).thenReturn(Optional.of(userLoginEntity));

        Optional<UserLoginEntity> userLogin = userLoginService.findByLogin(userLoginEntity.getLogin());

        assertNotNull(userLogin);
        assertEquals("Joao", userLogin.get().getLogin());
        assertEquals("123", userLogin.get().getSenha());
    }

    // util

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
