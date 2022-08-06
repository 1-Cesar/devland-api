package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.ContatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContatoRepository extends JpaRepository<ContatoEntity, Integer> {

    public ContatoEntity findByIdContatoAndIdUsuario(Integer idContato, Integer idUsuario);
}
