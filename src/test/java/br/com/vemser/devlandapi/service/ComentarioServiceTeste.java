package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.comentario.ComentarioCreateDTO;
import br.com.vemser.devlandapi.dto.comentario.ComentarioDTO;
import br.com.vemser.devlandapi.entity.ComentarioEntity;
import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ComentarioRepository;
import br.com.vemser.devlandapi.repository.PostagemRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ComentarioServiceTeste {

    @InjectMocks
    private ComentarioService comentarioService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PostagemRepository postagemRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(comentarioService, "objectMapper", objectMapper);
    }

    @Test
    public void deveTestarListComSucesso() throws RegraDeNegocioException {
        // setup
        List<ComentarioEntity> comentarioEntities = List.of(getComentarioEntity());

        Page<ComentarioEntity> page = new PageImpl<>(comentarioEntities);

        when(comentarioRepository.findAll(any(Pageable.class))).thenReturn(page);


        // act
        PageDTO<ComentarioDTO> list = comentarioService.list(3, 10);


        // assert
        assertNotNull(list);
        assertEquals(1, list.getTotalElements().intValue());
        assertEquals(1, list.getContent().size());
    }

    @Test
    public void deveTestarListByIdPostagemComSucesso() {
        Integer id = 1;

        List<ComentarioEntity> comentarioEntities = List.of(getComentarioEntity());

        when(comentarioRepository.findByidPostagem(anyInt())).thenReturn(comentarioEntities);

        // act
        List<ComentarioDTO> comentarioDTOS = comentarioService.listByIdPostagem(id);

        // assert
        assertNotNull(comentarioDTOS);
        assertTrue(!comentarioDTOS.isEmpty());
    }

    @Test
    public void deveTestarCreateComSucesso() throws RegraDeNegocioException {
        // setup
        ComentarioCreateDTO comentarioCreateDTO = getComentarioCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        PostagemEntity postagemEntity = getPostagemEntity();
//        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));

        ComentarioEntity comentarioEntity = getComentarioEntity();
        when(comentarioRepository.save(any(ComentarioEntity.class))).thenReturn(comentarioEntity);

        // act
        ComentarioDTO comentarioDTO = comentarioService.create(postagemEntity.getIdPostagem(), usuarioEntity.getIdUsuario(), comentarioCreateDTO);


        // assert
        assertNotNull(comentarioDTO);
        assertEquals(1, comentarioDTO.getIdComentario().intValue());
        assertEquals(1, comentarioDTO.getIdUsuario().intValue());
        assertEquals(1, comentarioDTO.getIdPostagem().intValue());
        assertEquals("Teste", comentarioDTO.getDescricaoComentarios());
        assertEquals(1, comentarioDTO.getCurtidasComentario().intValue());
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException {
        // setup
        Integer idComentario = 1;
        ComentarioCreateDTO comentarioCreateDTO = getComentarioCreateDTO();

        UsuarioEntity usuarioEntity = getUsuarioEntity();
//        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.of(usuarioEntity));

        PostagemEntity postagemEntity = getPostagemEntity();
//        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));

        ComentarioEntity comentarioEntity = getComentarioEntity();
        when(comentarioRepository.save(any(ComentarioEntity.class))).thenReturn(comentarioEntity);

        // act
        ComentarioDTO comentarioDTO = comentarioService.update(idComentario, comentarioCreateDTO);

        // assert
        assertNotNull(comentarioDTO);
        assertEquals(1, comentarioDTO.getIdComentario().intValue());
        assertEquals(1, comentarioDTO.getIdPostagem().intValue());
        assertEquals(1, comentarioDTO.getIdUsuario().intValue());
        assertEquals("Teste", comentarioDTO.getDescricaoComentarios());
        assertEquals(1, comentarioDTO.getCurtidasComentario().intValue());
    }

    @Test
    public void deveTesterDeleteComSucesso() {
        // setup
        Integer idParaDeletar = 1;
        ComentarioEntity comentarioEntity = getComentarioEntity();

        when(comentarioRepository.findById(anyInt())).thenReturn(Optional.of(comentarioEntity));
        doNothing().when(comentarioRepository).delete(any(ComentarioEntity.class));

        // act
        comentarioService.delete(idParaDeletar);

        //assert
        verify(comentarioRepository, times(1)).delete(any(ComentarioEntity.class));
    }

    //    =================== METODOS AUXILIARES =================================

    private static ComentarioCreateDTO getComentarioCreateDTO() {
        ComentarioCreateDTO comentarioCreateDTO = new ComentarioCreateDTO();
        comentarioCreateDTO.setDescricaoComentarios("Descricao do comentario");
        return comentarioCreateDTO;
    }

    private static ComentarioEntity getComentarioEntity() {
        ComentarioEntity comentarioEntity = new ComentarioEntity();
        comentarioEntity.setIdComentario(1);
        comentarioEntity.setIdPostagem(1);
        comentarioEntity.setIdUsuario(1);
        comentarioEntity.setDescricaoComentarios("Teste");
        comentarioEntity.setCurtidasComentario(1);
        comentarioEntity.setDataComentario(LocalDateTime.now());
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        comentarioEntity.setUsuario(usuarioEntity);
        PostagemEntity postagemEntity = getPostagemEntity();
        comentarioEntity.setPostagem(postagemEntity);

        return comentarioEntity;
    }

    private static PostagemEntity getPostagemEntity() {
        PostagemEntity postagemEntity = new PostagemEntity();

        postagemEntity.setIdPostagem(1);
        postagemEntity.setTipoPostagem(TipoPostagem.VAGAS);
        postagemEntity.setIdUsuario(1);
        postagemEntity.setDescricao("Excelente oportunidade.");
        postagemEntity.setFoto("http...");
        postagemEntity.setTitulo("Unica");
        postagemEntity.setCurtidas(0);
        postagemEntity.setComentarios(null);
        postagemEntity.setUsuario(null);

        return postagemEntity;
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
