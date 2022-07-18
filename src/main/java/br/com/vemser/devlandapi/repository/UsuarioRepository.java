package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.config.ConexaoBancoDeDados;

import br.com.vemser.devlandapi.entity.Usuario;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class UsuarioRepository {

    @Autowired
    private ConexaoBancoDeDados connection;

    public Integer getProximoId(Connection connection) throws SQLException {

        String sql = "SELECT SEQ_USUARIO.nextval mysequence from DUAL";

        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }
        return null;
    }

    public Usuario adicionar(Usuario usuario) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            Integer proximoId = this.getProximoId(con);

            usuario.setIdUsuario(proximoId);

            String sql = "INSERT INTO USUARIO\n" +
                    "(ID_USUARIO, NOME, EMAIL, AREA_ATUACAO, CPF_CNPJ, TIPO, FOTO)\n" +
                    "VALUES(?, ?, ?, ?, ?, ?, ?)\n";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, usuario.getIdUsuario());
            stmt.setString(2, usuario.getNome());
            stmt.setString(3, usuario.getEmail());
            stmt.setString(4, usuario.getAreaAtuacao());
            stmt.setString(5, usuario.getCpfCnpj());
            stmt.setInt(6, usuario.getTipoUsuario().getTipo());
            stmt.setString(7, usuario.getFoto());

            stmt.executeUpdate();

            return usuario;

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

    public List<Usuario> listarUsuario(Integer id) throws RegraDeNegocioException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            //Statement stmt = con.createStatement();

            String sql = "SELECT * FROM USUARIO WHERE ID_USUARIO = ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                Usuario usuario = getUsuario(res);
                usuarios.add(usuario);
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
        return usuarios;
    }

    public List<Usuario> listar() throws RegraDeNegocioException {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        try {
            con = connection.getConnection();
            Statement stmt = con.createStatement();

            String sql = "SELECT * FROM USUARIO";

            // Executa-se a consulta
            ResultSet res = stmt.executeQuery(sql);

            while (res.next()) {
                Usuario usuario = getUsuario(res);
                usuarios.add(usuario);
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
        return usuarios;
    }

    public Usuario getUsuario(ResultSet res) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(res.getInt("id_usuario"));
        usuario.setNome(res.getString("nome"));
        usuario.setTipoUsuario(TipoUsuario.ofTipo(res.getInt("tipo")));
        usuario.setAreaAtuacao(res.getString("area_atuacao"));
        usuario.setEmail(res.getString("email"));
        usuario.setCpfCnpj(res.getString("cpf_cnpj"));
        usuario.setFoto(res.getString("foto"));
        return usuario;
    }

    public Usuario editar(Integer id, Usuario usuario) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = connection.getConnection();

            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE USUARIO SET ");
            sql.append(" nome = ?,");
            sql.append(" cpf_cnpj = ?,");
            sql.append(" area_atuacao = ?,");
            sql.append(" foto = ?,");
            sql.append(" email = ?");
            sql.append(" WHERE id_usuario = ? ");

            PreparedStatement stmt = con.prepareStatement(sql.toString());

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpfCnpj());
            stmt.setString(3, usuario.getAreaAtuacao());
            stmt.setString(4, usuario.getFoto());
            stmt.setString(5, usuario.getEmail());
            stmt.setInt(6, id);

            usuario.setIdUsuario(id);
            // Executa-se a consulta
            int res = stmt.executeUpdate();

            return usuario;
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

            String sql1 = "DELETE FROM POSTAGEM WHERE id_usuario = ?";
            String sql2 = "DELETE FROM ENDERECO WHERE id_usuario = ?";
            String sql3 = "DELETE FROM CONTATO WHERE id_usuario = ?";
            String sql4 = "DELETE FROM USUARIO WHERE id_usuario = ?";

            PreparedStatement stmt1 = con.prepareStatement(sql1);
            PreparedStatement stmt2 = con.prepareStatement(sql2);
            PreparedStatement stmt3 = con.prepareStatement(sql3);
            PreparedStatement stmt4 = con.prepareStatement(sql4);


            stmt1.setInt(1, id);
            stmt2.setInt(1, id);
            stmt3.setInt(1, id);
            stmt4.setInt(1, id);

            // Executa-se a consulta
            int res1 = stmt1.executeUpdate();
            int res2 = stmt2.executeUpdate();
            int res3 = stmt3.executeUpdate();
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
