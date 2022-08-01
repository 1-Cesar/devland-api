package br.com.vemser.devlandapi.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenService tokenService;

    protected static final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //buscando o token da request
        String token = getTokenFromHeader(request);

        //buscando o is valid de token service para validar o token
        UsernamePasswordAuthenticationToken dtoDoSpringSecurity = tokenService.isValid(token);

        //setando a autenticação
        SecurityContextHolder.getContext().setAuthentication(dtoDoSpringSecurity);

        filterChain.doFilter(request, response);
    }


    // adicionar o usuário no contexto do spring security
    private String getTokenFromHeader(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null) {
            return null;
        }
        return token.replace(BEARER, "");
    }
}

