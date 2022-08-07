package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasCreateDTO;
import br.com.vemser.devlandapi.dto.tecnologias.TecnologiasDTO;
import br.com.vemser.devlandapi.entity.TecnologiasEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.TecnologiasRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class TecnologiasService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private TecnologiasRepository tecnologiasRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserLoginService userLoginService;


    public TecnologiasDTO create(TecnologiasCreateDTO tecnologiasCreateDTO) throws RegraDeNegocioException {

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        UsuarioEntity usuarioEntity = localizarUsuario(id);

        TecnologiasEntity tecnologiasEntity = retornarTecnologiaEntity(tecnologiasCreateDTO);

        tecnologiasEntity.setUsuario(usuarioEntity);

        return retornarDTO(tecnologiasRepository.save(tecnologiasEntity));
    }

    public void delete(Integer idTecnologia) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiaRecuperada = localizarTecnologiaById(idTecnologia);

        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        TecnologiasEntity tecnologia = localizarTecnologiaUsuarioLogado(id, tecnologiaRecuperada.getIdUsuario());

        tecnologiasRepository.delete(tecnologia);
    }

    public TecnologiasEntity localizarTecnologiaById(Integer id) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiaRecuperada = tecnologiasRepository.findById(id)
                .orElseThrow(() -> new RegraDeNegocioException("Valores inválidos"));
        return tecnologiaRecuperada;
    }

    public List<TecnologiasDTO> listarProprio() throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        return tecnologiasRepository.findAll().stream()
                .filter(tecnologias -> tecnologias.getIdUsuario().equals(id))
                .map(tecnologias -> objectMapper.convertValue(tecnologias, TecnologiasDTO.class))
                .collect(Collectors.toList());
    }

    public UsuarioEntity localizarUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioRecuperado = usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        return usuarioRecuperado;
    }

    public TecnologiasEntity localizarTecnologiaUsuarioLogado(Integer idContato, Integer idUsuario) throws RegraDeNegocioException {
        TecnologiasEntity tecnologiaRecuperada = tecnologiasRepository.findAll().stream()
                .filter(tecnologias -> tecnologias.getIdUsuario().equals(idContato) && tecnologias.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Id não localizado."));
        return tecnologiaRecuperada;
    }

    public TecnologiasDTO retornarDTO(TecnologiasEntity tecnologiasEntity) {
        return objectMapper.convertValue(tecnologiasEntity, TecnologiasDTO.class);
    }

    public TecnologiasEntity retornarTecnologiaEntity(TecnologiasCreateDTO tecnologiasCreateDTO) {
        return objectMapper.convertValue(tecnologiasCreateDTO, TecnologiasEntity.class);
    }
}
