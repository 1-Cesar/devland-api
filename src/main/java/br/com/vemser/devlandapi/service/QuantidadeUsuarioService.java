package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.config.MongoDB;
import br.com.vemser.devlandapi.enums.TipoUsuario;

import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;


@Service
@Slf4j
public class QuantidadeUsuarioService {

    @Autowired
    private MongoDB mongoDB;

    public String retornarQtdUsuario(TipoUsuario tipoUsuario) {
        if (tipoUsuario.equals(TipoUsuario.DEV)) {
            FindIterable findIterable =
                    mongoDB.getLogUsuario().find(Filters.eq("tipoUsuario", "DEV"));
            Iterator iterator = findIterable.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            return ">>>>> Quantidade de DEVS cadastrados = " + count;
        } else if (tipoUsuario.equals(TipoUsuario.EMPRESA)) {
            FindIterable findIterable =
                    mongoDB.getLogUsuario().find(Filters.eq("tipoUsuario", "EMPRESA"));
            Iterator iterator = findIterable.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            return ">>>>> Quantidade de Empresas cadastradas = " + count;
        } else {
            FindIterable findIterable =
                    mongoDB.getLogUsuario().find(Filters.eq("tipoUsuario", "ADMIN"));
            Iterator iterator = findIterable.iterator();
            int count = 0;
            while (iterator.hasNext()) {
                iterator.next();
                count++;
            }
            return ">>>>> Quantidade de Administradores cadastrados = " + count;
        }
    }
}
