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
            @RequestBody Client client) {
        return clientService.create(client);
    }

    @PostMapping("/image")
    public ResponseEntity<ApiResponse<Client>> uploadImage(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "photo") MultipartFile icon,
            @RequestParam(name = "gallery") List<MultipartFile> files
    ) {
        return clientService.uploadImage(id, icon,files);
    }



    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<ClientDTO>> findById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang)
    {
        return clientService.getById(id, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<ClientDTO>>> findAll(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang)
    {
        return clientService.findAll(lang);
    }

   /* @GetMapping("/get-full-data/{id}")
    public ResponseEntity<ApiResponse<Client>> findWithLanguage(@PathVariable Long id)
    {
        return clientService.getById(id);
    }*/

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Client>> update(
            @PathVariable Long id,
            @RequestBody Client client)
    {
        return clientService.update(id, client);
    }

    @PutMapping("/update/image/{id}")
    public ResponseEntity<ApiResponse<Client>> update(
            @PathVariable Long id,
            @RequestPart(name = "icon", required = false) MultipartFile icon,
            @RequestPart(name = "gallery", required = false) List<MultipartFile> gallery)
    {
        return clientService.updatePhoto(id, icon, gallery);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Client>> delete(@PathVariable Long id)
    {
        return clientService.delete(id);
    }

}
