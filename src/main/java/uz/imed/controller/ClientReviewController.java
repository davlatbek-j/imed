package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.ClientReview;
import uz.imed.payload.ApiResponse;
import uz.imed.service.ClientReviewService;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ClientReviewController {

    private final ClientReviewService clientReviewService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ClientReview>> create(
            @RequestParam(value = "json") String json,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return clientReviewService.create(json, photo);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<?>> findById(
            @PathVariable Long id,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return clientReviewService.findById(id, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return clientReviewService.findAll(lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ClientReview>> update(
            @RequestBody ClientReview clientReview
    ) {
        return clientReviewService.update(clientReview);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return clientReviewService.delete(id);
    }

}
