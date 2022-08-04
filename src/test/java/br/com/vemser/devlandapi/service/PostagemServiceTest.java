package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.postagem.PostagemCreateDTO;
import br.com.vemser.devlandapi.dto.postagem.PostagemDTO;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPostagemDTO;
import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PostagemServiceTest {
    @InjectMocks
    private PostagemService postagemService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private PostagemRepository postagemRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ComentarioRepository comentarioRepository;

    @Mock
    private ComentarioService comentarioService;

    @Before
    public void init() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        ReflectionTestUtils.setField(postagemService, "objectMapper", objectMapper);
    }

    @Test //não é do jupiter
    public void deveTestarListPaginadoComSucesso() throws RegraDeNegocioException {
//      PageDTO<PostagemDTO> list
        // setup
        List<PostagemEntity> postagemEntities = List.of(getPostagemEntity());

        Page<PostagemEntity> page = new PageImpl<>(postagemEntities);


        when(postagemRepository.findAll(any(Pageable.class))).thenReturn(page);

        // act
        PageDTO<PostagemDTO> list = postagemService.list(3, 10);


        // assert
        assertNotNull(list);
        assertEquals(1, list.getTotalElements().intValue());
        assertEquals(1, list.getContent().size());
    }

    @Test
    public void deveTestarRelatorioPostagemPaginadoComSucesso() throws RegraDeNegocioException {
//      PageDTO<RelatorioPostagemDTO> relatorioPostagem
        // setup
        RelatorioPostagemDTO relatorioPostagemDTO = new RelatorioPostagemDTO();
        relatorioPostagemDTO.setTipoPostagem(TipoPostagem.PENSAMENTOS);
        relatorioPostagemDTO.setCurtidas(4);
        relatorioPostagemDTO.setNome("Postagem exemplar");
        relatorioPostagemDTO.setDescricao("Postagem passada como exemplo");
        relatorioPostagemDTO.setData(LocalDateTime.now());
        relatorioPostagemDTO.setTitulo("Relatorio");

        List<RelatorioPostagemDTO> listRelatorio = new ArrayList<>();
        listRelatorio.add(relatorioPostagemDTO);

        Page<RelatorioPostagemDTO> page = new PageImpl<>(listRelatorio);

        TipoPostagem tp = TipoPostagem.PENSAMENTOS;


        when(postagemRepository.relatorioPostagem(any(TipoPostagem.class),
                any(PageRequest.class))).thenReturn(page);

        //act
        PageDTO<RelatorioPostagemDTO> relatorioPostagem = postagemService.relatorioPostagem(tp, 0, 10);

        //assert
        assertNotNull(relatorioPostagem);
        assertEquals(1, relatorioPostagem.getTotalElements().intValue());
        assertEquals(1, relatorioPostagem.getContent().size());
    }

    @Test
    public void deveTestarFindByIdPostagemComSucesso() throws RegraDeNegocioException {
//        PostagemDTO findByIdPostagem
        PostagemCreateDTO postagemCreateDTO = getPostagemCreateDTO();
        PostagemEntity postagemEntity = getPostagemEntity();
        Integer idPostagem = 1;


        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));

        PostagemDTO postagemDTO = postagemService.findByIdPostagem(idPostagem);
        postagemDTO.setIdPostagem(postagemEntity.getIdPostagem());
        postagemDTO.setTipoPostagem(postagemEntity.getTipoPostagem());
        postagemDTO.setCurtidas(postagemEntity.getCurtidas());

        assertNotNull(postagemDTO);
        assertNotNull(postagemDTO.getIdPostagem());
        assertEquals(TipoPostagem.VAGAS, postagemDTO.getTipoPostagem());
        assertEquals(0L,postagemDTO.getCurtidas().longValue());
