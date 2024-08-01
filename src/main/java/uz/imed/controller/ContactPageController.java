package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.ContactBody;
import uz.imed.entity.Representative;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ContactBodyDTO;
import uz.imed.payload.RepresentativeDTO;
import uz.imed.service.ContactBodyService;
import uz.imed.service.RepresentativeService;

import java.util.List;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactPageController {
    private final ContactBodyService contactBodyService;

    private final RepresentativeService representativeService;

    @PostMapping("/body/create")
    public ResponseEntity<ApiResponse<ContactBody>> createBody(
            @RequestParam String contactBody
    ) {
        return contactBodyService.create(contactBody);
    }

    @GetMapping("/body/get")
    public ResponseEntity<ApiResponse<List<ContactBodyDTO>>> find(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return contactBodyService.findAll(lang);
    }

    @PutMapping("/body/update")
    public ResponseEntity<ApiResponse<ContactBody>> update(
            @RequestBody ContactBody contactBody
    ) {
        return contactBodyService.update(contactBody);
    }

    @DeleteMapping("/body/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return contactBodyService.delete();
    }

    @PostMapping("/representative/create")
    public ResponseEntity<ApiResponse<Representative>> createRepresentative(
            @RequestParam(value = "json") String json,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return representativeService.create(json, photo);
    }

    @GetMapping("/representative/get/{id}")
    public ResponseEntity<ApiResponse<RepresentativeDTO>> getRepresentativeById(
            @RequestHeader(value = "Accept-Language") String lang,
            @PathVariable Long id
    ) {
        return representativeService.findById(lang,id);
    }

    @GetMapping("/representative/get-all")
    public ResponseEntity<ApiResponse<List<RepresentativeDTO>>> getAllRepresentative(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return representativeService.findAll(lang);
    }

    @PutMapping("/representative/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActiveRepresentative(
            @PathVariable Long id
    ) {
        return representativeService.changeActive(id);
    }

    @DeleteMapping("/representative/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRepresentative(
            @PathVariable Long id
    ) {
        return representativeService.deleteById(id);
    }
}
