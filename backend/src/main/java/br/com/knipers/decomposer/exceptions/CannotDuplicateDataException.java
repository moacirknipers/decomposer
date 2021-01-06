package br.com.knipers.decomposer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CannotDuplicateDataException extends Exception {
    public CannotDuplicateDataException(String message) {
        super(message);
    }
}
