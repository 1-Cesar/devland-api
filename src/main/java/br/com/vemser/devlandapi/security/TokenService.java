package br.com.vemser.devlandapi.security;

import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.UserLoginService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private String expiration;

    private final UserLoginService userLoginService;
    //private static final String TOKEN_PREFIX = "Bearer ";
    private static final String KEY_CARGOS = "roles";


    //criando um token JWT
    public String getToken(UserLoginEntity userLoginEntity) throws RegraDeNegocioException {

        if (userLoginEntity.getStatus().equals(false)) {
            throw new RegraDeNegocioException("Seu login foi desativado. Entre em contato com o suporte: contato@devland.com");
        }

        Date now = new Date();
        Date exp = new Date(now.getTime() + Long.valueOf(expiration)); //convertendo para long

        List<String> listaDeCargos = userLoginEntity.getCargos().stream()
                .map(cargoEntity -> cargoEntity.getNome())
                .toList();

        String token = Jwts.builder()
                .setIssuer("devland-api")
                .claim(Claims.ID, userLoginEntity.getIdUserLogin())
                .claim(KEY_CARGOS, listaDeCargos)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();


        //return TOKEN_PREFIX + token;
        return TokenAuthenticationFilter.BEARER + token;
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
            List<String> cargos = payload.get(KEY_CARGOS, List.class);

            List<SimpleGrantedAuthority> cargosGrantedAuthority = cargos.stream()
                    .map(cargo -> new SimpleGrantedAuthority(cargo))
                    .toList();

            //como o jws já sinalizou que a chave é válida, estamos buscando do póprio jwt
            // e setando no objeto abaixo
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(idUsuario, null, cargosGrantedAuthority);

            return usernamePasswordAuthenticationToken;
        }
        return null;
    }
}