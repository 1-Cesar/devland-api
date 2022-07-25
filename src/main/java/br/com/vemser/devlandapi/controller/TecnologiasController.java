package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.TecnologiasDocs;
import br.com.vemser.devlandapi.dto.TecnologiasCreateDTO;
import br.com.vemser.devlandapi.dto.TecnologiasDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.TecnologiasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tecnologia")
public class TecnologiasController implements TecnologiasDocs {

    @Autowired
    private TecnologiasService tecnologiasService;


    @PostMapping("/{idUsuario}")
    public ResponseEntity<TecnologiasDTO> adicionar(@PathVariable("idUsuario") Integer idUsuario,
                                                    @Valid @RequestBody TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(tecnologiasService.create(idUsuario, tecnologiasCreateDTO));
    }

    /*@PutMapping("/{idUsuario}")
    public ResponseEntity<TecnologiasDTO> editar(@PathVariable ("idUsuario") Integer idUsuario,
                                                    @Valid @RequestBody TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(tecnologiasService.editar(idUsuario, tecnologiasCreateDTO));
    }*/

    @DeleteMapping("/{nomeTecnologia}")
    public void delete(@PathVariable("nomeTecnologia") String nomeTecnologia) throws RegraDeNegocioException {
        tecnologiasService.delete(nomeTecnologia);
    }

}
