package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.LogUsuario;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface LogUsuarioRepository extends MongoRepository<LogUsuario,Integer> {

    @Query(value = "{}", count = true)
    Long contaTodosUsuarios();

    @Query(value = "{tipoUsuario:DEV}", count = true)
    Long contaTodosDevs();

    @Query(value = "{tipoUsuario:EMPRESA}", count = true)
    Long contaTodasEmpresas();

    @Query(value = "{tipoUsuario:ADMIN}", count = true)
    Long contaTodosAdmins();
}
