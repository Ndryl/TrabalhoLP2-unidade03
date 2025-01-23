package com.example.MongoSpring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class begin {
    @GetMapping("/login")
    public String login() {

        return "login";
    }
}
