package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.postagem.PostagemDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostagemServiceTest {
    @InjectMocks
    private PostagemService postagemService;

    @Test //não é do jupiter
    public void deveTestarListComPaginacao(){

    }
}
