package br.com.vemser.devlandapi.documentations;

import br.com.vemser.devlandapi.annotations.UsuarioNotas;
import br.com.vemser.devlandapi.dto.PageDTO;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPersonalizadoDevDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface UsuarioDocs {
    @UsuarioNotas
    @Operation(summary = "Exibir todos os usuarios", description = "Permite ao ADMIN visualizar todos os usuarios.")
    public ResponseEntity<List<UsuarioDTO>> listarTodos() throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "Listar usuario por id", description = "Recupera um usuario do banco de dados atraves de seu id.")
    public ResponseEntity<List<UsuarioDTO>> listarUsuario(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "Listar usuario por nome", description = "Busca um usuario por seu nome ou trecho dele.")
    public ResponseEntity<List<UsuarioDTO>> listarPorNome(@RequestParam("nome") String nome) throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "Deletar um usuario", description = "Deleta um usuario do banco de dados atraves de seu id.")
    public void delete(@PathVariable("idUsuario") Integer id) throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "Filtrar os usuarios por Stack", description = "Filtra os usuarios por stack.")
    public PageDTO<RelatorioPersonalizadoDevDTO> getUsuarioByStack(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) String stack);

    @UsuarioNotas
    @Operation(summary = "Filtrar os usuarios por Genero", description = "Filtra os usuarios por genero: MASCULINO ou FEMININO.")
    public PageDTO<RelatorioPersonalizadoDevDTO> getUsuarioByGenero(Integer pagina, Integer quantidadeRegistros, @RequestParam(required = false) Genero genero);

    @UsuarioNotas
    @Operation(summary = "Exibir ao usuário seu proprio perfil", description = "Usuario exibe o seu proprio perfil.")
    public List<UsuarioDTO> listarProprio() throws RegraDeNegocioException;

    @UsuarioNotas
    @Operation(summary = "Permitir ao usuário editar seu proprio perfil", description = "Usuario edita o seu proprio perfil.")
    public UsuarioDTO editarProprio(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException;

}
