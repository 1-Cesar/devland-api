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
                @ApiResponse(responseCode = "200", description = "Operação realizada com sucesso!"),
                @ApiResponse(responseCode = "403", description = "Acesso negado! Verique os parametros ou sua permissão."),
                @ApiResponse(responseCode = "500", description = "Erro! Durante a execução, foi gerada uma exceção")
        }
)
public @interface AuthNotas {
}
