package br.com.bruno.data.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