//      assertEquals(LocalDateTime.now(), postagemDTO.getData());
        assertEquals("Excelente oportunidade.",postagemDTO.getDescricao());
        assertEquals("http...",postagemDTO.getFoto());
        assertEquals("Unica",postagemDTO.getTitulo());
    }

    @Test
    public void deveTestarFindByTituloComSucesso() {
//        List<PostagemDTO> findByTitulo
        PostagemCreateDTO postagemCreateDTO = getPostagemCreateDTO();
        PostagemEntity postagemEntity = getPostagemEntity();
        //titulo = Unica
        PostagemEntity postagemEntity1 = getPostagemEntity();
        postagemEntity1.setTitulo("nunca");

        PostagemEntity postagemEntity2 = getPostagemEntity();
        postagemEntity2.setTitulo("UNICA");

        List<PostagemEntity> lista = new ArrayList<>();

        lista.add(postagemEntity);
        lista.add(postagemEntity1);
        lista.add(postagemEntity2);

        String titulo = "unica";

        when(postagemRepository.findAll()).thenReturn(lista);

        //act
        List<PostagemDTO> byTitulo = postagemService.findByTitulo(titulo);

        //assert
        assertNotNull(byTitulo);
        assertEquals(2, byTitulo.size());
    }

    @Test
    public void deveTestarPaginadoFindByTipoComSucesso() throws RegraDeNegocioException {
//        PageDTO<RelatorioPostagemDTO> relatorioPostagem(TipoPostagem tipoPostagem, Integer pagina, Integer quantRegistros)
        // setup

        PostagemEntity postagemEntity = getPostagemEntity();


        List<PostagemEntity> listEnt = new ArrayList<>();
        listEnt.add(postagemEntity);

        Page<PostagemEntity> page = new PageImpl<>(listEnt);

        TipoPostagem tp = TipoPostagem.VAGAS;


        when(postagemRepository.filtrarPorTipo(any(TipoPostagem.class),
                any(PageRequest.class))).thenReturn(page);

        //act
        PageDTO<PostagemDTO> dtoPageDTO = postagemService.findByTipo(tp, 0, 10);

        //assert
        assertNotNull(dtoPageDTO);
        assertEquals(1, dtoPageDTO.getTotalElements().intValue());
        assertEquals(1, dtoPageDTO.getContent().size());

    }

    @Test
    public void deveTestarPostComSucesso() throws RegraDeNegocioException {
//        PostagemDTO post(Integer idUsuario, PostagemCreateDTO postagemCreateDTO)
        //setup
        PostagemCreateDTO postagemCreateDTO = getPostagemCreateDTO();
        PostagemEntity postagemEntity = getPostagemEntity();
        UsuarioEntity usuarioEntity = new UsuarioEntity();
        Integer idUsuario = 2;

        when(postagemRepository.save(any(PostagemEntity.class))).thenReturn(postagemEntity);
        when(usuarioRepository.findById(any(Integer.class))).thenReturn(Optional.of(usuarioEntity));
        //act
        PostagemDTO postagemDTO = postagemService.post(1,postagemCreateDTO);
        postagemDTO.setIdPostagem(postagemEntity.getIdPostagem());
        //assert

        assertNotNull(postagemDTO);
        assertNotNull(postagemDTO.getIdPostagem());
        assertEquals(TipoPostagem.VAGAS, postagemDTO.getTipoPostagem());
        assertEquals(0,postagemDTO.getCurtidas().intValue());
//        assertEquals(LocalDateTime.now(), postagemDTO.getData());
        assertEquals("Excelente oportunidade.",postagemDTO.getDescricao());
        assertEquals("http...",postagemDTO.getFoto());
        assertEquals("Unica",postagemDTO.getTitulo());
    }


    @Test
    public void deveTestarCurtirComSucesso() throws RegraDeNegocioException {
//        PostagemDTO curtir
        PostagemEntity postagemEntity = getPostagemEntity();
        Integer idPostagem = 1;

        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));

        when(postagemRepository.save(any(PostagemEntity.class))).thenReturn(postagemEntity);
        PostagemDTO postagemDTO = new PostagemDTO();
        postagemDTO.setCurtidas(1);

        // act
        postagemDTO = postagemService.curtir(idPostagem);
        postagemDTO.setIdPostagem(postagemEntity.getIdPostagem());

        // assert
        assertNotNull(postagemDTO);
        assertNotNull(postagemDTO.getIdPostagem());
        assertEquals(TipoPostagem.VAGAS, postagemDTO.getTipoPostagem());
        assertEquals(1, postagemDTO.getCurtidas().longValue());
        assertEquals("Excelente oportunidade.",postagemDTO.getDescricao());
        assertEquals("http...",postagemDTO.getFoto());
        assertEquals("Unica",postagemDTO.getTitulo());

    }

    @Test(expected = RegraDeNegocioException.class)
    public void deveTestarCurtirPostagemInexistente()throws RegraDeNegocioException{
        PostagemEntity postagemEntity = getPostagemEntity();
        Integer idPostagem = 50;

        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));

        PostagemDTO postagemDTO = postagemService.curtir(idPostagem);
        // act
        if(idPostagem != postagemEntity.getIdPostagem()){
            throw new RegraDeNegocioException("Postagem não encontrada");
        }
    }

    @Test
    public void deveTestarUpdateComSucesso() throws RegraDeNegocioException {
//        PostagemDTO update
        // setup
        PostagemCreateDTO postagemCreateDTO = getPostagemCreateDTO();
        PostagemEntity postagemEntity = getPostagemEntity();

        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));
        when(postagemRepository.save(any(PostagemEntity.class))).thenReturn(postagemEntity);

        // act
        PostagemDTO postagemDTO = postagemService.update(1, postagemCreateDTO);
        postagemDTO.setIdPostagem(postagemEntity.getIdPostagem());
        postagemDTO.setCurtidas(postagemEntity.getCurtidas());

        // assert
        assertNotNull(postagemDTO);
        assertNotNull(postagemDTO.getIdPostagem());
        assertEquals(TipoPostagem.VAGAS, postagemDTO.getTipoPostagem());
        assertEquals(0, postagemDTO.getCurtidas().intValue());
        assertEquals("Excelente oportunidade.",postagemDTO.getDescricao());
        assertEquals("http...",postagemDTO.getFoto());
        assertEquals("Unica",postagemDTO.getTitulo());
    }

    @Test
    public void deveTestarDeleteComSucesso() throws RegraDeNegocioException {
//        void delete
        // setup
        Integer idParaDeletar = 10;
        PostagemEntity postagemEntity = getPostagemEntity();

        when(postagemRepository.findById(anyInt())).thenReturn(Optional.of(postagemEntity));
        doNothing().when(postagemRepository).delete(any(PostagemEntity.class));

        // act
        postagemService.delete(idParaDeletar);

        // assert
        verify(postagemRepository, times(1)).delete(any(PostagemEntity.class));
    }

    // testar a exception linha 109 PostagemService


    //    =================== METODOS AUXILIARES =================================
    private static PostagemCreateDTO getPostagemCreateDTO() {
        PostagemCreateDTO postagemCreateDTO = new PostagemCreateDTO();
        postagemCreateDTO.setTipoPostagem(TipoPostagem.VAGAS);
        postagemCreateDTO.setIdUsuario(1);
        postagemCreateDTO.setDescricao("Excelente oportunidade.");
        postagemCreateDTO.setFoto("http...");
        postagemCreateDTO.setTitulo("Unica");

        return postagemCreateDTO;
    }

    private static PostagemEntity getPostagemEntity() {
        PostagemEntity postagemEntity = new PostagemEntity();

        postagemEntity.setIdPostagem(1);
        postagemEntity.setTipoPostagem(TipoPostagem.VAGAS);
        postagemEntity.setIdUsuario(5);
        postagemEntity.setDescricao("Excelente oportunidade.");
        postagemEntity.setFoto("http...");
        postagemEntity.setTitulo("Unica");
        postagemEntity.setData(LocalDateTime.now());
        postagemEntity.setCurtidas(0);
        postagemEntity.setComentarios(null);
        postagemEntity.setUsuario(null);

        return postagemEntity;
    }

}
