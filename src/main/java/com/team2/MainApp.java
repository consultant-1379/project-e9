package com.team2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainApp {

    @GetMapping("/")
    public String home(){
        return "index.html";
    }
}
