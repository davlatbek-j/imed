package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.imed.entity.New;
import uz.imed.payload.ApiResponse;
import uz.imed.service.NewService;

@RestController
@RequestMapping("/v1/new")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> create(
            @RequestParam(value = "json") String newness,
            MultipartHttpServletRequest request
    ) {
        return newService.create(newness, request);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<?>> findBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return newService.findBySlug(slug, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language", required = false) String lang,
            @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
            @RequestParam(value = "page", defaultValue = "1") Integer page
    ) {
        return newService.findAllByPageNation(lang, page, size);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<New>> update(
            @RequestBody New newness
    ) {
        return newService.update(newness);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return newService.delete(id);
    }


}
