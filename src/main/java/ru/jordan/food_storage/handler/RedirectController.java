package ru.jordan.food_storage.handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectController {

    @RequestMapping(value = "/{path}", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:/swagger-ui/index.html";
    }
}
