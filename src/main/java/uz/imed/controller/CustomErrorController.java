package uz.imed.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController
{

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request)
    {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");

        if (statusCode != null && statusCode == 404)
        {
            return "404";
        }
        // Add more status code handling if needed
        return "error"; // generic error page
    }

}
