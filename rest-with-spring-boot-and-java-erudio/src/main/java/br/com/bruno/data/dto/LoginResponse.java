package br.com.bruno.data.dto;

import java.util.Date;

public record LoginResponse(String accessToken, Date expiresIn) {
}
