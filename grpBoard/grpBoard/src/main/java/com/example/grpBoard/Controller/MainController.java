package com.example.grpBoard.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/main")
    public String getMain() {
        return "main";
    }

    @GetMapping("/employees/login")
    public String getLogin() {
        return "employees/login";
    }

}
