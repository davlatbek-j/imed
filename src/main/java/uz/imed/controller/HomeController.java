package uz.imed.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("imed.uz/api")
public class HomeController
{
    @GetMapping
    public ResponseEntity<String> home()
    {
        return ResponseEntity.ok().body("Everything's gonna be alright/...");
    }
}
