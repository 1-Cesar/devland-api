package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.UserLoginDTO;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLoginService {
    private final ObjectMapper objectMapper;
    @Autowired
    private UserLoginRepository userLoginRepository;

    public UserLoginDTO cadastrar(UserLoginCreateDTO userLoginCreateDTO){

        UserLoginEntity userLoginEntity = objectMapper.convertValue(userLoginCreateDTO,UserLoginEntity.class);

        LdapShaPasswordEncoder ldapShaPasswordEncoder = new LdapShaPasswordEncoder();

        userLoginEntity.setSenha(ldapShaPasswordEncoder.encode(userLoginCreateDTO.getSenha()));

        UserLoginEntity userSalvo= userLoginRepository.save(userLoginEntity);

        return objectMapper.convertValue(userSalvo, UserLoginDTO.class);
    }

    public Optional<UserLoginEntity> findByLoginAndSenha(String login, String senha) {
        return userLoginRepository.findByLoginAndSenha(login, senha);
    }


    public Optional<UserLoginEntity> findByLogin(String login) {
        return userLoginRepository.findByLogin(login);
    }

    public Integer getIdLoggedUser(){
        Integer findUserId = (Integer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findUserId;
    }

    public UserLoginEntity getLoggedUser() throws RegraDeNegocioException {
        return findById(getIdLoggedUser());
    }

    public UserLoginEntity findById(Integer idAutenticacao) throws RegraDeNegocioException{

        return userLoginRepository.findById(idAutenticacao)
                .orElseThrow((() -> new RegraDeNegocioException("Usuario nao encontrado")));
    }

}
