package br.com.vemser.devlandapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final TokenService tokenService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Desabilitar frameOptions
        http.headers().frameOptions().disable().and()
                // Habilitar cors
                .cors().and()
                // Desabilitar csrf
                .csrf().disable()
                // Adicionar regras de requisição
                .authorizeHttpRequests((authz) ->
                        authz.antMatchers("/", "/auth", "/auth/cadastro", "/auth/recuperar-login").permitAll()
                                .antMatchers(HttpMethod.PUT,
                                        "/usuario/editar-se",
                                        "/comentario/editar-se/",
                                        "/postagem/editar/", "/postagem/curtir/",
                                        "/endereco/editar-se/",
                                        "/contato/editar-se/").hasAnyRole("DEV", "EMPRESA")
                                .antMatchers(HttpMethod.GET,
                                        "/usuario/paginacao-tipo-usuario",
                                        "/usuario/relatorio-stack-usuario",
                                        "/usuario/relatorio-genero-usuario").hasAnyRole("EMPRESA", "ADMIN")
                                .antMatchers(HttpMethod.GET,
                                        "/usuario/byname",
                                        "/usuario/listar-se",
                                        "/contato/listar-se",
                                        "/endereco/listar-seus-enderecos",
                                        "/tecnologia/minhas-tecnologias",
                                        "/postagem/**",
                                        "/comentario/**",
                                        "/seguidor/**").hasAnyRole("DEV", "EMPRESA", "ADMIN")
                                .antMatchers(HttpMethod.POST,
                                        "/contato/adicionar-se",
                                        "/endereco/",
                                        "/seguidor/**",
                                        "/postagem/**",
                                        "/tecnologia/**",
                                        "/comentario/**").hasAnyRole("DEV", "EMPRESA")
                                .antMatchers(HttpMethod.DELETE, "/contato/deletar-se/", "/endereco/", "/seguidor/", "/tecnologia/", "/postagem/deletar/", "/comentario/delete/", "usuario/deletar-se").hasAnyRole("DEV", "EMPRESA")
                                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN")
                                .antMatchers(HttpMethod.GET, "/**", "/logs/**").hasRole("ADMIN")
                                .antMatchers(HttpMethod.PUT, "/auth/alterar-status/").hasRole("ADMIN")
                                .anyRequest().authenticated());
        // Adicionar filtro do token
        http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("*")
                        .exposedHeaders("Authorization");
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new LdapShaPasswordEncoder();
    }

    //retorna a autenticação do spring
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}