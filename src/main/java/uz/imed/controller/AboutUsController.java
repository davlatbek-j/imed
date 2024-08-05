package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.AboutUsChooseSection;
import uz.imed.entity.AboutUsPageHeader;
import uz.imed.entity.AboutUsPageSection;
import uz.imed.payload.ApiResponse;
import uz.imed.service.AboutUsChooseSectionService;
import uz.imed.service.AboutUsPageHeaderService;
import uz.imed.service.AboutUsPageSectionService;

@RestController
@RequestMapping("/api/about-us")
@RequiredArgsConstructor
public class AboutUsController {

    private final AboutUsPageHeaderService aboutUsPageHeaderService;

    private final AboutUsPageSectionService aboutUsPageSectionService;

    private final AboutUsChooseSectionService aboutUsChooseSectionService;

    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<AboutUsPageHeader>> createHeader(
            @RequestParam(value = "json") String header,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return aboutUsPageHeaderService.create(header, photo);
    }

    @GetMapping("/header/get")
    public ResponseEntity<ApiResponse<?>> findHeader(
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return aboutUsPageHeaderService.find(lang);
    }

    @PutMapping("/header/update")
    public ResponseEntity<ApiResponse<AboutUsPageHeader>> updateHeader(
            @RequestBody AboutUsPageHeader aboutUsPageHeader
    ) {
        return aboutUsPageHeaderService.update(aboutUsPageHeader);
    }

    @DeleteMapping("/header/delete")
    public ResponseEntity<ApiResponse<?>> deleteHeader() {
        return aboutUsPageHeaderService.delete();
    }

    @PostMapping("/section/create")
    public ResponseEntity<ApiResponse<AboutUsPageSection>> createSection(
            @RequestParam(value = "json") String section,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return aboutUsPageSectionService.create(section, photo);
    }

    @GetMapping("/section/get")
    public ResponseEntity<ApiResponse<?>> findSection(
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return aboutUsPageSectionService.find(lang);
    }

    @PutMapping("/section/update")
    public ResponseEntity<ApiResponse<AboutUsPageSection>> updateSection(
            @RequestBody AboutUsPageSection section
    ) {
        return aboutUsPageSectionService.update(section);
    }

    @DeleteMapping("/section/delete")
    public ResponseEntity<ApiResponse<?>> deleteSection() {
        return aboutUsPageSectionService.delete();
    }

    @PostMapping("/choose-section/create")
    public ResponseEntity<ApiResponse<AboutUsChooseSection>> createChooseSection(
            @RequestBody AboutUsChooseSection chooseSection
    ) {
        return aboutUsChooseSectionService.create(chooseSection);
    }

    @GetMapping("/choose-section/get/{id}")
    public ResponseEntity<ApiResponse<?>> findById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return aboutUsChooseSectionService.findById(id, lang);
    }

    @GetMapping("/choose-section/get-all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return aboutUsChooseSectionService.findAll(lang);
    }

    @PutMapping("/choose-section/update")
    public ResponseEntity<ApiResponse<AboutUsChooseSection>> update(
            @RequestBody AboutUsChooseSection chooseSection
    ) {
        return aboutUsChooseSectionService.update(chooseSection);
    }

    @DeleteMapping("/choose-section/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return aboutUsChooseSectionService.delete(id);
    }

}
