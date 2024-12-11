package br.com.bruno.data.dto.v1.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenDto {
    private String username;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    //Armazena o valor do token
    private String accessToken;
    //Usado para renovar o token, obtendo um novo
    private String refreshToken;
}
