package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.config.ConexaoBancoDeDados;
import br.com.vemser.devlandapi.entity.Endereco;
import br.com.vemser.devlandapi.enums.TipoClassificacao;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnderecoRepository {

    @Autowired
    private ConexaoBancoDeDados connection;

    public Integer getProximoId(Connection connection) throws SQLException {

        String sql = "SELECT SEQ_ENDERECO.nextval mysequence from DUAL";

        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }
        return null;
    }

    public Endereco adicionar(Integer id, Endereco endereco) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            Integer proximoId = this.getProximoId(con);

            endereco.setIdEndereco(proximoId);

            String sql = "INSERT INTO ENDERECO\n" +
                    "(ID_ENDERECO, ID_USUARIO, TIPO, LOGRADOURO, NUMERO, COMPLEMENTO, CEP, CIDADE, ESTADO, PAIS)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, endereco.getIdEndereco());
            stmt.setInt(2, id);
            stmt.setInt(3, endereco.getTipo().getTipo());
            stmt.setString(4, endereco.getLogradouro());
            stmt.setString(5, endereco.getNumero());
            stmt.setString(6, endereco.getComplemento());
            stmt.setString(7, endereco.getCep());
            stmt.setString(8, endereco.getCidade());
            stmt.setString(9, endereco.getEstado());
            stmt.setString(10, endereco.getPais());

            stmt.executeUpdate();
            endereco.setIdUsuario(id);
            return endereco;

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

    public List<Endereco> listarEndereco(Integer id) throws RegraDeNegocioException {
        List<Endereco> enderecos = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            //Statement stmt = con.createStatement();

            String sql = "SELECT * FROM ENDERECO WHERE ID_ENDERECO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Endereco endereco = new Endereco();
                endereco.setIdEndereco(res.getInt("id_endereco"));
                endereco.setIdUsuario(res.getInt("id_usuario"));
                endereco.setTipo(TipoClassificacao.ofTipo(res.getInt("tipo")));
                endereco.setLogradouro(res.getString("logradouro"));
                endereco.setNumero(res.getString("numero"));
                endereco.setComplemento(res.getString("complemento"));
                endereco.setCep(res.getString("cep"));
                endereco.setCidade(res.getString("cidade"));
                endereco.setEstado(res.getString("estado"));
                endereco.setPais(res.getString("pais"));
                enderecos.add(endereco);
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
        return enderecos;
    }

    public List<Endereco> listarEnderecoUsuario(Integer id) throws RegraDeNegocioException {
        List<Endereco> enderecos = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            //Statement stmt = con.createStatement();

            String sql = "SELECT * FROM ENDERECO WHERE ID_USUARIO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Endereco endereco = new Endereco();
                endereco.setIdEndereco(res.getInt("id_endereco"));
                endereco.setIdUsuario(res.getInt("id_usuario"));
                endereco.setTipo(TipoClassificacao.ofTipo(res.getInt("tipo")));
                endereco.setLogradouro(res.getString("logradouro"));
                endereco.setNumero(res.getString("numero"));
                endereco.setComplemento(res.getString("complemento"));
                endereco.setCep(res.getString("cep"));
                endereco.setCidade(res.getString("cidade"));
                endereco.setEstado(res.getString("estado"));
                endereco.setPais(res.getString("pais"));
                enderecos.add(endereco);
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
        return enderecos;
    }

    public List<Endereco> listar() throws RegraDeNegocioException {
        List<Endereco> enderecos = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            Statement stmt = con.createStatement();

            String sql = "SELECT * FROM ENDERECO";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Endereco endereco = new Endereco();
                endereco.setIdEndereco(res.getInt("id_endereco"));
                endereco.setIdUsuario(res.getInt("id_usuario"));
                endereco.setTipo(TipoClassificacao.ofTipo(res.getInt("tipo")));
                endereco.setLogradouro(res.getString("logradouro"));
                endereco.setNumero(res.getString("numero"));
                endereco.setComplemento(res.getString("complemento"));
                endereco.setCep(res.getString("cep"));
                endereco.setCidade(res.getString("cidade"));
                endereco.setEstado(res.getString("estado"));
                endereco.setPais(res.getString("pais"));
                enderecos.add(endereco);
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
        return enderecos;
    }

    public Endereco editar(Integer id, Endereco endereco) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ENDERECO SET ");
            sql.append(" tipo = ?,");
            sql.append(" logradouro = ?,");
            sql.append(" numero = ?,");
            sql.append(" complemento = ?,");
            sql.append(" cep = ?,");
            sql.append(" cidade = ?,");
            sql.append(" estado = ?,");
            sql.append(" pais = ?");
            sql.append(" WHERE id_endereco = ? ");

            PreparedStatement stmt = con.prepareStatement(sql.toString());

            stmt.setInt(1, endereco.getTipo().getTipo());
            stmt.setString(2, endereco.getLogradouro());
            stmt.setString(3, endereco.getNumero());
            stmt.setString(4, endereco.getComplemento());
            stmt.setString(5, endereco.getCep());
            stmt.setString(6, endereco.getCidade());
            stmt.setString(7, endereco.getEstado());
            stmt.setString(8, endereco.getPais());
            stmt.setInt(9, id);


            // Executa-se a consulta
            int res = stmt.executeUpdate();

            endereco.setIdEndereco(id);

            return endereco;
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

            String sql = "DELETE FROM ENDERECO WHERE id_endereco = ?";

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
