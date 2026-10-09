package med.clift.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.clift.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {
    private static final String apiName = "API Clift.med";

    @Value("${api.security.token.secret}")
    private String secret;

    public String gerarToken(Usuario usuario) {
        try {
            return JWT.create()
                .withIssuer(apiName)
                .withSubject(usuario.getLogin())
                .withExpiresAt(dataExpiracao())
                .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro para gerar token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                            .withIssuer(apiName)
                            .build()
                            .verify(tokenJWT)
                            .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token inválido ou expirado");
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
 }