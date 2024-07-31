package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Client;
import uz.imed.payload.ApiResponse;
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
            @RequestParam(value = "json") String client,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return clientService.create(client, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<ClientDTO>> findById(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return clientService.findBySlug(slug, lang);
    }

    @GetMapping("/get-full-data/{id}")
    public ResponseEntity<ApiResponse<Client>> findFullData(
            @PathVariable Long id
    ) {
        return clientService.findFullDataById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ClientDTO>>> findAll(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return clientService.findAll(lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Client>> update(
            @RequestBody Client client
    ) {
        return clientService.update(client);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return clientService.deleteById(id);
    }

}
