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
                        authz.antMatchers("/", "/auth","/auth/cadastro").permitAll()

                                //.antMatchers(HttpMethod.GET, "/contato/","/endereco/","/postagem/","/comentario/","/usuario/", "/seguidores/").hasRole("ADMIN") // 1
                                /*//ADMIN
                                .antMatchers(HttpMethod.DELETE, "/usuario", "/comentario","/postagem").hasRole("ADMIN") // 1

                                .antMatchers(HttpMethod.POST, "/postagem", "/comentario", "/seguidores").hasRole("EMPRESA") // 1
                                //DEV
                                .antMatchers(HttpMethod.DELETE, "/contato", "/endereco","/comentario", "/postagem", "/seguidor", "/tecnologia","/usuario/deletar-se").hasRole("DEV") // 1
                                .antMatchers(HttpMethod.GET, "/contato/listar-se", "/endereco/listar-se","/postagem/listar-se", "/comentario/listar-se", "/seguidores" ,"/usuario", "/usuario/byname", "/tecnologia/listar-se").hasRole("DEV") // 1
                                .antMatchers(HttpMethod.POST, "/contato", "/endereco","/postagem", "/comentario", "/seguidores").hasRole("DEV") // 1
                                .antMatchers(HttpMethod.PUT, "/contato", "/endereco","/postagem", "/comentario", "/seguidores" ,"/usuario/editar-se").hasRole("DEV") // 1
                                //EMPRESA
                                .antMatchers(HttpMethod.DELETE, "/contato", "/endereco","/comentario", "/postagem", "/seguidor","/usuario/deletar-se").hasRole("EMPRESA") // 1
                                .antMatchers(HttpMethod.GET, "/contato", "/endereco","/postagem", "/comentario", "/seguidores" ,"/usuario/listar-se","/usuario/byname").hasRole("EMPRESA") // 1
                                .antMatchers(HttpMethod.POST, "/contato", "/endereco","/postagem", "/comentario", "/seguidores").hasRole("EMPRESA") // 1
                                .antMatchers(HttpMethod.PUT, "/contato", "/endereco","/postagem", "/comentario", "/seguidores" ,"/usuario/byname").hasRole("EMPRESA") // 1*/
                                //.antMatchers(HttpMethod.DELETE, "/usuario").hasAnyRole("ADMIN","DEV", "EMPRESA")
                                .antMatchers(HttpMethod.PUT, "/usuario/editar-se","/comentario/editar-se","/postagem/editar-se","/endereco/editar-se","/contato/").hasAnyRole("DEV", "EMPRESA") // 2
                                .antMatchers(HttpMethod.GET, "/usuario/listar-se", "/usuario/byname","/contato/listar-seus-contatos", "/endereco/listar-seus-enderecos", "/seguidor/**").hasAnyRole("DEV", "EMPRESA", "ADMIN") // 2
                                .antMatchers(HttpMethod.GET, "/**").hasRole("ADMIN") // 2
                                .antMatchers(HttpMethod.DELETE, "/**").hasRole("ADMIN") // 2
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