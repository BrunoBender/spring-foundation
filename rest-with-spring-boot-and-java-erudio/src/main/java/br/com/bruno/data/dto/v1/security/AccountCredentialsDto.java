package br.com.bruno.data.dto.v1.security;

import lombok.Data;

@Data
public class AccountCredentialsDto {
    private String username;
    private String password;
}
