package ru.jordan.food_storage.handler;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Intercept default 404 Spring handler ("throw-exception-if-no-handler-found" property doesn't seem to work)
 */
@Controller
public class CustomErrorController {
    @RequestMapping("/error")
    public void handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
        String message = "Resource not found. Check documentation: " + baseUrl + "/swagger-ui/index.html";
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                throw new ResourceNotFoundException(message);
            }
        }
    }
}
