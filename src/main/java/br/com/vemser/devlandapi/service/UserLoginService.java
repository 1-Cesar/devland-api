package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.userlogin.UserLoginAuthDTO;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.enums.TipoStatus;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserLoginService {
    private final ObjectMapper objectMapper;
    @Autowired
    private UserLoginRepository userLoginRepository;

    public String criptografarSenha(String senha) {

        LdapShaPasswordEncoder ldapShaPasswordEncoder = new LdapShaPasswordEncoder();

        String criptografia = ldapShaPasswordEncoder.encode(senha);

        return criptografia;
    }

    public String trocarSenha (UserLoginAuthDTO userLoginAuthDTO, UserLoginEntity usuarioLogadoEntity) throws RegraDeNegocioException {
        if (userLoginAuthDTO.getLogin().equals(usuarioLogadoEntity.getLogin())) {
            usuarioLogadoEntity.setSenha(criptografarSenha(userLoginAuthDTO.getSenha()));
            userLoginRepository.save(usuarioLogadoEntity);
        } else {
            throw new RegraDeNegocioException("Usuário ou senha inválidos");
        }
        return "Senha Alterada com Sucesso !";
    }

    public Optional<UserLoginEntity> findByLoginAndSenha(String login, String senha) {
        return userLoginRepository.findByLoginAndSenha(login, senha);
    }


    public Optional<UserLoginEntity> findByLogin(String login) {
        return userLoginRepository.findByLogin(login);
    }


    public Integer getIdLoggedUser() {
        Integer findUserId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findUserId;
    }

    public UserLoginEntity getLoggedUser() throws RegraDeNegocioException {
        return this.findById(getIdLoggedUser());
    }

    public UserLoginEntity findById(Integer idAutenticacao) throws RegraDeNegocioException {
        return userLoginRepository.findById(idAutenticacao)
                .orElseThrow((() -> new RegraDeNegocioException("Usuario nao encontrado")));
    }


    public UserLoginEntity findByIdUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UserLoginEntity userLoginRecuperado = userLoginRepository.findAll().stream()
                .filter(usuario -> usuario.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        return userLoginRecuperado;
    }


    public String desativar(Integer idUsuario, TipoStatus opcao) throws RegraDeNegocioException {

        UserLoginEntity userLoginEntity = findByIdUsuario(idUsuario);

        String mudarStatus = opcao.toString();

        if (mudarStatus == "DESATIVAR") {
            userLoginEntity.setStatus(false);
            userLoginRepository.save(userLoginEntity);
            return "login Desativado!";

        } else {
            userLoginEntity.setStatus(true);
            userLoginRepository.save(userLoginEntity);
            return "login Ativado!";
        }

    }

}
