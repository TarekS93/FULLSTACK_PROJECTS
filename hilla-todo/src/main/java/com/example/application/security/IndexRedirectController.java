package com.example.application.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexRedirectController {

    @GetMapping(value = "/")
    public String index() {
        return "redirect:/main";
    }

}
