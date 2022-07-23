package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.TecnologiasEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TecnologiasRepository extends JpaRepository<TecnologiasEntity, Integer> {
}
