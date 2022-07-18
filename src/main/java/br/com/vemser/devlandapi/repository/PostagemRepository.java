package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.config.ConexaoBancoDeDados;
import br.com.vemser.devlandapi.entity.Postagem;
import br.com.vemser.devlandapi.enums.TipoPostagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostagemRepository {

    @Autowired
    private ConexaoBancoDeDados connection;

    public Integer getProximoId(Connection connection) throws SQLException, RegraDeNegocioException {
        try {
            String sql = " SELECT seq_postagem.nextval mysequence from DUAL ";

            Statement stmt = connection.createStatement();
            ResultSet res = stmt.executeQuery(sql);

            if (res.next()) {
                return res.getInt("mysequence");
            }
            return null;
        } catch (SQLException e) {
            throw new RegraDeNegocioException("Erro ao acessar o banco de dados");
        }
    }

    public List<Postagem> list() throws RegraDeNegocioException {
        List<Postagem> postagens = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();

            Statement stmt = con.createStatement();

            String sql = " SELECT * FROM POSTAGEM ";

            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Postagem postagem = getPostagemFromResultSet(res);
                postagens.add(postagem);
            }
            return postagens;
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if(con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Postagem> listByTipo(Integer tipoPostagem) throws RegraDeNegocioException {
        List<Postagem> postagens = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();

            String sql = "SELECT P.*, " +
                    "            U.NOME AS NOME_USUARIO " +
                    "       FROM POSTAGEM P" +
                    "  INNER JOIN USUARIO U ON (P.ID_USUARIO = U.ID_USUARIO)  " +
                    "      WHERE P.TIPO = ? ";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, tipoPostagem);

            ResultSet res = stmt.executeQuery();

            while(res.next()) {
                Postagem postagem = getPostagemFromResultSet(res);
                postagens.add(postagem);
            }
            return postagens;
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Postagem post(Postagem postagem) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            Integer proximoId = getProximoId(con);

            postagem.setIdPostagem(proximoId);

            String sql = " INSERT INTO POSTAGEM\n " +
                    " (ID_POSTAGEM, ID_USUARIO, TIPO, TITULO, DESCRICAO, FOTO, LIKES, DATA_POSTAGEM)\n " +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?)\n ";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, postagem.getIdPostagem());
            stmt.setInt(2, postagem.getIdUsuario());
            stmt.setInt(3, postagem.getTipoPostagem().getTipo());
            stmt.setString(4, postagem.getTitulo());
            stmt.setString(5, postagem.getDescricao());
            stmt.setString(6, postagem.getFoto());
            stmt.setInt(7, postagem.getCurtidas());
            java.util.Date data = Date.from(postagem.getData().atZone(ZoneId.systemDefault()).toInstant());
            stmt.setDate(8, new java.sql.Date(data.getTime()));

            stmt.executeUpdate();

            return postagem;
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Postagem curtir(Postagem postagem) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE POSTAGEM SET ");
            sql.append(" likes = ? ");
            sql.append(" WHERE id_postagem = ? ");

            PreparedStatement stmt = con.prepareStatement(sql.toString());
            stmt.setInt(1, postagem.getCurtidas());
            stmt.setInt(2, postagem.getIdPostagem());

            stmt.executeUpdate();

            return postagem;
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Postagem update(Integer idPostagem, Postagem postagem) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append(" UPDATE POSTAGEM SET ");
            sql.append(" tipo = ?, ");
            sql.append(" titulo = ?, ");
            sql.append(" descricao = ?, ");
            sql.append(" foto = ? ");
            sql.append(" WHERE id_postagem = ? ");

            PreparedStatement stmt = con.prepareStatement(sql.toString());

            stmt.setInt(1, postagem.getTipoPostagem().getTipo());
            stmt.setString(2, postagem.getTitulo());
            stmt.setString(3, postagem.getDescricao());
            stmt.setString(4, postagem.getFoto());
            stmt.setInt(5, idPostagem);

            stmt.executeUpdate();
            return postagem;
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void delete(Integer idPostagem) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            String sql = " DELETE FROM POSTAGEM WHERE id_postagem = ? ";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idPostagem);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Postagem findByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
        Connection con;
        try {
            con = connection.getConnection();

            String sql = " SELECT * FROM POSTAGEM WHERE id_postagem = ? ";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idPostagem);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return getPostagemFromResultSet(result);
            }
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        }
        return null;
    }

    private Postagem getPostagemFromResultSet(ResultSet result) throws SQLException {
        Postagem postagem = new Postagem();
        postagem.setIdPostagem(result.getInt("id_postagem"));
        postagem.setIdUsuario(result.getInt("id_usuario"));
        postagem.setTipoPostagem(TipoPostagem.ofTema(result.getInt("tipo")));
        postagem.setTitulo(result.getString("titulo"));
        postagem.setDescricao(result.getString("descricao"));
        postagem.setCurtidas(result.getInt("likes"));
        postagem.setFoto(result.getString("foto"));
        Timestamp dataPostagem = result.getTimestamp("data_postagem");
        if(dataPostagem != null) {
            postagem.setData(dataPostagem.toLocalDateTime());
        }
        return postagem;
    }

}

