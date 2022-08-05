package br.com.vemser.devlandapi.controller;

import br.com.vemser.devlandapi.documentations.TecnologiasDocs;
import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasCreateDTO;
import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.service.TecnologiasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tecnologia")
public class TecnologiasController  {

    @Autowired
    private TecnologiasService tecnologiasService;


    @GetMapping("/minhas-tecnologias")
    public List<TecnologiasDTO> listarProprio() throws RegraDeNegocioException {
        return tecnologiasService.listarProprio();
    }

    @PostMapping
    public ResponseEntity<TecnologiasDTO> adicionar(@Valid @RequestBody TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException {
        return ResponseEntity.ok(tecnologiasService.create(tecnologiasCreateDTO));
    }

    @DeleteMapping("/{idTecnologia}")
    public void delete(@PathVariable("idTecnologia") Integer idTecnologia) throws RegraDeNegocioException {
        tecnologiasService.delete(idTecnologia);
    }
}
