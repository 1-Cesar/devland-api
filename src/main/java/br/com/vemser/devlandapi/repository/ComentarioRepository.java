package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.dto.comentario.ComentarioDTO;
import br.com.vemser.devlandapi.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Integer> {
    @Query("SELECT c FROM comentario c " +
            "WHERE c.idPostagem = ?1")
    List<ComentarioEntity> findByidPostagem(Integer idPostagem);

}
