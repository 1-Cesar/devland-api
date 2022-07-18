package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.Usuario;
import br.com.vemser.devlandapi.enums.TipoMensagem;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final freemarker.template.Configuration fmConfiguration;

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender emailSender;

    public void sendEmailUsuario(Usuario usuario, String tipo) {
        MimeMessage mimeMessage = emailSender.createMimeMessage();
        try {

            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuario.getEmail());
            if (tipo.equals(TipoMensagem.CREATE.getTipo())){
                mimeMessageHelper.setSubject("Cadastro realizado");
            } else if (tipo.equals(TipoMensagem.UPDATE.getTipo())) {
                mimeMessageHelper.setSubject("Cadastro atualizado");
            } else {
                mimeMessageHelper.setSubject("Cadastro deletado");
            }
            mimeMessageHelper.setText(getContentFromTemplatePessoa(usuario, tipo), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (MessagingException | IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    public String getContentFromTemplatePessoa(Usuario usuario, String tipo) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();
        dados.put("nome", usuario.getNome());
        dados.put("id", usuario.getIdUsuario());
        dados.put("email", this.from);

        Template template;
        if (tipo.equals(TipoMensagem.CREATE.getTipo())){
            template = fmConfiguration.getTemplate("email-template-create.ftl");
        } else if (tipo.equals(TipoMensagem.UPDATE.getTipo())) {
            template = fmConfiguration.getTemplate("email-template-update.ftl");
        } else {
            template = fmConfiguration.getTemplate("email-template-delete.ftl");
        }
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}
