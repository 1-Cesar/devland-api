package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.config.ConexaoBancoDeDados;
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
    @Query("select p from Postagem p where p.tipoPostagem = ?1")
    List<PostagemEntity> filtrarPorTipo(TipoPostagem tipoPostagem);


//    public Integer getProximoId(Connection connection) throws SQLException, RegraDeNegocioException {
//        try {
//            String sql = " SELECT seq_postagem.nextval mysequence from DUAL ";
//
//            Statement stmt = connection.createStatement();
//            ResultSet res = stmt.executeQuery(sql);
//
//            if (res.next()) {
//                return res.getInt("mysequence");
//            }
//            return null;
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException("Erro ao acessar o banco de dados");
//        }
//    }
//
//    private void closeConnection(Connection con) {
//        try {
//            if (con != null) {
//                con.close();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public List<PostagemEntity> list() throws RegraDeNegocioException {
//        List<PostagemEntity> postagens = new ArrayList<>();
//        Connection con = null;
//        try {
//            con = connection.getConnection();
//
//            Statement stmt = con.createStatement();
//
//            String sql = " SELECT * FROM POSTAGEM ";
//
//            ResultSet res = stmt.executeQuery(sql);
//
//            while (res.next()) {
//                PostagemEntity postagemEntity = getPostagemFromResultSet(res);
//                postagens.add(postagemEntity);
//            }
//            return postagens;
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        } finally {
//            closeConnection(con);
//        }
//    }
//
//
//    public List<PostagemEntity> listByTipo(Integer tipoPostagem) throws RegraDeNegocioException {
//        List<PostagemEntity> postagens = new ArrayList<>();
//        Connection con = null;
//        try {
//            con = connection.getConnection();
//
//            String sql = "SELECT P.*, " +
//                    "            U.NOME AS NOME_USUARIO " +
//                    "       FROM POSTAGEM P" +
//                    "  INNER JOIN USUARIO U ON (P.ID_USUARIO = U.ID_USUARIO)  " +
//                    "      WHERE P.TIPO = ? ";
//
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setInt(1, tipoPostagem);
//
//            ResultSet res = stmt.executeQuery();
//
//            while (res.next()) {
//                PostagemEntity postagemEntity = getPostagemFromResultSet(res);
//                postagens.add(postagemEntity);
//            }
//            return postagens;
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        } finally {
//            closeConnection(con);
//        }
//    }
//
//    public PostagemEntity post(PostagemEntity postagemEntity) throws RegraDeNegocioException {
//        Connection con = null;
//        try {
//            con = connection.getConnection();
//
//            Integer proximoId = getProximoId(con);
//
//            postagemEntity.setIdPostagem(proximoId);
//
//            String sql = " INSERT INTO POSTAGEM\n " +
//                    " (ID_POSTAGEM, ID_USUARIO, TIPO, TITULO, DESCRICAO, FOTO, LIKES, DATA_POSTAGEM)\n " +
//                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?)\n ";
//
//            PreparedStatement stmt = con.prepareStatement(sql);
//
//            stmt.setInt(1, postagemEntity.getIdPostagem());
//            stmt.setInt(2, postagemEntity.getIdUsuario());
//            stmt.setInt(3, postagemEntity.getTipoPostagem().getTipo());
//            stmt.setString(4, postagemEntity.getTitulo());
//            stmt.setString(5, postagemEntity.getDescricao());
//            stmt.setString(6, postagemEntity.getFoto());
//            stmt.setInt(7, postagemEntity.getCurtidas());
//            java.util.Date data = Date.from(postagemEntity.getData().atZone(ZoneId.systemDefault()).toInstant());
//            stmt.setDate(8, new java.sql.Date(data.getTime()));
//
//            stmt.executeUpdate();
//
//            return postagemEntity;
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        } finally {
//            closeConnection(con);
//        }
//    }
//
//    public PostagemEntity curtir(PostagemEntity postagemEntity) throws RegraDeNegocioException {
//        Connection con = null;
//        try {
//            con = connection.getConnection();
//
//            StringBuilder sql = new StringBuilder();
//            sql.append(" UPDATE POSTAGEM SET ");
//            sql.append(" likes = ? ");
//            sql.append(" WHERE id_postagem = ? ");
//
//            PreparedStatement stmt = con.prepareStatement(sql.toString());
//            stmt.setInt(1, postagemEntity.getCurtidas());
//            stmt.setInt(2, postagemEntity.getIdPostagem());
//
//            stmt.executeUpdate();
//
//            return postagemEntity;
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        } finally {
//            closeConnection(con);
//        }
//    }
//
//    public PostagemEntity update(Integer idPostagem, PostagemEntity postagemEntity) throws RegraDeNegocioException {
//        Connection con = null;
//        try {
//            con = connection.getConnection();
//
//            StringBuilder sql = new StringBuilder();
//            sql.append(" UPDATE POSTAGEM SET ");
//            sql.append(" tipo = ?, ");
//            sql.append(" titulo = ?, ");
//            sql.append(" descricao = ?, ");
//            sql.append(" foto = ? ");
//            sql.append(" WHERE id_postagem = ? ");
//
//            PreparedStatement stmt = con.prepareStatement(sql.toString());
//
//            stmt.setInt(1, postagemEntity.getTipoPostagem().getTipo());
//            stmt.setString(2, postagemEntity.getTitulo());
//            stmt.setString(3, postagemEntity.getDescricao());
//            stmt.setString(4, postagemEntity.getFoto());
//            stmt.setInt(5, idPostagem);
//
//            stmt.executeUpdate();
//            return postagemEntity;
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        } finally {
//            closeConnection(con);
//        }
//    }
//
//    public void delete(Integer idPostagem) throws RegraDeNegocioException {
//        Connection con = null;
//        try {
//            con = connection.getConnection();
//
//            String sql = " DELETE FROM POSTAGEM WHERE id_postagem = ? ";
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setInt(1, idPostagem);
//
//            stmt.executeUpdate();
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        } finally {
//            closeConnection(con);
//        }
//    }
//
//    public PostagemEntity findByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
//        Connection con;
//        try {
//            con = connection.getConnection();
//
//            String sql = " SELECT * FROM POSTAGEM WHERE id_postagem = ? ";
//
//            PreparedStatement stmt = con.prepareStatement(sql);
//            stmt.setInt(1, idPostagem);
//
//            ResultSet result = stmt.executeQuery();
//
//            if (result.next()) {
//                return getPostagemFromResultSet(result);
//            }
//        } catch (SQLException e) {
//            throw new RegraDeNegocioException(e.getMessage());
//        }
//        return null;
//    }
//
//    private PostagemEntity getPostagemFromResultSet(ResultSet result) throws SQLException {
//        PostagemEntity postagemEntity = new PostagemEntity();
//        postagemEntity.setIdPostagem(result.getInt("id_postagem"));
//        postagemEntity.setIdUsuario(result.getInt("id_usuario"));
//        postagemEntity.setTipoPostagem(TipoPostagem.ofTema(result.getInt("tipo")));
//        postagemEntity.setTitulo(result.getString("titulo"));
//        postagemEntity.setDescricao(result.getString("descricao"));
//        postagemEntity.setCurtidas(result.getInt("likes"));
//        postagemEntity.setFoto(result.getString("foto"));
//        Timestamp dataPostagem = result.getTimestamp("data_postagem");
//        if (dataPostagem != null) {
//            postagemEntity.setData(dataPostagem.toLocalDateTime());
//        }
//        return postagemEntity;
//    }

}

