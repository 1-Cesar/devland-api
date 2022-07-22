package br.com.vemser.devlandapi.annotations;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(
        value = {
                @ApiResponse(responseCode = "200", description = "Recupera seguidor por id de usuário"),
                @ApiResponse(responseCode = "400", description = "Usuário não encontrado"),
                @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
        }
)
public @interface SeguidorNotas {
}
