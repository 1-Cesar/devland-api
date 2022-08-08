package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.LogPostagem;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LogPostagemRepository extends MongoRepository<LogPostagem,Integer> {
}
