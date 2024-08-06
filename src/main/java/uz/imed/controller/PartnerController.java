package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Partner;
import uz.imed.entity.PartnerHeader;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerHeaderDTO;
import uz.imed.service.PartnerHeaderService;
import uz.imed.service.PartnerService;

@RestController
@RequestMapping("/v1/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    private final PartnerHeaderService partnerHeaderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Partner>> create(
            @RequestParam(value = "json") String partner,
            @RequestPart(value = "photo") MultipartFile photo) {
        return partnerService.create(partner, photo);
    }

    //if Accept-Language==null return in all language(s)
    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<?>> get(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @PathVariable String slug) {
        return partnerService.get(slug, lang);
    }

    //OnlyLogoName using for choosing brand of product. Only logo and name
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<?>> findBySlug(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @RequestParam(value = "only-logo-name", required = false, defaultValue = "false") boolean onlyLogoName) {
        return partnerService.all(lang, onlyLogoName);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Partner>> update(
            @RequestBody Partner partner) {
        return partnerService.update(partner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(
            @PathVariable Long id) {
        return partnerService.delete(id);
    }

    @PostMapping("/header")
    public ResponseEntity<ApiResponse<PartnerHeader>> create(
            @RequestBody PartnerHeader header) {
        return partnerHeaderService.create(header);
    }

    @GetMapping("/header")
    public ResponseEntity<ApiResponse<?>> get(
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return partnerHeaderService.get(lang);
    }

    @PutMapping("/header")
    public ResponseEntity<ApiResponse<PartnerHeader>> update(
            @RequestBody PartnerHeader header) {
        return partnerHeaderService.update(header);
    }

    @DeleteMapping("/header")
    public ResponseEntity<ApiResponse<?>> delete() {
        return partnerHeaderService.delete();
    }

}
