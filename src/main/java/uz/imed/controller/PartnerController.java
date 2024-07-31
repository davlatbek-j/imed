package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Partner;
import uz.imed.entity.PartnerHeader;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PartnerDTO;
import uz.imed.payload.PartnerHeaderDTO;
import uz.imed.service.PartnerHeaderService;
import uz.imed.service.PartnerService;

import java.util.List;

@RestController
@RequestMapping("/api/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    private final PartnerHeaderService partnerHeaderService;

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
    ) {
        return partnerService.update(partner);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteById(
            @PathVariable Long id
    ) {
        return partnerService.deleteById(id);
    }

    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<PartnerHeader>> create(
            @RequestBody PartnerHeader header
    ) {
        return partnerHeaderService.create(header);
    }

    @GetMapping("/header/get")
    public ResponseEntity<ApiResponse<PartnerHeaderDTO>> find(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return partnerHeaderService.find(lang);
    }

    @GetMapping("/header/get-full-data")
    public ResponseEntity<ApiResponse<PartnerHeader>> findFullData() {
        return partnerHeaderService.findFullData();
    }

    @PutMapping("/header/update")
    public ResponseEntity<ApiResponse<PartnerHeader>> update(
            @RequestBody PartnerHeader header
    ) {
        return partnerHeaderService.update(header);
    }

    @DeleteMapping("/header/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return partnerHeaderService.delete();
    }

}
