package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer> {

    @Query(" select c" +
            " from usuario c" +
            " where c.tipoUsuario = :tipoUsuario")
    Page<UsuarioEntity> getUsuarioByTipo(@Param("tipoUsuario") TipoUsuario tipoUsuario, Pageable pageable);

}
