package br.com.vemser.devlandapi.dto;

import lombok.Data;

@Data
public class UserLoginAuthDTO {

    private String login;
    private String senha;
}
