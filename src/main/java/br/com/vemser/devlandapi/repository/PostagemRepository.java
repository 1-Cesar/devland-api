package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.dto.RelatorioPostagemDTO;
import br.com.vemser.devlandapi.entity.PostagemEntity;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import feign.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @Query(value = "select p " +
            "from postagem p " +
            "where :tipoPostagem is null OR  p.tipoPostagem = :tipoPostagem")
    Page<PostagemEntity> filtrarPorTipo(@Param("tipoPostagem") TipoPostagem tipoPostagem, PageRequest pageRequest);

    @Query(value = "select new br.com.vemser.devlandapi.dto.RelatorioPostagemDTO(" +
            " u.nome," +
            " p.titulo," +
            " p.tipoPostagem," +
            " p.data," +
            " p.descricao," +
            " p.curtidas," +
            " c" +
            ") " +
            " from postagem p " +
            " left join p.comentarios c " +
            " left join p.usuario u " +
            " where (:tipoPostagem is null OR p.tipoPostagem = :tipoPostagem )")
    Page<RelatorioPostagemDTO> relatorioPostagem(@Param("tipoPostagem") TipoPostagem tipoPostagem, Pageable pageable);
}



