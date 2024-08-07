package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsHeader;
import uz.imed.entity.Certificate;
import uz.imed.payload.ApiResponse;
import uz.imed.service.CertificateService;

@RestController
@RequestMapping("/api/certificate")
@RequiredArgsConstructor
public class CertificateController {
    private  final CertificateService certificateService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Certificate>> create(
            @RequestParam("json") String json,
            @RequestParam("photo") MultipartFile file
    ) {return certificateService.create(json,file);
    }


    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> findAllAboutUsHeader(
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return certificateService.findAll(lang);
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ApiResponse<?>> findAboutUsHeaderById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return certificateService.getById(id,lang);
    }

    @GetMapping("/by-slug/{slug}")
    public ResponseEntity<ApiResponse<?>> getBySlug(
            @PathVariable(name = "slug") String slug,
            @RequestHeader(value = "Accept-Language", required = false) String lang

    ){
        return certificateService.get(slug,lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Certificate>> updateAboutUsHeader(
            @RequestParam("json") String json
    ) {
        return certificateService.update(json);
    }

    @DeleteMapping("/delete/{id}")

    public ResponseEntity<ApiResponse<Certificate>> delete(
            @PathVariable Long id
    )
    {
        return certificateService.delete(id);
    }
}
