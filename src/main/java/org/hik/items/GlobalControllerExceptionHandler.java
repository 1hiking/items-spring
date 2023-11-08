package org.hik.items;


import jakarta.annotation.Nonnull;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoSuchElementException.class)
    public String ItemNotFoundException(@Nonnull NoSuchElementException e, @Nonnull Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(RuntimeException.class)
    public String BadRequest(@Nonnull RuntimeException e, @Nonnull Model model) {
        model.addAttribute("message", e.getMessage());
        return "error";
    }


}
