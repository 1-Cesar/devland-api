package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.*;
import br.com.vemser.devlandapi.entity.TecnologiasEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;

import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.TecnologiasRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class TecnologiasService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TecnologiasRepository tecnologiasRepository;

    @Autowired
    private EmailService emailService;

    public void delete(String tecnologia) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiasEntity = localizarTecnologiaByNome(tecnologia);
        tecnologiasRepository.delete(tecnologiasEntity);
    }

    public TecnologiasDTO editar(Integer id, TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiasEntity = localizarTecnologiaById(id);
        localizarTecnologiaByNome(tecnologiasCreateDTO.getNomeTecnologia());

        tecnologiasEntity.setNomeTecnologia(tecnologiasCreateDTO.getNomeTecnologia());

        return retornarDTO(tecnologiasEntity);
    }

    public TecnologiasDTO create(Integer id, TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException {
        UsuarioEntity usuarioEntity = usuarioService.localizarUsuario(id);
        TecnologiasEntity tecnologiasEntity = retornarTecnologiaEntity(tecnologiasCreateDTO);

        tecnologiasEntity.setUsuario(usuarioEntity);

        tecnologiasRepository.save(tecnologiasEntity);

        return retornarDTO(tecnologiasEntity);
    }

    public TecnologiasEntity localizarTecnologiaByNome(String tecnologia) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiaRecuperada = tecnologiasRepository.findAll().stream()
                .filter(tecnologiasEntity -> tecnologiasEntity.getNomeTecnologia().equals(tecnologia))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Tecnologia não encontrada"));
        return tecnologiaRecuperada;
    }

    public TecnologiasEntity localizarTecnologiaById(Integer id) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiaRecuperada = tecnologiasRepository.findById(id).stream()
                .filter(tecnologiasEntity -> tecnologiasEntity.getIdTecnologia().equals(id))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Nome de tecnologia não encontrado"));
        return tecnologiaRecuperada;
    }

    public TecnologiasDTO retornarDTO(TecnologiasEntity tecnologiasEntity) {
        return objectMapper.convertValue(tecnologiasEntity, TecnologiasDTO.class);
    }

    public TecnologiasEntity retornarTecnologiaEntity(TecnologiasCreateDTO tecnologiasCreateDTO) {
        return objectMapper.convertValue(tecnologiasCreateDTO, TecnologiasEntity.class);
    }
}
