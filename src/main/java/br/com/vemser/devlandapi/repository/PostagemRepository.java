package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public interface PostagemRepository extends JpaRepository<PostagemEntity, Integer> {
    @Query("select p " +
            "from postagem p " +
            "where :tipoPostagem is null OR  p.tipoPostagem = :tipoPostagem")
    Set<PostagemEntity> filtrarPorTipo(@Param("tipoPostagem") TipoPostagem tipoPostagem);
}

