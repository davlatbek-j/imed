package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsAdvantages;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.AboutUsHeader;
import uz.imed.entity.AboutUsPartner;
import uz.imed.payload.AboutUsMainDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.service.AboutUsAdvantagesService;
import uz.imed.service.AboutUsChooseUsService;
import uz.imed.service.AboutUsHeaderService;
import uz.imed.service.AboutUsPartnerService;

import java.util.List;

@RestController
@RequestMapping("/about-us")
@RequiredArgsConstructor
public class AboutUsPageController {

    private final AboutUsHeaderService aboutUsHeaderService;

    private final AboutUsPartnerService aboutUsPartnerTaskService;

    private final AboutUsChooseUsService aboutUsChooseUsService;

    private final AboutUsAdvantagesService aboutUsAdvantagesService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<AboutUsMainDTO>> findMainAbout() {
        return aboutUsHeaderService.findForMainPage();
    }

    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<AboutUsHeader>> createAboutUsHeader(
            @RequestParam(value = "json") String aboutUsHeader,
            @RequestPart(value = "photos") MultipartFile photo
    ) {
        return aboutUsHeaderService.create(aboutUsHeader, photo);
    }

    @GetMapping("/header/get")
    public ResponseEntity<ApiResponse<AboutUsHeader>> findAboutUsHeader() {
        return aboutUsHeaderService.find();
    }

    @PutMapping("/header/update")
    public ResponseEntity<ApiResponse<AboutUsHeader>> updateAboutUsHeader(
            @RequestBody AboutUsHeader aboutUsHeader

    ) {
        return aboutUsHeaderService.update(aboutUsHeader);
    }

    @DeleteMapping("/header/delete")
    public ResponseEntity<ApiResponse<?>> deleteAboutUsHeader() {
        return aboutUsHeaderService.delete();
    }

    @PostMapping("/partner-service/create")
    public ResponseEntity<ApiResponse<AboutUsPartner>> createPartnerService(
            @RequestParam(value = "json") String partnerTask,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return aboutUsPartnerTaskService.create(partnerTask, photo);
    }

    @GetMapping("/partner-service/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartner>> findByIdPartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerTaskService.findById(id);
    }

    @GetMapping("/partner-service/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsPartner>>> findAllPartnerTask() {
        return aboutUsPartnerTaskService.findAll();
    }

    @PutMapping("/partner-service/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartner>> updatePartnerTask(
            @PathVariable Long id,
            @RequestParam(value = "json") String partnerTask,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return aboutUsPartnerTaskService.update(id, partnerTask, photo);
    }

    @PutMapping("/partner-service/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActivePartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerTaskService.changeActive(id);
    }

    @DeleteMapping("/partner-service/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deletePartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerTaskService.delete(id);
    }

    @PostMapping("/choose-us/create")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> createChooseUs(
            @RequestBody AboutUsChooseUs aboutUsChooseUs
    ) {
        return aboutUsChooseUsService.create(aboutUsChooseUs);
    }

    @GetMapping("/choose-us/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> getByIdChooseUs(
            @PathVariable Long id
    ) {
        return aboutUsChooseUsService.findById(id);
    }

    @GetMapping("/choose-us/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsChooseUs>>> findAllChooseUs() {
        return aboutUsChooseUsService.findAll();
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


    @PostMapping("/advantages/create")
    public ResponseEntity<ApiResponse<AboutUsAdvantages>> createAdvantages(
            @RequestParam MultipartFile file
    ) {
        return aboutUsAdvantagesService.create(file);
    }

    @GetMapping("/advantages/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsAdvantages>> getByIdAdvantages(
            @PathVariable Long id
    ) {
        return aboutUsAdvantagesService.findById(id);
    }

    @GetMapping("/advantages/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsAdvantages>>> findAllAdvantages() {
        return aboutUsAdvantagesService.findAll();
    }

    @PutMapping("/advantages/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsAdvantages>> update(
            @PathVariable Long id,
            @RequestParam MultipartFile file
    ) {
        return aboutUsAdvantagesService.update(id, file);
    }

    @DeleteMapping("/advantages/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAdvantages(
            @PathVariable Long id
    ) {
        return aboutUsAdvantagesService.delete(id);
    }
}
