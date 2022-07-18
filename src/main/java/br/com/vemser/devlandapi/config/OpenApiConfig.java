package br.com.vemser.devlandapi.config;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Hidden
@Configuration
@RestController
public class OpenApiConfig {

    @GetMapping(value = "/")
    public void index(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui/index.html");
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .info(new Info()
                        .title("DevLand API  - Documentação")
                        .description("9º Vem Ser")
                        .version("v1.0.0")
                        .license(new License()
                                .name("DBC Company")
                                .url("https://www.dbccompany.com.br/"))
                        .contact(new Contact()
                                .name("Github da API")
                                .url("https://github.com/alysoncampos/devland-api")))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Clique aqui para ver nosso Diagrama de Entidade Relacionamento")
                        .url("https://i.imgur.com/89vxpMV.png"));
    }
}

