package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Certificate;
import uz.imed.payload.ApiResponse;
import uz.imed.service.CertificateService;

@RestController
@RequestMapping("/api/certificate")
@RequiredArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Certificate>> create(
            @RequestParam(value = "json") String certificate,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return certificateService.create(certificate, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<?>> findBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return certificateService.findBySlug(slug, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return certificateService.findAll(lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Certificate>> update(
            @RequestBody Certificate certificate
    ) {
        return certificateService.update(certificate);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return certificateService.delete(id);
    }

}
