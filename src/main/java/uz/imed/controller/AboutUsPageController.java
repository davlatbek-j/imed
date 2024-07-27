package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsCertificates;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.AboutUsHeader;
import uz.imed.entity.AboutUsPartner;
import uz.imed.payload.AboutUsChooseUsDTO;
import uz.imed.payload.AboutUsHeaderDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.service.AboutUsCertificatesService;
import uz.imed.service.AboutUsChooseUsService;
import uz.imed.service.AboutUsHeaderService;
import uz.imed.service.AboutUsPartnerService;

import java.util.List;

@RestController
@RequestMapping("/about-us")
@RequiredArgsConstructor
public class AboutUsPageController {

    private final AboutUsHeaderService aboutUsHeaderService;

    private final AboutUsPartnerService aboutUsPartnerService;

    private final AboutUsChooseUsService aboutUsChooseUsService;

    private final AboutUsCertificatesService aboutUsCertificaresService;





    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<AboutUsHeader>> createAboutUsHeader(
            @RequestBody AboutUsHeader aboutUsHeader
    ) {return aboutUsHeaderService.create(aboutUsHeader);
    }

@PostMapping("/upload-image")
public  ResponseEntity<ApiResponse<AboutUsHeader>> uploadImage(
        @RequestParam(name = "id") Long id,
        @RequestParam(name = "photo") MultipartFile file
){
        return aboutUsHeaderService.uploadImage(id,file);
}

    @GetMapping("/header/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsHeaderDTO>>> findAllAboutUsHeader(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang) {
        return aboutUsHeaderService.findAll(lang);
    }

    @GetMapping("/header/get-by-id/{id}")
    public ResponseEntity<ApiResponse<AboutUsHeaderDTO>> findAboutUsHeaderById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang) {
        return aboutUsHeaderService.getById(id,lang);
    }

    @PutMapping("/header/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsHeader>> updateAboutUsHeader(
            @PathVariable Long id,
            @RequestBody AboutUsHeader aboutUsHeader
    ) {
        return aboutUsHeaderService.update(id,aboutUsHeader);
    }

    @PutMapping("/header/update/image/{id}")
    public ResponseEntity<ApiResponse<AboutUsHeader>> updateImage(
            @PathVariable Long id,
            @RequestParam(name = "photo") MultipartFile file
    ){
        return aboutUsHeaderService.updateImage(id,file);
    }

    @DeleteMapping("/header/delete")
    public ResponseEntity<ApiResponse<?>> deleteAboutUsHeader() {
        return aboutUsHeaderService.delete();
    }

    @PostMapping("/partner-service/create")
    public ResponseEntity<ApiResponse<AboutUsPartner>> createPartnerService(
            @RequestBody AboutUsPartner partner

    ) {
        return aboutUsPartnerService.create(partner);
    }

    @PostMapping("/partner-service/upload-image")
    public ResponseEntity<ApiResponse<AboutUsPartner>> uploadPartnerIcon(
            @RequestParam(name = "id") Long id,
            @RequestPart(value = "icon") MultipartFile icon
    ){
        return aboutUsPartnerService.uploadImage(id,icon);
    }

    @GetMapping("/partner-service/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartner>> findByIdPartner(
            @PathVariable Long id
    ) {
        return aboutUsPartnerService.findById(id);
    }

    @GetMapping("/partner-service/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsPartner>>> findAllPartnerTask() {
        return aboutUsPartnerService.findAll();
    }

    @PutMapping("/partner-service/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartner>> updatePartnerTask(
            @PathVariable Long id,
            @RequestBody AboutUsPartner partner
    ) {
        return aboutUsPartnerService.update(id, partner);
    }

    @PutMapping("/partner-service/update-icon/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartner>> updateIcon(
            @PathVariable Long id,
            @RequestPart(value = "icon") MultipartFile icon
    ){
        return aboutUsPartnerService.updateIcon(id,icon);
    }

    @PutMapping("/partner-service/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActivePartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerService.changeActive(id);
    }

    @DeleteMapping("/partner-service/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deletePartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerService.delete(id);
    }

    @PostMapping("/choose-us/create")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> createChooseUs(
            @RequestBody AboutUsChooseUs aboutUsChooseUs
    ) {
        return aboutUsChooseUsService.create(aboutUsChooseUs);
    }

    @GetMapping("/choose-us/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsChooseUsDTO>> getByIdChooseUs(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    ) {
        return aboutUsChooseUsService.getById(id,lang);
    }

    @GetMapping("/choose-us/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsChooseUsDTO>>> findAllChooseUs(
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang
    ) {
        return aboutUsChooseUsService.findAll(lang);
    }

    @PutMapping("/choose-us/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> update(
            @PathVariable Long id,
            @RequestBody AboutUsChooseUs aboutUsChooseUs
    ) {
        return aboutUsChooseUsService.update(id, aboutUsChooseUs);
    }

    @DeleteMapping("/choose-us/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return aboutUsChooseUsService.delete(id);
    }

    @PostMapping("/create/us-certificate")
    public ResponseEntity<ApiResponse<AboutUsCertificates>> create(
            @RequestParam(name = "photo") MultipartFile file
    ){
        return aboutUsCertificaresService.create(file);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsCertificates>>> getAll(){
        return aboutUsCertificaresService.findAll();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsCertificates>> getById(
            @PathVariable Long id
    ){
        return aboutUsCertificaresService.findById(id);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<ApiResponse<AboutUsCertificates>> update(
            @PathVariable Long id,
            @RequestParam MultipartFile photo
    ){
        return aboutUsCertificaresService.update(id,photo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCertificates(
            @PathVariable Long id
    ){
        return aboutUsCertificaresService.delete(id);
    }
}
