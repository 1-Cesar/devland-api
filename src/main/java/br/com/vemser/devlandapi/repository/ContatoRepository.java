package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.config.ConexaoBancoDeDados;
import br.com.vemser.devlandapi.entity.Contato;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ContatoRepository {

    @Autowired
    private ConexaoBancoDeDados connection;

    public Integer getProximoId(Connection connection) throws SQLException {

        String sql = "SELECT SEQ_CONTATO.nextval mysequence from DUAL";

        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }
        return null;
    }

    public Contato adicionar(Integer id, Contato contato) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            Integer proximoId = this.getProximoId(con);

            contato.setIdContato(proximoId);

            String sql = "INSERT INTO CONTATO\n" +
                    "(ID_CONTATO, ID_USUARIO, TIPO, NUMERO, DESCRICAO)\n" +
                    "VALUES(?, ?, ?, ?, ?)\n";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, contato.getIdContato());
            stmt.setInt(2, id);
            stmt.setInt(3, contato.getTipo().getTipo());
            stmt.setString(4, contato.getNumero());
            stmt.setString(5, contato.getDescricao());

            stmt.executeUpdate();

            contato.setIdUsuario(id);
            return contato;

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

    public List<Contato> listarContato(Integer id) throws RegraDeNegocioException {
        List<Contato> contatos = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            //Statement stmt = con.createStatement();

            String sql = "SELECT * FROM CONTATO WHERE ID_CONTATO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Contato contato = new Contato();
                contato.setIdContato(res.getInt("id_contato"));
                contato.setIdUsuario(res.getInt("id_usuario"));
                contato.setTipo(TipoClassificacao.ofTipo(res.getInt("tipo")));
                contato.setNumero(res.getString("numero"));
                contato.setDescricao(res.getString("descricao"));
                contatos.add(contato);
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
        return contatos;
    }

    public List<Contato> listarContatoUsuario(Integer id) throws RegraDeNegocioException {
        List<Contato> contatos = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            //Statement stmt = con.createStatement();

            String sql = "SELECT * FROM CONTATO WHERE ID_USUARIO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Contato contato = new Contato();
                contato.setIdContato(res.getInt("id_contato"));
                contato.setIdUsuario(res.getInt("id_usuario"));
                contato.setTipo(TipoClassificacao.ofTipo(res.getInt("tipo")));
                contato.setNumero(res.getString("numero"));
                contato.setDescricao(res.getString("descricao"));
                contatos.add(contato);
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
        return contatos;
    }

    public List<Contato> listar() throws RegraDeNegocioException {
        List<Contato> contatos = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            Statement stmt = con.createStatement();

            String sql = "SELECT * FROM CONTATO";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Contato contato = new Contato();
                contato.setIdContato(res.getInt("id_contato"));
                contato.setIdUsuario(res.getInt("id_usuario"));
                contato.setTipo(TipoClassificacao.ofTipo(res.getInt("tipo")));
                contato.setNumero(res.getString("numero"));
                contato.setDescricao(res.getString("descricao"));
                contatos.add(contato);
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
        return contatos;
    }

    public Contato editar(Integer id, Contato contato) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE CONTATO SET ");
            sql.append(" tipo = ?,");
            sql.append(" numero = ?,");
            sql.append(" descricao = ?");
            sql.append(" WHERE id_contato = ? ");

            PreparedStatement stmt = con.prepareStatement(sql.toString());

            stmt.setInt(1, contato.getTipo().getTipo());
            stmt.setString(2, contato.getNumero());
            stmt.setString(3, contato.getDescricao());
            stmt.setInt(4, id);


            // Executa-se a consulta
            int res = stmt.executeUpdate();

            contato.setIdContato(id);

            return contato;
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

    public boolean remover(Integer id) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            String sql = "DELETE FROM CONTATO WHERE id_contato = ?";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, id);

            // Executa-se a consulta
            int res = stmt.executeUpdate();

            return res > 0;
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
