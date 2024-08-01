package uz.imed.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.New;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.NewDTO;
import uz.imed.service.NewService;

import java.io.*;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> create(
            @RequestParam(value = "json") String client,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return newService.create(client, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<NewDTO>> findById(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return newService.findBySlug(slug, lang);
    }

    @GetMapping("/get-full-data/{id}")
    public ResponseEntity<ApiResponse<New>> findFullData(
            @PathVariable Long id
    ) {
        return newService.findFullDataById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return newService.findAll(lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<New>> update(
            @RequestBody New newn
    ) {
        return newService.update(newn);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return newService.deleteById(id);
    }

}
