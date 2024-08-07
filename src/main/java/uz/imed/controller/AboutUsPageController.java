package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Certificate;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.entity.AboutUsHeader;

import uz.imed.payload.AboutUsChooseUsDTO;
import uz.imed.payload.AboutUsHeaderDTO;
import uz.imed.payload.ApiResponse;
import uz.imed.service.AboutUsChooseUsService;
import uz.imed.service.AboutUsHeaderService;

import java.util.List;

@RestController
@RequestMapping("/api/about-us")
@RequiredArgsConstructor
public class AboutUsPageController {


    private final AboutUsChooseUsService aboutUsChooseUsService;

    private final AboutUsHeaderService aboutUsHeaderService;




    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<AboutUsHeader>> createAboutUsHeader(
            @RequestParam("json") String json,
            @RequestParam("photo") MultipartFile file
    ) {return aboutUsHeaderService.create(json,file);
    }


    @GetMapping("/header/get-all")
    public ResponseEntity<ApiResponse<?>> findAllAboutUsHeader(
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return aboutUsHeaderService.findAll(lang);
    }

    @GetMapping("/header/get-by-id/{id}")
    public ResponseEntity<ApiResponse<?>> findAboutUsHeaderById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", required = false) String lang) {
        return aboutUsHeaderService.getById(id,lang);
    }

    @GetMapping("/header/by-slug/{slug}")
    public ResponseEntity<ApiResponse<?>> getBySlug(
            @PathVariable(name = "slug") String slug,
            @RequestHeader(value = "Accept-Language", required = false) String lang

    ){
        return aboutUsHeaderService.get(slug,lang);
    }

    @PutMapping("/header/update")
    public ResponseEntity<ApiResponse<AboutUsHeader>> updateAboutUsHeader(
            @RequestParam("json") String json
    ) {
        return aboutUsHeaderService.update(json);
    }

    @DeleteMapping("/header/delete")

    public ResponseEntity<ApiResponse<?>> delete()
    {
        return aboutUsHeaderService.delete();
    }


    @PostMapping("/choose-us/create")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> createChooseUs(
            @RequestParam("json") String json
    ) {
        return aboutUsChooseUsService.create(json);
    }

    @GetMapping("/choose-us/get/{id}")
    public ResponseEntity<ApiResponse<?>> getByIdChooseUs(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return aboutUsChooseUsService.getById(id,lang);
    }

    @GetMapping("/choose-us/get-all")
    public ResponseEntity<ApiResponse<?>> findAllChooseUs(
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return aboutUsChooseUsService.findAll(lang);
    }

    @GetMapping("/choose-us/by-slug/{slug}")
    public ResponseEntity<ApiResponse<?>> getBySlugCh(
            @PathVariable(name = "slug") String slug,
            @RequestHeader(value = "Accept-Language", required = false) String lang

    ){
        return aboutUsChooseUsService.get(slug,lang);
    }

    @PutMapping("/choose-us/update")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> update(
            @RequestParam("json") String json
    ) {
        return aboutUsChooseUsService.update(json);
    }

    @DeleteMapping("/choose-us/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return aboutUsChooseUsService.delete(id);
    }




}
