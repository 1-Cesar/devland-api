package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.config.MongoDB;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.repository.LogUsuarioRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.mongodb.BasicDBObject;
import com.mongodb.client.*;
import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuantidadeUsuarioServiceTest {

    @InjectMocks
    private QuantidadeUsuarioService quantidadeUsuarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LogUsuarioRepository logUsuarioRepository;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void deveTestarRetornarQtdUsuarioComSucesso() {

        Long testeReturn = Long.valueOf(0);

        when(logUsuarioRepository.contaTodosUsuarios()).thenReturn(testeReturn);

        String dev = quantidadeUsuarioService.retornarQtdUsuario(TipoUsuario.DEV);
        String empresa = quantidadeUsuarioService.retornarQtdUsuario(TipoUsuario.EMPRESA);
        String admin = quantidadeUsuarioService.retornarQtdUsuario(TipoUsuario.ADMIN);
        String todos = quantidadeUsuarioService.retornarTodosUsuarios();

        assertNotNull(dev);
        assertNotNull(empresa);
        assertNotNull(admin);
        assertNotNull(todos);
    }
}
