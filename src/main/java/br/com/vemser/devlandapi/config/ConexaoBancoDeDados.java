package br.com.vemser.devlandapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component //@Service ...
public class ConexaoBancoDeDados {

    //@Value("${jdbc-string}")
    private String jdbcString;

    //@Value("${jdbc-user}")
    private String user;

    //@Value("${jdbc-pass}")
    private String pass;

    //@Value("${jdbc-schema}")
    private String schema;

    public Connection getConnection() throws SQLException {
        Connection con = DriverManager.getConnection(jdbcString, user, pass);

        con.createStatement().execute("alter session set current_schema=" + schema);

        return con;
    }
}
