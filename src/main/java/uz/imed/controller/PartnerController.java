package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Partner;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerDTO;
import uz.imed.service.PartnerService;

import java.util.List;

@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Partner>> create(
            @RequestParam(value = "json") String partner,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return partnerService.create(partner, photo);
    }

    @GetMapping("/get-full-data/{id}")
    public ResponseEntity<ApiResponse<Partner>> findFullData(
            @PathVariable Long id
    ) {
        return partnerService.findFullDataById(id);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<PartnerDTO>> findBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return partnerService.findBySlug(slug, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findAll(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return partnerService.findAll(lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Partner>> update(
            @RequestBody Partner partner
    ){
        return partnerService.update(partner);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(
            @PathVariable Long id
    ) {
        return partnerService.deleteById(id);
    }

}
