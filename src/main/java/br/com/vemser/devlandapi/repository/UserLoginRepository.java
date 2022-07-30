package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.UserLoginEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginEntity, Integer> {
    Optional<UserLoginEntity> findByLoginAndSenha(String login, String senha);

    Optional<UserLoginEntity> findByLogin(String login);

}
