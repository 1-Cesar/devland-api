package br.com.vemser.devlandapi.repository;

import br.com.vemser.devlandapi.entity.ComentarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepository extends JpaRepository<ComentarioEntity, Integer> {


/*    public Integer getProximoId(Connection connection) throws SQLException {
        Connection con = dbconnection.getConnection();

        String sql = " SELECT seq_comentario.nextval mysequence from DUAL ";

        Statement stmt = connection.createStatement();
        ResultSet res = stmt.executeQuery(sql);

        if (res.next()) {
            return res.getInt("mysequence");
        }
        return null;
    }

    public List<ComentarioEntity> listByIdPostagem(Integer idPostagem) throws RegraDeNegocioException {
        List<ComentarioEntity> comentarios = new ArrayList<>();
        Connection con = null;
        try {
            con = dbconnection.getConnection();

            String sql = " SELECT * " +
                    "      FROM COMENTARIO C " +
                    "      JOIN USUARIO U ON (C.ID_USUARIO = U.ID_USUARIO)" +
                    "      WHERE id_postagem = ? ";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idPostagem);

            ResultSet res = stmt.executeQuery();

            while (res.next()) {
                ComentarioEntity comentario = getComentarioFromResultSet(res);
                Usuario usuario = usuarioRepository.getUsuario(res);
                comentario.setUsuario(usuario);
                comentarios.add(comentario);
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
        return comentarios;
    }

    public ComentarioEntity post(ComentarioEntity comentario) throws RegraDeNegocioException {
        Connection con = null;

        try {
            con = dbconnection.getConnection();

            Integer proximoId = getProximoId(con);

            comentario.setIdComentario(proximoId);

            String sql = " INSERT INTO COMENTARIO\n " +
                    " (ID_COMENTARIO, ID_USUARIO, ID_POSTAGEM, DESCRICAO, LIKES, DATA_COMENTARIO)\n " +
                    " VALUES(?, ?, ?, ?, ?, ?)\n ";

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, comentario.getIdComentario());
            stmt.setInt(2, comentario.getIdUsuario());
            stmt.setInt(3, comentario.getIdPostagem());
            stmt.setString(4, comentario.getDescricao());
            stmt.setInt(5, comentario.getCurtidas());
            stmt.setString(6, comentario.getData());

            stmt.executeUpdate();

            return comentario;
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

    public ComentarioEntity findById(Integer idComentario) throws RegraDeNegocioException {
        Connection con = null;
        try {
            con = dbconnection.getConnection();

            String sql = " SELECT * FROM COMENTARIO WHERE id_comentario = ? ";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, idComentario);

            ResultSet result = stmt.executeQuery();

            if (result.next()) {
                return getComentarioFromResultSet(result);
            }
        } catch (SQLException e) {
            throw new RegraDeNegocioException(e.getMessage());
        }
        return null;
    }

    private ComentarioEntity getComentarioFromResultSet(ResultSet res) throws SQLException {
        ComentarioEntity comentario = new ComentarioEntity();

        comentario.setIdComentario(res.getInt("id_comentario"));
        comentario.setIdUsuario(res.getInt("id_usuario"));
        comentario.setIdPostagem(res.getInt("id_postagem"));
        comentario.setDescricao(res.getString("descricao"));
        comentario.setCurtidas(res.getInt("likes"));
        comentario.setData(res.getString("data_comentario"));

        return comentario;
    }*/
}
