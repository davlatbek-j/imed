package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Event;
import uz.imed.payload.ApiResponse;
import uz.imed.service.EventService;

@RestController
@RequestMapping("/v1/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Event>> create(
            @RequestParam(value = "json") String event,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return eventService.create(event, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<?>> getBySlug(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language", required = false) String lang
    ) {
        return eventService.findBySlug(slug, lang);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<?>> findAll(
            @RequestHeader(value = "Accept-Language",required = false) String lang,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "10") Integer size,
            @RequestParam(value = "city", required = false) String city
    ) {
        return eventService.findAllByPageNation(lang,page,size,city);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Event>> update(
            @RequestBody Event event
    ) {
        return eventService.update(event);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return eventService.delete(id);
    }


}
