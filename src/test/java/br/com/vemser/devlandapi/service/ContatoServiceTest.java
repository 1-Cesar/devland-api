package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.contato.ContatoDTO;
import br.com.vemser.devlandapi.entity.ContatoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ContatoRepository;
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
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ContatoServiceTest {
    @InjectMocks
    private ContatoService contatoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ContatoRepository contatoRepository;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UserLoginService userLoginService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(contatoService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListarPaginadoComSucesso() {
        //setup
        List<ContatoEntity> entityList = List.of(getContatoEntity());
        Page<ContatoEntity> page = new PageImpl<>(entityList);

        when(contatoRepository.findAll(any(Pageable.class))).thenReturn(page);

        // act
        PageDTO<ContatoDTO> list = contatoService.listarPaginado(2, 15);

        // assert
        assertNotNull(list);
        assertEquals(1, list.getTotalElements().intValue());
        assertEquals(1, list.getContent().size());
    }

    @Test
    public void deveListarPorIdComSucesso() throws RegraDeNegocioException {
        ContatoEntity contatoEntity = getContatoEntity();
        Integer idContato = 1;
        when(contatoRepository.findById(anyInt())).thenReturn(Optional.of(contatoEntity));
        //act
        List<ContatoDTO> contatoDTOS = contatoService.listarContatoPorId(idContato);
        //assert
        assertNotNull(contatoDTOS);
        assertEquals(1, contatoDTOS.size());
    }

    @Test
    public void deveTestarListarContatoUsuarioComSucesso() throws RegraDeNegocioException {
        List<ContatoEntity> entityList = List.of(getContatoEntity());
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        Integer idContato = 1;

        when(contatoRepository.findAll()).thenReturn(entityList);
        when(usuarioService.localizarUsuario(idContato)).thenReturn(usuarioEntity);

        List<ContatoDTO> contatoUsuario = contatoService.listarContatoUsuario(idContato);

        assertFalse(contatoUsuario.isEmpty());
        assertEquals(1, contatoUsuario.size());
    }

    @Test
    public void deveTestarListarContatoUsuarioLogadoComSucesso() throws RegraDeNegocioException {
        List<ContatoEntity> entityList = List.of(getContatoEntity());
        UserLoginEntity userLoginEntity = getUserLoginEntity();

        when(contatoRepository.findAll().stream()
                .filter(contato -> contato.getIdUsuario().equals(userLoginEntity.getIdUsuario()))
                .collect(Collectors.toList())).thenReturn(entityList);
        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);

        List<ContatoDTO> usuarioLogado = contatoService.listarContatoUsuarioLogado();

        assertFalse(usuarioLogado.isEmpty());
        assertEquals(1, usuarioLogado.size());
    }

    @Test
    public void deveTestarAdicionarComSucesso() throws RegraDeNegocioException {
        UserLoginEntity userLoginEntity = getUserLoginEntity();
        ContatoEntity contatoEntity = getContatoEntity();
        ContatoCreateDTO contatoCreateDTO = getcontatoCreateDTO();

        when(userLoginService.findById(anyInt())).thenReturn(userLoginEntity);
        when(contatoRepository.save(contatoEntity)).thenReturn(contatoEntity);

        ContatoDTO contatoDTO = (ContatoDTO) contatoService.adicionar(contatoCreateDTO);
    }


    //    ================================ METODOS AUXILIARES ====================
    private static ContatoEntity getContatoEntity() {
        ContatoEntity contatoEntity = new ContatoEntity();

        contatoEntity.setIdContato(1);
        contatoEntity.setDescricao("Ligar só dps das 18h");
        contatoEntity.setNumero("894");
        contatoEntity.setTipo(TipoClassificacao.RESIDENCIAL);
        contatoEntity.setIdUsuario(1);

        return contatoEntity;
    }

    private static UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuario = new UsuarioEntity();

        usuario.setIdUsuario(1);
        usuario.setNome("Samuel");

        return usuario;
    }

    private static UserLoginEntity getUserLoginEntity() {
        UserLoginEntity userLoginEntity = new UserLoginEntity();

        userLoginEntity.setIdUserLogin(1);
        userLoginEntity.setLogin("rsf");
        userLoginEntity.setSenha("123");
        userLoginEntity.setIdUsuario(1);

        return userLoginEntity;
    }

    private static ContatoCreateDTO getcontatoCreateDTO() {
        ContatoCreateDTO contatoCreateDTO = new ContatoCreateDTO();

        contatoCreateDTO.setIdUsuario(1);
        contatoCreateDTO.setNumero("88665544");
        contatoCreateDTO.setDescricao("ligar horario comercial");
        contatoCreateDTO.setTipo(TipoClassificacao.COMERCIAL);

        return contatoCreateDTO;
    }
}
