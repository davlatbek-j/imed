package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.imed.entity.Navbar;
import uz.imed.payload.ApiResponse;

@RequiredArgsConstructor

@Controller
@RequestMapping("/api/controller")
public class NavbarController
{
//    private final NavbarService navbarService;

    @PostMapping
    public ResponseEntity<ApiResponse<Navbar>> create(@RequestBody Navbar navbar)
    {
        return null;
    }

}
