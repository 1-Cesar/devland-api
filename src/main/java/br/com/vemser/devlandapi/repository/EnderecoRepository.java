package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.EnderecoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EnderecoRepository extends JpaRepository<EnderecoEntity, Integer> {

    @Query(" select e" +
            " from endereco e" +
            " where e.pais = :pais")
    Page<EnderecoEntity> paginacaoPais(@Param("pais") String pais, Pageable pageable);

}
