package com.example.MongoSpring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class register {
    @GetMapping("/register")
    public String registro() {

        return "register";
    }
}
