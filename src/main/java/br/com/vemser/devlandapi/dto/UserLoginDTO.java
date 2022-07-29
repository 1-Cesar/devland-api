package br.com.vemser.devlandapi.dto;

import lombok.Data;

@Data
public class UserLoginDTO extends UserLoginCreateDTO{

    private String idAutenticacao;

}
