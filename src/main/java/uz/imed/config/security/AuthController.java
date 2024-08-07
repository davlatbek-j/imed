package uz.imed.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.Token;

@RequiredArgsConstructor

@Controller
@RequestMapping("/v1/auth")
public class AuthController
{
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Token>> login(
            @RequestParam("username") String username,
            @RequestParam("password") String password)
    {
        return userService.login(username, password);
    }
}
