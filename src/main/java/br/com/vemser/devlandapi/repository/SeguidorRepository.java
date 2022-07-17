package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.config.ConexaoBancoDeDados;
import br.com.vemser.devlandapi.entity.Seguidor;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SeguidorRepository {

    @Autowired
    private ConexaoBancoDeDados connection;

    public Integer getProximoId(Connection connection) throws SQLException {

        String sql = "SELECT SEQ_SEGUIDOR.nextval mysequence from DUAL";

        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }
        return null;
    }

    public Seguidor adicionar(Integer id, Seguidor seguidor) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            Integer proximoId = this.getProximoId(con);


            seguidor.setId(proximoId);
            seguidor.setIdUsuario(id);

            String sql = "INSERT INTO SEGUIDOR\n" +
                    "(ID, ID_SEGUIDOR, NOME_SEGUIDOR, ID_USUARIO)\n" +
                    "VALUES(?, ?, ?, ?)\n";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, seguidor.getId());
            stmt.setInt(2, seguidor.getIdSeguidor());
            stmt.setString(3, seguidor.getNomeSeguidor());
            stmt.setInt(4, seguidor.getIdUsuario());

            stmt.executeUpdate();

            return seguidor;

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

    public List<Seguidor> listarSeguidor(Integer id) throws RegraDeNegocioException {
        List<Seguidor> seguidores = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();

            String sql = "SELECT * FROM SEGUIDOR WHERE ID_USUARIO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Seguidor seguidor = new Seguidor();
                seguidor.setId(res.getInt("id"));
                seguidor.setIdUsuario(res.getInt("id_usuario"));
                seguidor.setIdSeguidor(res.getInt("id_seguidor"));
                seguidor.setNomeSeguidor(res.getString("nome_seguidor"));
                seguidores.add(seguidor);
            }
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
        return seguidores;
    }

    public boolean VerificarSeguidor(Integer id, Integer idUsuario) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            String sql = "SELECT * FROM SEGUIDOR WHERE ID_SEGUIDOR = ? AND ID_USUARIO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.setInt(2, idUsuario);

            int res = stmt.executeUpdate();
            return res > 0 ;
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

    public boolean remover(Integer id, Integer idSeguidor) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            String sql4 = "DELETE FROM SEGUIDOR WHERE id_usuario = ? AND id_seguidor = ?";

            PreparedStatement stmt4 = con.prepareStatement(sql4);

            stmt4.setInt(1, id);
            stmt4.setInt(2, idSeguidor);

            int res4 = stmt4.executeUpdate();

            return res4 > 0;
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
}
