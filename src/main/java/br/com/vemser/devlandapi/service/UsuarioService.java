package br.com.vemser.devlandapi.service;

import br.com.vemser.devlandapi.dto.*;
import br.com.vemser.devlandapi.dto.relatorios.RelatorioPersonalizadoDevDTO;
import br.com.vemser.devlandapi.dto.userlogin.UserLoginCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioCreateDTO;
import br.com.vemser.devlandapi.dto.usuario.UsuarioDTO;
import br.com.vemser.devlandapi.entity.CargoEntity;
import br.com.vemser.devlandapi.entity.UserLoginEntity;
import br.com.vemser.devlandapi.entity.UsuarioEntity;
import br.com.vemser.devlandapi.enums.Genero;
import br.com.vemser.devlandapi.enums.TipoMensagem;
import br.com.vemser.devlandapi.enums.TipoUsuario;
import br.com.vemser.devlandapi.exceptions.RegraDeNegocioException;
import br.com.vemser.devlandapi.repository.UserLoginRepository;
import br.com.vemser.devlandapi.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private EmailService emailService;

    public List<UsuarioDTO> listar() throws RegraDeNegocioException {
        if (usuarioRepository.findAll().size() == 0) {
            throw new RegraDeNegocioException("Nenhum usuario encontrado");
        } else {
            return usuarioRepository.findAll().stream()
                    .map(usuario -> objectMapper.convertValue(usuario, UsuarioDTO.class))
                    .collect(Collectors.toList());
        }
    }

    public List<UsuarioDTO> listarUsuario(Integer id) throws RegraDeNegocioException {
        localizarUsuario(id);
        return usuarioRepository.findById(id).stream()
                .filter(usuario -> usuario.getIdUsuario().equals(id))
                .map(usuario -> objectMapper.convertValue(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> listarPorNome(String nome) {
        return usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getNome().toUpperCase().contains(nome.toUpperCase()))
                .map(this::retornarDTO)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) throws RegraDeNegocioException {
        UsuarioEntity usuarioRecuperado = localizarUsuario(id);
        usuarioRepository.delete(usuarioRecuperado);

        String tipoMensagem = TipoMensagem.DELETE.getTipo();
        emailService.sendEmailUsuario(usuarioRecuperado, tipoMensagem);
    }

    public String adicionar(UserLoginCreateDTO userLoginCreateDTO) throws RegraDeNegocioException {

        if (userLoginCreateDTO.getUsuarioCreateDTO().getTipoUsuario() == TipoUsuario.ADMIN) {
            throw new RegraDeNegocioException("tipo de usuário inválido");
        }

        if (userLoginCreateDTO.getUsuarioCreateDTO().getTipoUsuario() == TipoUsuario.DEV) {
            if (userLoginCreateDTO.getUsuarioCreateDTO().getCpfCnpj().length() == 11 && ValidaCPF.isCPF(userLoginCreateDTO.getUsuarioCreateDTO().getCpfCnpj())) {

                userLoginCreateDTO.setSenha(userLoginService.criptofrafia(userLoginCreateDTO.getSenha()));

                UsuarioEntity usuarioEntity = objectMapper.convertValue(userLoginCreateDTO.getUsuarioCreateDTO(), UsuarioEntity.class);

                UsuarioEntity usuario = usuarioRepository.save(usuarioEntity);

                CargoEntity cargoEntity = new CargoEntity();

                List<CargoEntity> cargoEntities = new ArrayList<>();

                UserLoginEntity userLoginEntity = objectMapper.convertValue(userLoginCreateDTO, UserLoginEntity.class);



                userLoginEntity.setUsuarioEntity(usuario);
                userLoginEntity.setStatus(true);

                if (usuario.getTipoUsuario().toString().equals("DEV")) {

                    cargoEntity.setIdCargo(2);

                } else {

                    cargoEntity.setIdCargo(1);

                }

                cargoEntities.add(cargoEntity);

                userLoginEntity.setCargos(cargoEntities);

                userLoginRepository.save(userLoginEntity);

                String tipoMensagem = TipoMensagem.CREATE.getTipo();
                emailService.sendEmailUsuario(usuario, tipoMensagem);

                return "Desenvolvedor cadastrado com sucesso!";
            } else {
                throw new RegraDeNegocioException("CPF Inválido");
            }
        }

        if (userLoginCreateDTO.getUsuarioCreateDTO().getCpfCnpj().length() == 14 && ValidaCNPJ.isCNPJ(userLoginCreateDTO.getUsuarioCreateDTO().getCpfCnpj())) {

            userLoginCreateDTO.setSenha(userLoginService.criptofrafia(userLoginCreateDTO.getSenha()));

            UsuarioEntity usuarioEmpresa = usuarioRepository.save(retornarUsuarioEntity(userLoginCreateDTO.getUsuarioCreateDTO()));

            CargoEntity cargoEntity = new CargoEntity();

            List<CargoEntity> cargoEntities = new ArrayList<>();

            UserLoginEntity userLoginEntity = objectMapper.convertValue(userLoginCreateDTO, UserLoginEntity.class);
            userLoginEntity.setUsuarioEntity(retornarUsuarioEntity(userLoginCreateDTO.getUsuarioCreateDTO()));
            userLoginEntity.setStatus(true);

            if (usuarioEmpresa.getTipoUsuario().toString().equals("EMPRESA")) {

                cargoEntity.setIdCargo(3);

            } else {

                cargoEntity.setIdCargo(1);

            }

            cargoEntities.add(cargoEntity);

            userLoginEntity.setCargos(cargoEntities);

            userLoginRepository.save(userLoginEntity);

            String tipoMensagem = TipoMensagem.CREATE.getTipo();
            emailService.sendEmailUsuario(usuarioEmpresa, tipoMensagem);

            return "Empresa cadastrada com sucesso!";
        } else {
            throw new RegraDeNegocioException("CNPJ Inválido");
        }
    }

    public UsuarioEntity localizarUsuario(Integer idUsuario) throws RegraDeNegocioException {
        UsuarioEntity usuarioRecuperado = usuarioRepository.findAll().stream()
                .filter(usuario -> usuario.getIdUsuario().equals(idUsuario))
                .findFirst()
                .orElseThrow(() -> new RegraDeNegocioException("Usuário não encontrado"));
        return usuarioRecuperado;
    }

    public PageDTO<UsuarioDTO> paginacaoTipo(TipoUsuario tipoUsuario, Integer pagina, Integer quantidadeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros);
        Page<UsuarioEntity> page = usuarioRepository.getUsuarioByTipo(tipoUsuario, pageable);
        List<UsuarioDTO> usuarioDTOS = page.getContent().stream()
                .map(this::retornarDTO)
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeRegistros, usuarioDTOS);
    }

    public PageDTO<RelatorioPersonalizadoDevDTO> relatorioStack(String stack, Integer pagina, Integer quantidadeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros);
        Page<RelatorioPersonalizadoDevDTO> page = usuarioRepository.relatorioPersonalizadoDevDTO(stack, pageable);
        List<RelatorioPersonalizadoDevDTO> relatorioPersonalizadoDevDTOS = page.getContent().stream()
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeRegistros, relatorioPersonalizadoDevDTOS);
    }

    public PageDTO<RelatorioPersonalizadoDevDTO> relatorioGenero(Genero genero, Integer pagina, Integer quantidadeRegistros) {
        Pageable pageable = PageRequest.of(pagina, quantidadeRegistros);
        Page<RelatorioPersonalizadoDevDTO> page = usuarioRepository.relatorioPersonalizadoDevGeneroDTO(genero, pageable);
        List<RelatorioPersonalizadoDevDTO> relatorioPersonalizadoDevDTOS = page.getContent().stream()
                .toList();
        return new PageDTO<>(page.getTotalElements(), page.getTotalPages(), pagina, quantidadeRegistros, relatorioPersonalizadoDevDTOS);
    }

    //==================================================================================================================
    //                                        EXCLUSIVOS DEV & EMPRESA
    //==================================================================================================================

    public List<UsuarioDTO> listarProprio() throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = (Integer) usuarioLogadoEntity.getIdUsuario();

        localizarUsuario(id);
        return usuarioRepository.findById(id).stream()
                .filter(usuario -> usuario.getIdUsuario().equals(id))
                .map(usuario -> objectMapper.convertValue(usuario, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    public String deleteProprio() throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        UsuarioEntity usuarioRecuperado = localizarUsuario(usuarioLogadoEntity.getIdUsuario());

        usuarioRepository.delete(usuarioRecuperado);

        String tipoMensagem = TipoMensagem.DELETE.getTipo();
        emailService.sendEmailUsuario(usuarioRecuperado, tipoMensagem);

        return "Usuario " + usuarioLogadoEntity.getIdUsuario() + " foi deletado.";
    }

    public UsuarioDTO editarProprio(UsuarioCreateDTO usuarioCreateDTO) throws RegraDeNegocioException {
        Integer idLoggedUser = userLoginService.getIdLoggedUser();
        UserLoginEntity usuarioLogadoEntity = userLoginService.findById(idLoggedUser);

        Integer id = usuarioLogadoEntity.getIdUsuario();


        if (usuarioCreateDTO.getTipoUsuario() == TipoUsuario.DEV) {
            if (usuarioCreateDTO.getCpfCnpj().length() == 11 && ValidaCPF.isCPF(usuarioCreateDTO.getCpfCnpj())) {

                UsuarioEntity usuarioEntity = localizarUsuario(id);
                usuarioEntity = validaAlteracoes(usuarioEntity, usuarioCreateDTO);
                usuarioRepository.save(usuarioEntity);

                String tipoMensagem = TipoMensagem.UPDATE.getTipo();
                emailService.sendEmailUsuario(usuarioEntity, tipoMensagem);

                return retornarDTO(usuarioEntity);
            } else {
                throw new RegraDeNegocioException("CPF Inválido");
            }
        }

        if (usuarioCreateDTO.getCpfCnpj().length() == 14 && ValidaCNPJ.isCNPJ(usuarioCreateDTO.getCpfCnpj())) {

            UsuarioEntity usuarioEntity = localizarUsuario(id);
            usuarioEntity = validaAlteracoes(usuarioEntity, usuarioCreateDTO);
            usuarioRepository.save(usuarioEntity);

            String tipoMensagem = TipoMensagem.UPDATE.getTipo();
            emailService.sendEmailUsuario(usuarioEntity, tipoMensagem);

            return retornarDTO(usuarioEntity);
        } else {
            throw new RegraDeNegocioException("CNPJ Inválido");
        }
    }

//    ================================== CONVERSOES ========================================

    public UsuarioDTO retornarDTO(UsuarioEntity usuario) {
        return objectMapper.convertValue(usuario, UsuarioDTO.class);
    }

    public UsuarioEntity retornarUsuarioEntity(UsuarioCreateDTO usuarioCreateDTO) {
        return objectMapper.convertValue(usuarioCreateDTO, UsuarioEntity.class);
    }

    //    ================================== VALIDAÇÕES ======================================
    public class ValidaCPF {
        public static boolean isCPF(String CPF) {
            // considera-se erro CPF's formados por uma sequencia de numeros iguais
            if (CPF.equals("00000000000") ||
                    CPF.equals("11111111111") ||
                    CPF.equals("22222222222") || CPF.equals("33333333333") ||
                    CPF.equals("44444444444") || CPF.equals("55555555555") ||
                    CPF.equals("66666666666") || CPF.equals("77777777777") ||
                    CPF.equals("88888888888") || CPF.equals("99999999999") ||
                    (CPF.length() != 11))
                return (false);

            char dig10, dig11;
            int sm, i, r, num, peso;

            // "try" - protege o codigo para eventuais erros de conversao de tipo (int)
            try {
                // Calculo do 1o. Digito Verificador
                sm = 0;
                peso = 10;
                for (i = 0; i < 9; i++) {
                    // converte o i-esimo caractere do CPF em um numero:
                    // por exemplo, transforma o caractere '0' no inteiro 0
                    // (48 eh a posicao de '0' na tabela ASCII)
                    num = (int) (CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig10 = '0';
                else dig10 = (char) (r + 48); // converte no respectivo caractere numerico

                // Calculo do 2o. Digito Verificador
                sm = 0;
                peso = 11;
                for (i = 0; i < 10; i++) {
                    num = (int) (CPF.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso - 1;
                }

                r = 11 - (sm % 11);
                if ((r == 10) || (r == 11))
                    dig11 = '0';
                else dig11 = (char) (r + 48);

                // Verifica se os digitos calculados conferem com os digitos informados.
                if ((dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10)))
                    return (true);
                else return (false);
            } catch (InputMismatchException erro) {
                return (false);
            }
        }

        public static String imprimeCPF(String CPF) {
            return (CPF.substring(0, 3) + "." + CPF.substring(3, 6) + "." +
                    CPF.substring(6, 9) + "-" + CPF.substring(9, 11));
        }
    }

    public class ValidaCNPJ {

        public static boolean isCNPJ(String CNPJ) {
            // considera-se erro CNPJ's formados por uma sequencia de numeros iguais
            if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
                    CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
                    CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
                    CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
                    CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
                    (CNPJ.length() != 14))
                return (false);

            char dig13, dig14;
            int sm, i, r, num, peso;

            // "try" - protege o código para eventuais erros de conversao de tipo (int)
            try {
                // Calculo do 1o. Digito Verificador
                sm = 0;
                peso = 2;
                for (i = 11; i >= 0; i--) {
                    // converte o i-ésimo caractere do CNPJ em um número:
                    // por exemplo, transforma o caractere '0' no inteiro 0
                    // (48 eh a posição de '0' na tabela ASCII)
                    num = (int) (CNPJ.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso + 1;
                    if (peso == 10)
                        peso = 2;
                }

                r = sm % 11;
                if ((r == 0) || (r == 1))
                    dig13 = '0';
                else dig13 = (char) ((11 - r) + 48);

                // Calculo do 2o. Digito Verificador
                sm = 0;
                peso = 2;
                for (i = 12; i >= 0; i--) {
                    num = (int) (CNPJ.charAt(i) - 48);
                    sm = sm + (num * peso);
                    peso = peso + 1;
                    if (peso == 10)
                        peso = 2;
                }

                r = sm % 11;
                if ((r == 0) || (r == 1))
                    dig14 = '0';
                else dig14 = (char) ((11 - r) + 48);

                // Verifica se os dígitos calculados conferem com os dígitos informados.
                if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
                    return (true);
                else return (false);
            } catch (InputMismatchException erro) {
                return (false);
            }
        }

        public static String imprimeCNPJ(String CNPJ) {
            // máscara do CNPJ: 99.999.999.9999-99
            return (CNPJ.substring(0, 2) + "." + CNPJ.substring(2, 5) + "." +
                    CNPJ.substring(5, 8) + "." + CNPJ.substring(8, 12) + "-" +
                    CNPJ.substring(12, 14));
        }
    }

    public UsuarioEntity validaAlteracoes(UsuarioEntity usuarioEntity, UsuarioCreateDTO usuarioCreateDTO) {

        usuarioEntity.setNome(usuarioCreateDTO.getNome());
        usuarioEntity.setCpfCnpj(usuarioCreateDTO.getCpfCnpj());
        usuarioEntity.setEmail(usuarioCreateDTO.getEmail());
        usuarioEntity.setFoto(usuarioCreateDTO.getFoto());
        usuarioEntity.setAreaAtuacao(usuarioCreateDTO.getAreaAtuacao());

        return usuarioEntity;
    }



}
