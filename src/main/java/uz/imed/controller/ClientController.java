package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Client;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.CategoryDTO;
import uz.imed.payload.ClientDTO;
import uz.imed.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Client>> create(
            @RequestParam("json") String json,
            @RequestParam("icon") MultipartFile icon,
            @RequestParam("gallery") List<MultipartFile> gallery
    ) {
        return clientService.create(json, icon, gallery);
    }


    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<?>> findById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return clientService.getById(id, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return clientService.findAll(lang);
    }

    @GetMapping("/get_by_slug/{slug}")
    public ResponseEntity<ApiResponse<?>> getBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language",required = false) String lang) {
        return clientService.get(slug, lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Client>> update(
            @RequestParam("json") String json) {
        return clientService.update(json);
    }




    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Client>> delete(@PathVariable Long id) {
        return clientService.delete(id);
    }

}
