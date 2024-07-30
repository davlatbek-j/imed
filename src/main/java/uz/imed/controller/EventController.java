package uz.imed.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Event;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.EventDTO;
import uz.imed.service.EventService;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Event>> add(
            @RequestBody Event event) {
        return eventService.add(event);
    }

    @PostMapping("/upload_image")

    public ResponseEntity<ApiResponse<Event>> aploadImage(
            @RequestParam(name = "id") Long id,
            @RequestParam(name = "photo") MultipartFile file
    ) {
        return eventService.uploadImage(id, file);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<EventDTO>> findBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang) {
        return eventService.get(slug, lang);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAll(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort,
            @RequestHeader(value = "Accept-Language", defaultValue = "ru") String lang) {
        return eventService.getAll(city, page, size, sort, lang);
    }

    @GetMapping("/city-list")
    public ResponseEntity<ApiResponse<List<String>>> getCityList()
    {
        return eventService.getCityList();
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Event>> update(
            @RequestBody Event event)
    {
        return eventService.update(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id)
    {
        return eventService.delete(id);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteAbouts(
            @RequestParam(value = "about-id") Long aboutId)
    {
        return eventService.deleteAbout(aboutId);
    }

}