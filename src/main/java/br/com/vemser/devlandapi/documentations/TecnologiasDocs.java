package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.TecnologiasNotas;
import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasCreateDTO;
import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

public interface TecnologiasDocs {


    @TecnologiasNotas
    @Operation(summary = "insere tecnologia através do id do usuário", description = "insere uma tecnologia dentro do banco de dados com base no id do usuário")
    public ResponseEntity<TecnologiasDTO> adicionar(@PathVariable("idUsuario") Integer idUsuario,
                                                    @Valid @RequestBody TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException;

    @TecnologiasNotas
    @Operation(summary = "deleta tecnologia", description = "deleta uma tecnologia do banco de dados atraves de seu id")
    public void delete(@PathVariable("nomeTecnologia") String nomeTecnologia) throws RegraDeNegocioException;

}
