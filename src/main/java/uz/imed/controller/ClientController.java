package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Client;
import uz.imed.payload.ApiResponse;
import uz.imed.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/v1/client")
@RequiredArgsConstructor
public class ClientController
{
    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ApiResponse<Client>> create(
            @RequestParam("json") String json,
            @RequestParam("logo") MultipartFile icon,
            @RequestParam("gallery") List<MultipartFile> gallery)
    {
        return clientService.create(json, icon, gallery);
    }


//    @GetMapping("/get/{id}")
//    public ResponseEntity<ApiResponse<?>> findById(
//            @PathVariable Long id,
//            @RequestHeader(value = "Accept-Language", required = false) String lang) {
//        return clientService.getById(id, lang);
//    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language") String lang,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "12") int size)
    {
        return clientService.findAll(lang, page, size);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<?>> getBySlug(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @PathVariable String slug,
            @RequestParam(value = "gallery-size", required = false, defaultValue = "6") int gallerySize)
    {
        return clientService.get(slug, lang, gallerySize);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Client>> update(
            @RequestBody Client client)
    {
        return clientService.update(client);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Client>> delete(@PathVariable Long id)
    {
        return clientService.delete(id);
    }

}
