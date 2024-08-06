package uz.imed.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/v1")
public class HomeController
{
    @GetMapping
    public ResponseEntity<String> home()
    {
        return ResponseEntity.ok().body("Everything's gonna be alright/...");
    }


    @GetMapping("/home")
    public ResponseEntity<String> homeApi()
    {
        return ResponseEntity.ok().body("url: /v1/home");
    }

    @GetMapping("/abc")
    public ResponseEntity<String> homeAbc()
    {
        return ResponseEntity.ok().body("url: /v1/abc");
    }

}
