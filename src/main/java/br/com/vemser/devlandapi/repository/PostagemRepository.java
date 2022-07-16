package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.Postagem;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class PostagemRepository {

    @Autowired
    private Connection connection;

    public Integer getProximoId(Connection connection) throws SQLException {
        try {
            String sql = "SELECT seq_postagem.nextval mysequence from DUAL";

            Statement stmt = connection.createStatement();

            ResultSet res = stmt.executeQuery(sql);

            if (res.next()) {
                return res.getInt("mysequence");
            }
            return null;
        } catch (SQLException e) {
            throw new SQLException(e.getCause());
        }
    }

    public Postagem post(Postagem postagem) throws RegraDeNegocioException {
        try {

            Integer proximoId = getProximoId(connection);
            postagem.setIdPostagem(proximoId);

            String sql = " INSERT INTO POSTAGEM\n " +
                    " (ID_POSTAGEM, ID_USUARIO, TIPO, TITULO, DESCRICAO, UPS, DOWNS, VIEWS, DATA_POSTAGEM)\n " +
                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)\n ";

            PreparedStatement stmt = connection.prepareStatement(sql);

            stmt.setInt(1, postagem.getIdPostagem());
            stmt.setInt(2, postagem.getIdUsuario());
            stmt.setInt(3, postagem.getTipoPostagem().getTipo());
            stmt.setString(4, postagem.getTitulo());
            stmt.setString(5, postagem.getDescricao());
            stmt.setInt(6, postagem.getUps());
            stmt.setInt(7, postagem.getDowns());
            stmt.setInt(8, postagem.getViews());
            stmt.setDate(9, postagem.getData());

            int res = stmt.executeUpdate();
            return postagem;
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

