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
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    private final PartnerHeaderService partnerHeaderService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Partner>> createPartner(
            @RequestBody Partner partner
    ) {
        return partnerService.create(partner);
    }

    @PostMapping("/upload_photo")
    public ResponseEntity<ApiResponse<Partner>> uploadImage(
         @RequestParam Long id,
         @RequestPart(value = "photo") MultipartFile photo
    ){
        return partnerService.uploadImage(id,photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<Partner>> getBySlug(
            @PathVariable String slug
    ) {
        return partnerService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findAll(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    ) {
        return partnerService.findAll(lang);
    }

    @GetMapping("/get-all-partner")
    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findAllForMenuPage(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    ) {
        return partnerService.findSixPartnerForMainPage(lang);
    }

    @GetMapping("/get-others/{partnerSlug}")
    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findOtherPartner(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang,
            @PathVariable String partnerSlug
    ) {
        return partnerService.findOtherPartnerBySlug(partnerSlug,lang);
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return partnerService.deleteById(id);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return partnerService.changeActive(id);
    }

    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<PartnerHeader>> createHeader(
            @RequestBody PartnerHeader header
    ) {
        return partnerHeaderService.create(header);
    }


    @GetMapping("/header/get")
    public ResponseEntity<ApiResponse<PartnerHeaderDTO>> findHeader(

            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    ) {
        return partnerHeaderService.find(lang);
    }

    @PutMapping("/header/update")
    public ResponseEntity<ApiResponse<PartnerHeader>> updateHeader(
            @RequestBody PartnerHeader partnerHeader
    ) {
        return partnerHeaderService.update(partnerHeader);
    }

    @DeleteMapping("/header/delete")
    public ResponseEntity<ApiResponse<?>> deleteHeader() {
        return partnerHeaderService.delete();
    }


}