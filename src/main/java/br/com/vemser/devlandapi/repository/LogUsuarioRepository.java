package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.LogUsuario;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogUsuarioRepository extends MongoRepository<LogUsuario,Integer> {
}
