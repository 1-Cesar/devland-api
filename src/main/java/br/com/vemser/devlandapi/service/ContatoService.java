package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.ContatoCreateDTO;
import br.com.vemser.devlandapi.dto.ContatoDTO;
import br.com.vemser.devlandapi.dto.UsuarioDTO;
import br.com.vemser.devlandapi.entity.ContatoEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.ContatoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContatoService {

    @Autowired
    private ContatoRepository contatoRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioService usuarioService;


    //==================================================================================================================
    //LIST ALL
    public List<ContatoDTO> listar() throws RegraDeNegocioException { //todo acho que deu certo
        if (contatoRepository.findAll().size() == 0) {
            throw new RegraDeNegocioException("Nenhum contato encontrado");
        } else {
            return contatoRepository.findAll().stream()
                    .map(this::retornarContatoDTO)//converte dto através de método
                    .collect(Collectors.toList());
        }
    }

    //==================================================================================================================
    //LIST CONTATO todo revisar
    public List<ContatoDTO> listarContatoPorId(Integer id) throws RegraDeNegocioException {
        localizarContato(id);
        return contatoRepository.findById(id).stream()
                .filter(contato -> contato.getIdContato().equals(id))
                .map(this::retornarContatoDTO)//converte dto através de método
                .collect(Collectors.toList());
    }

    //==================================================================================================================
    //LIST CONTATO USUÁRIO todo revisar

    public List<ContatoDTO> listarContatoUsuario(Integer id) throws RegraDeNegocioException {
        usuarioService.localizarUsuario(id);
        return contatoRepository.findById(id).stream()
                .filter(contato -> contato.getIdUsuario().equals(id))
                .map(this::retornarContatoDTO)
                .collect(Collectors.toList());
    }

    //==================================================================================================================
    //ADICIONAR

    public ContatoCreateDTO adicionar(Integer id, ContatoCreateDTO contatoDTO) throws RegraDeNegocioException {
        //recupera usuário
        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(id);

        //converte
        ContatoEntity contatoEntity = retornarContatoEntity(contatoDTO);

        //seta no usuário
        contatoEntity.setUsuario(usuarioRecuperado);

        ContatoEntity contatoCriado = contatoRepository.save(contatoEntity);

        return retornarContatoDTO(contatoCriado);
    }

    //==================================================================================================================
    //EDITAR

    public ContatoDTO editar(Integer id,
                             ContatoDTO contatoDTO) throws RegraDeNegocioException {
        ContatoEntity contatoRecuperado = localizarContato(id);

        UsuarioEntity usuarioEntity = contatoRecuperado.getUsuario();
        usuarioEntity.setContatos(null);


        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(contatoDTO.getIdUsuario());

        contatoRecuperado.setTipo(contatoDTO.getTipo());
        contatoRecuperado.setNumero(contatoDTO.getNumero());
        contatoRecuperado.setDescricao(contatoDTO.getDescricao());
        contatoRecuperado.setUsuario(usuarioRecuperado);
        usuarioRecuperado.setContatos(Set.of(contatoRecuperado));
        usuarioService.adicionar(retornarUsuarioDTO(usuarioRecuperado));


        if (! usuarioRecuperado.getIdUsuario() .equals(usuarioEntity.getIdUsuario())) {
            usuarioService.adicionar(retornarUsuarioDTO(usuarioEntity));
        }


        return retornarContatoDTO(contatoRepository.save(contatoRecuperado));


    }


    //==================================================================================================================
    //EXCLUIR
    public void remover(Integer id) throws RegraDeNegocioException {

        ContatoEntity contatoEntityRecuperado = localizarContato(id);
        UsuarioEntity usuarioRecuperado = usuarioService.localizarUsuario(contatoEntityRecuperado.getIdUsuario());

       // log
        contatoRepository.delete(contatoEntityRecuperado);
    }


    //==================================================================================================================
    //LOCALIZAR CONTATO
    public ContatoEntity localizarContato (Integer idContato) throws RegraDeNegocioException {
        ContatoEntity contatoRecuperado = contatoRepository.findAll().stream()
                .filter(contato -> contato.getIdContato().equals(idContato))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Contato não encontrado"));
        return contatoRecuperado;
    }

    //DTO PARA ENTITY
    public ContatoEntity retornarContatoEntity(ContatoCreateDTO contatoCreateDTO) {
        return objectMapper.convertValue(contatoCreateDTO, ContatoEntity.class);
    }

    //ENTITY PARA DTO
    public ContatoDTO retornarContatoDTO(ContatoEntity contato) {
        return objectMapper.convertValue(contato, ContatoDTO.class);
    }

    public UsuarioDTO retornarUsuarioDTO(UsuarioEntity usuario) {
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }


}
