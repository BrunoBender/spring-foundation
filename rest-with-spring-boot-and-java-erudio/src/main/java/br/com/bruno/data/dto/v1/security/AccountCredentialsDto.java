package br.com.bruno.data.dto.v1.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountCredentialsDto {
    private String username;
    private String password;
}
