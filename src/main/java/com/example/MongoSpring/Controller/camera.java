package com.example.MongoSpring.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class camera {
    @GetMapping("/camera")
    public String camera(){
        return "camera";
    }
}
