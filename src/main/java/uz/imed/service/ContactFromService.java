package uz.imed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.ContactForm;
import uz.imed.entity.translation.ContactBodyTranslation;
import uz.imed.entity.translation.ContactFormTranslation;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ContactFormDTO;
import uz.imed.repository.ContactFormRepository;
import uz.imed.repository.ContactFormTranslationRepo;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactFromService {

    private final ContactFormRepository contactFormRepository;

    private final ContactFormTranslationRepo repo;

    public ResponseEntity<ApiResponse<ContactForm>> create(ContactForm contactForm) {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isPresent()) {
            return update(contactForm);
        }
        ContactForm save = contactFormRepository.save(contactForm);
        for (ContactFormTranslation translation : contactForm.getTranslations()) {
            translation.setContactFrom(save);
            repo.save(translation);
        }
        response.setData(save);
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<ContactFormDTO>> find(String lang) {
        ApiResponse<ContactFormDTO> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isEmpty()) {
            response.setMessage("ContactForm is not found");
            return ResponseEntity.status(404).body(response);
        }
        ContactForm contactForm = contactFormOptional.get();
        response.setData(new ContactFormDTO(contactForm,lang));
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<ContactForm>> update(ContactForm contactForm) {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isEmpty()) {
            response.setMessage("ContactForm is not found");
            return ResponseEntity.status(404).body(response);
        }
        ContactForm oldContactForm = contactFormOptional.get();
        oldContactForm.setPhoneNumber(contactForm.getPhoneNumber());
        oldContactForm.setEmail(contactForm.getEmail());
        if (contactForm.getTranslations() != null) {
            for (ContactFormTranslation newTranslation : contactForm.getTranslations()) {
                ContactFormTranslation existTranslation = oldContactForm.getTranslations()
                        .stream()
                        .filter(clientTranslation -> clientTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getLocation() != null) {
                        existTranslation.setLocation(newTranslation.getLanguage());
                    }
                    if (newTranslation.getWorkTime() != null) {
                        existTranslation.setWorkTime(newTranslation.getWorkTime());
                    }

                    existTranslation.setContactFrom(oldContactForm);
                }
            }
        }
        ContactForm save = contactFormRepository.save(oldContactForm);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isEmpty()) {
            response.setMessage("ContactForm is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = contactFormOptional.get().getId();
        contactFormRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
