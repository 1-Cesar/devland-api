package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.SeguidorEntity;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeguidorRepository extends JpaRepository<SeguidorEntity, Integer> {

    //Verifica seguidor pra ver se segue a si
    @Query(" select s " +
            " from seguidor s" +
            " where s.idUsuario = :idUsuario AND s.idSeguidor = :idSeguidor")
    List<SeguidorEntity> verificaSeguidor(@Param("idUsuario") Integer idUsuario, @Param("idSeguidor") Integer idSeguidor);


    // Query listar paginado em Seguidores
    @Query(value = "select s " +
            "from seguidor s " +
            "where :idUsuario is null OR  s.idUsuario = :idUsuario")
    Page<SeguidorEntity> filtrarQuemUsuarioSegue(@Param("idUsuario") Integer idUsuario, PageRequest pageRequest);

}
