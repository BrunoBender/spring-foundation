package br.com.bruno.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequiredObjectISNullException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    public RequiredObjectISNullException() {
        super("It is not allowed to persist a null object");
    }

    public RequiredObjectISNullException(String message) {
        super(message);
    }
}
