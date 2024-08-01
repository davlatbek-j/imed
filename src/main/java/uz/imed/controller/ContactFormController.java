package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.imed.entity.ContactForm;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ContactFormDTO;
import uz.imed.service.ContactFromService;

@RestController
@RequestMapping("/contact-form")
@RequiredArgsConstructor
public class ContactFormController {

    private final ContactFromService contactFormService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ContactForm>> create(
            @RequestBody ContactForm contactForm
    ) {
        return contactFormService.create(contactForm);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ContactFormDTO>> find(
            @RequestHeader(value = "Accept-Language") String lang
    ) {
        return contactFormService.find(lang);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ContactForm>> update(
            @RequestBody ContactForm contactForm
    ) {
        return contactFormService.update(contactForm);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return contactFormService.delete();
    }

}
