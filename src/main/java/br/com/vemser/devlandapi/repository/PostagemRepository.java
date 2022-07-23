package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface PostagemRepository extends JpaRepository<PostagemEntity, Integer> {
    @Query("select p from postagem p where p.tipoPostagem = ?1")
    List<PostagemEntity> filtrarPorTipo(TipoPostagem tipoPostagem);

}

