package br.com.bruno.controllers;

import br.com.bruno.exceptions.ResourceNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MathController {

    private AtomicLong counter = new AtomicLong();

    @RequestMapping(value = "/exception-test/{numberOne}", method = RequestMethod.GET)
    public Double exceptionTest(@PathVariable(value = "numberOne") Integer numberOne) throws Exception {
        if(numberOne == 1) {
            throw new Exception("Erro genérico");
        } else if(numberOne == 2) {
            throw new ResourceNotFoundException("Please set a numeric value!");
        }

        return 1d;
    }
}
