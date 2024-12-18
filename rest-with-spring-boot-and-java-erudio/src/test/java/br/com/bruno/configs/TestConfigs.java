package br.com.bruno.configs;

public class TestConfigs {

    public static final int SERVER_PORT = 8889;
    public static final String BASE_URI = "http://localhost";
    public static final String HEADER_PARAM_AUTHORIZATION = "Authorization";
    public static final String HEADER_PARAM_ORIGIN = "Origin";

    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_XML = "application/xml";
    public static final String CONTENT_TYPE_YML = "application/x-yaml";

    public static final String ORIGIN_VALID = "https://erudio.com.br";
//    TODO: adicionar origem inválida ao resolver o problema de CORS
    public static final String ORIGIN_INVALID = "";

}
