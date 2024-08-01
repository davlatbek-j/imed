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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Event>> create(
            @RequestParam(value = "json") String event,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return eventService.create(event, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<EventDTO>> findById(
            @PathVariable String slug,
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return eventService.findBySlug(slug, lang);
    }

    @GetMapping("/get-full-data/{id}")
    public ResponseEntity<ApiResponse<Event>> findFullData(
            @PathVariable Long id
    ) {
        return eventService.findFullDataById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<EventDTO>>> findAll(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return eventService.findAll(lang);
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
        return eventService.deleteById(id);
    }

}