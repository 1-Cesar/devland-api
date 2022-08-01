package br.com.vemser.devlandapi.dto.tecnologias;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TecnologiasDTO extends TecnologiasCreateDTO {

    @Schema(description = "id da tecnologia", example = "1")
    private Integer idTecnologias;
}
