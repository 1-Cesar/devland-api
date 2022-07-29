package br.com.vemser.devlandapi.dto;

import lombok.Data;

@Data
public class UserLoginCreateDTO {

    private String login;

    private String senha;

}
