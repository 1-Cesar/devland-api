package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoMensagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
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

    public void sendEmailUsuario(UsuarioEntity usuario, String tipo)  {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setTo(usuario.getEmail());
            if (tipo.equals(TipoMensagem.CREATE.getTipo())) {
                mimeMessageHelper.setSubject("Olá, " + usuario.getNome() + "! Seja bem-vindo(a) na DevLand!");
            } else if (tipo.equals(TipoMensagem.UPDATE.getTipo())) {
                mimeMessageHelper.setSubject(usuario.getNome() + ", seus dados foram atualizados!");
            } else if (tipo.equals(TipoMensagem.DELETE.getTipo())) {
                mimeMessageHelper.setSubject(usuario.getNome() + ", sentiremos sua falta na DevLand!");
            } else {
                throw new RegraDeNegocioException("Falha no envio de e-mail");
            }
            mimeMessageHelper.setText(getContentFromTemplatePessoa(usuario, tipo), true);
            emailSender.send(mimeMessageHelper.getMimeMessage());
        } catch (RegraDeNegocioException | MessagingException | IOException | TemplateException e) {
            System.out.println("erro no envio de email");
        }
    }

    public String getContentFromTemplatePessoa(UsuarioEntity usuario, @NotNull String tipo) throws IOException, TemplateException {
        Map<String, Object> dados = new HashMap<>();

        Template template;

        if (tipo.equals(TipoMensagem.CREATE.getTipo())) {
            dados.put("nome", "Olá, " + usuario.getNome() + "! Seja bem-vindo(a) na DevLand!");
            dados.put("mensagem", "Seu cadastro foi realizado com sucesso, seu código de identificação é " + usuario.getIdUsuario());
            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
            template = fmConfiguration.getTemplate("email-template.html");

        } else if (tipo.equals(TipoMensagem.UPDATE.getTipo())) {
            dados.put("nome", "Olá, " + usuario.getNome() + "! Seus dados foram atualizados!");
            dados.put("mensagem", "Seus dados foram atualizados com sucesso e já podem ser encontrados por empresas e talentos.");
            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
            template = fmConfiguration.getTemplate("email-template.html");

        } else {
            dados.put("nome", "Olá, " + usuario.getNome() + "! Sentiremos sua falta na DevLand");
            dados.put("mensagem", "Seu cadastro foi removido da nossa rede, mas você pode voltar quando quiser!");
            dados.put("email", "Qualquer dúvida, entre em contato com o suporte pelo e-mail " + from);
            template = fmConfiguration.getTemplate("email-template.html");
        }
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, dados);
        return html;
    }
}
