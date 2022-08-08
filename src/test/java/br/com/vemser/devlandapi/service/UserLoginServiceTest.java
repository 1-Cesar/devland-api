package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.userlogin.UserLoginAuthDTO;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.entity.CargoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoStatus;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
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
    public void deveTestarCriptografarSenhaComSucesso() {

    }

    @Test
    public void deveTestarTrocarSenhaComSucesso() throws RegraDeNegocioException {
        UserLoginEntity userLoginEntity = getUserLoginEntity();
        when(userLoginRepository.save(any(UserLoginEntity.class))).thenReturn(userLoginEntity);

        UserLoginAuthDTO userLoginAuthDTO = new UserLoginAuthDTO();

        userLoginAuthDTO.setLogin("Joao");
        userLoginAuthDTO.setSenha("123");

        String trocarsenha = userLoginService.trocarSenha(userLoginAuthDTO, userLoginEntity);

        assertEquals("Senha Alterada com Sucesso !", trocarsenha);
    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarTrocarSenhaComFalha() throws RegraDeNegocioException {
        UserLoginEntity userLoginEntity = getUserLoginEntity();
        when(userLoginRepository.save(any(UserLoginEntity.class))).thenReturn(userLoginEntity);

        UserLoginAuthDTO userLoginAuthDTO = new UserLoginAuthDTO();

        userLoginAuthDTO.setLogin("Joao");
        userLoginAuthDTO.setSenha("123");

        //trocando os Login p ver o erro
        userLoginEntity.setLogin("Rafael");

        String trocarsenha = userLoginService.trocarSenha(userLoginAuthDTO, userLoginEntity);

        if (!userLoginAuthDTO.getLogin().equals(userLoginEntity.getLogin())){
            throw new RegraDeNegocioException("Usuário ou senha inválidos");
        }
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

    @Test
    public void deveTestarGetIdLoggedUser() {
        Integer idLoggedUser = 1;

        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        Integer findUserId = userLoginService.getIdLoggedUser();

        assertNotNull(findUserId);
        assertEquals(1, findUserId.intValue());
    }

    @Test
    public void deveTestarGetLoggedUser() throws RegraDeNegocioException {
        Integer idLoggedUser = 1;
        when(userLoginService.getIdLoggedUser()).thenReturn(idLoggedUser);

        UserLoginEntity usuarioLogadoEntity = getUserLoginEntity();
        usuarioLogadoEntity.setIdUsuario(idLoggedUser);
        when(userLoginService.findById(anyInt())).thenReturn(usuarioLogadoEntity);

        UserLoginEntity userLoginEntity = userLoginService.getLoggedUser();

        assertNotNull(userLoginEntity);
        assertEquals("Joao", userLoginEntity.getLogin());
        assertEquals("123", userLoginEntity.getSenha());
    }

    @Test
    public void deveTestarFindById() throws RegraDeNegocioException {
        Integer id = 1;

        UserLoginEntity userLoginEntity = getUserLoginEntity();
        when(userLoginRepository.findById(anyInt())).thenReturn(Optional.of(userLoginEntity));

        UserLoginEntity userLogin = userLoginService.findById(id);

        assertNotNull(userLogin);
        assertEquals("Joao", userLogin.getLogin());
        assertEquals("123", userLogin.getSenha());

    }

    @Test
    public void deveTestarFindByIdUsuario() throws RegraDeNegocioException {
        Integer id = 1;

        UserLoginEntity userLoginRecuperado = getUserLoginEntity();
        List<UserLoginEntity> userLoginEntities = List.of(userLoginRecuperado);
        when(userLoginRepository.findAll()).thenReturn(userLoginEntities);

        UserLoginEntity userLoginEntity = userLoginService.findByIdUsuario(id);

        assertNotNull(userLoginEntity);
        assertEquals("Joao", userLoginEntity.getLogin());
        assertEquals("123", userLoginEntity.getSenha());
    }

    @Test
    public void deveTestarDesativar() throws RegraDeNegocioException {
        Integer id = 1;
        TipoStatus opcao = TipoStatus.DESATIVAR;
        TipoStatus opcao2 = TipoStatus.ATIVAR;

        UserLoginEntity userLoginRecuperado = getUserLoginEntity();
        List<UserLoginEntity> userLoginEntities = List.of(userLoginRecuperado);
        when(userLoginRepository.findAll()).thenReturn(userLoginEntities);

        when(userLoginRepository.save(any(UserLoginEntity.class))).thenReturn(userLoginRecuperado);

        String status = userLoginService.desativar(id, opcao);
        String status2 = userLoginService.desativar(id, opcao2);

        assertNotNull(status);
        assertEquals("login Desativado!", status);

        assertNotNull(status2);
        assertEquals("login Ativado!", status2);

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

    private static UserLoginCreateDTO getUserLoginCreateDTO(){
        UserLoginCreateDTO userLoginCreateDTO = new UserLoginCreateDTO();

        userLoginCreateDTO.setLogin("Joao");
        userLoginCreateDTO.setSenha("123");
        UsuarioCreateDTO usuarioCreateDTO = new UsuarioCreateDTO();
        usuarioCreateDTO.setNome("Joao");
        userLoginCreateDTO.setUsuarioCreateDTO(usuarioCreateDTO);

        return userLoginCreateDTO;
    }
}
