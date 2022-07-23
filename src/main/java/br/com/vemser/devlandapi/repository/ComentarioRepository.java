package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Integer> {

}
