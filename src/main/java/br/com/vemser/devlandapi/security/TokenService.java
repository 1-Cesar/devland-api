package br.com.vemser.devlandapi.security;

import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.service.UserLoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private final UserLoginService userLoginService;
    private static final String TOKEN_PREFIX = "Bearer ";


    //criando um token JWT
    public String getToken(UserLoginEntity userLoginEntity) {

        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.valueOf(expiration)); //convertendo para long

        String token = Jwts.builder()
                .setIssuer("devland-api")
                .claim(Claims.ID, userLoginEntity.getIdAutenticacao())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();


        return TOKEN_PREFIX + token;
    }

    //validar se o token é válido e retornar o usuário se for válido
    public UsernamePasswordAuthenticationToken isValid(String token) {

        if (token == null) {
            return null;
        }

        Claims payload = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token) //tb verifica a expiração
                .getBody();

        //busca/ recupera o id que está dentro do body (payload) do JWTS
        Integer idUsuario = payload.get(Claims.ID, Integer.class);

        if (idUsuario != null) {

            //como o jws já sinalizou que a chave é válida, estamos buscando do póprio jwt
            // e setando no objeto abaixo
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(idUsuario, null, Collections.emptyList());

            return usernamePasswordAuthenticationToken;
        }
        return null;

    }
}