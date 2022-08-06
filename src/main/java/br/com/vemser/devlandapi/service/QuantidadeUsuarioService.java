package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.repository.LogUsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuantidadeUsuarioService {

    @Autowired
    private LogUsuarioRepository logUsuarioRepository;

    public String retornarQtdUsuario(TipoUsuario tipoUsuario) {

        if (tipoUsuario.equals(TipoUsuario.DEV)) {
            return "Quantidade de Desenvolvedores Cadastrados = "  + logUsuarioRepository.contaTodosDevs();

        } else if (tipoUsuario.equals(TipoUsuario.EMPRESA)) {
            return "Quantidade de Empresas Cadastradas = "  + logUsuarioRepository.contaTodasEmpresas();

        } else {
            return "Quantidade de Admins Cadastrados = "  + logUsuarioRepository.contaTodosAdmins();
        }
    }

    public String retornarTodosUsuarios() {
        return "Quantidade de Usuários Cadastrados = "  + logUsuarioRepository.contaTodosUsuarios();
    }
}
