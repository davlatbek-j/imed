package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.New;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.NewDTO;
import uz.imed.service.NewService;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> createNew(
            @RequestParam(value = "json") String newness,
            @RequestPart(value = "mainPhoto") MultipartFile mainPhoto,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return newService.create(newness, mainPhoto, photos);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<New>> findBySlug(
            @PathVariable String slug
    ) {
        return newService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll() {
        return newService.findAll();
    }

    @GetMapping("/get-page")
    public ResponseEntity<ApiResponse<Page<NewDTO>>> findAllByPageNation(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "12") Integer size
    ) {
        return newService.findAllByPageNation(page, size);
    }

    @GetMapping("/get-all-other/{newSlug}")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(
            @PathVariable String newSlug
    ) {
        return newService.findOtherFourNews(newSlug);
    }

    @GetMapping("/get-four")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findFourNews() {
        return newService.findFourNews();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<New>> update(
            @PathVariable Long id,
            @RequestParam(value = "json") String newness,
            @RequestPart(value = "mainPhoto") MultipartFile mainPhoto,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return newService.update(id, newness, mainPhoto, photos);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return newService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNew(
            @PathVariable Long id
    ) {
        return newService.deleteById(id);
    }


}
