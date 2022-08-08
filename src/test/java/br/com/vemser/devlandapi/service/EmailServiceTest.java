package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoMensagem;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.logging.log4j.message.SimpleMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.WantedButNotInvoked;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private freemarker.template.Configuration fmConfiguration;

    @Test
    public void deveTestarSendEmailUsuarioComPost() throws IOException, MessagingException, TemplateException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        TipoMensagem tipoMensagem = TipoMensagem.CREATE;

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "from", "mr_robot@testedostestes.com");
        doNothing().when(emailSender).send(any(MimeMessage.class));
        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("Oiii", "createee", new Configuration()));

        emailService.sendEmailUsuario(usuarioEntity, tipoMensagem.getTipo());

        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void deveTestarSendEmailComRequisicaoPut() throws IOException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        TipoMensagem tipoMensagem = TipoMensagem.UPDATE;

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "from", "mr_robot@testedostestes.com");
        doNothing().when(emailSender).send(any(MimeMessage.class));
        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("Oiii", "updateee", new Configuration()));

        emailService.sendEmailUsuario(usuarioEntity, tipoMensagem.getTipo());

        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void deveTestarSendEmailComRequisicaoDelete() throws IOException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();
        TipoMensagem tipoMensagem = TipoMensagem.DELETE;

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "from", "mr_robot@testedostestes.com");
        doNothing().when(emailSender).send(any(MimeMessage.class));
        when(fmConfiguration.getTemplate(anyString())).thenReturn(new Template("Oiii", "deleteee", new Configuration()));

        emailService.sendEmailUsuario(usuarioEntity, tipoMensagem.getTipo());

        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test(expected = WantedButNotInvoked.class)
    public void deveTestarSendEmailSemSucesso() throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = getUsuarioEntity();

        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "from", "mr_robot@testedostestes.com");

        emailService.sendEmailUsuario(usuarioEntity, "teste");

        verify(emailSender, times(1)).send(any(MimeMessage.class));
    }

    private static UsuarioEntity getUsuarioEntity() {

        UsuarioEntity usuarioEntity = new UsuarioEntity();

        usuarioEntity.setCpfCnpj("35351148293");
        usuarioEntity.setEmail("cesar@teste.com.br");
        usuarioEntity.setNome("cesar");
        usuarioEntity.setFoto("foto");
        usuarioEntity.setGenero(Genero.MASCULINO);
        usuarioEntity.setAreaAtuacao("Java");
        usuarioEntity.setTipoUsuario(TipoUsuario.DEV);

        return usuarioEntity;
    }

}
