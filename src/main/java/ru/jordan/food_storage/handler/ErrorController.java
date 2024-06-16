package ru.jordan.food_storage.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Controller
public class ErrorController {

    @GetMapping("/error")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleError() {
        return "redirect:/swagger-ui/index.html";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public String handleNoResourceFoundException() {
        return "redirect:/swagger-ui/index.html";
    }
}
