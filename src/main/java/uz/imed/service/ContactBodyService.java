package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.ContactBody;
import uz.imed.entity.Event;
import uz.imed.entity.translation.ContactBodyTranslation;
import uz.imed.entity.translation.EventTranslation;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ContactBodyDTO;
import uz.imed.payload.EventDTO;
import uz.imed.repository.ContactBodyRepository;
import uz.imed.repository.ContactBodyTranslationRepo;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContactBodyService {
    private final ContactBodyRepository contactBodyRepository;

    private final ObjectMapper objectMapper;

    private final ContactBodyTranslationRepo contactBodyTranslationRepo;

    public ResponseEntity<ApiResponse<ContactBody>> create(String json) {
        ApiResponse<ContactBody> response = new ApiResponse<>();
        try {
            ContactBody contactBody = objectMapper.readValue(json, ContactBody.class);
            contactBody.setAddable(true);


            ContactBody save = contactBodyRepository.save(contactBody);
            for (ContactBodyTranslation translation : contactBody.getTranslations()) {
                translation.setContactBody(save);
                contactBodyTranslationRepo.save(translation);
            }

            response.setData(save);
            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }


    public ResponseEntity<ApiResponse<List<ContactBodyDTO>>> findAll(String lang) {
        ApiResponse<List<ContactBodyDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<ContactBody> all = contactBodyRepository.findAll();
        all.forEach(contactBody -> response.getData().add(new ContactBodyDTO(contactBody, lang)));
        response.setMessage("Found " + all.size() + " contact(s)");
        return ResponseEntity.ok(response);
    }


    public ResponseEntity<ApiResponse<ContactBody>> update(ContactBody newContactBody) {
        ApiResponse<ContactBody> response = new ApiResponse<>();
        ContactBody existContactBody = contactBodyRepository.findById(newContactBody.getId()).orElseThrow(() -> new NotFoundException("contact is not found by id: " + newContactBody.getId()));
        if (newContactBody.getTranslations() != null) {
            for (ContactBodyTranslation newTranslation : newContactBody.getTranslations()) {
                ContactBodyTranslation existTranslation = existContactBody.getTranslations()
                        .stream()
                        .filter(clientTranslation -> clientTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getAddress() != null) {
                        existTranslation.setAddress(newTranslation.getAddress());
                    }
                    if (newTranslation.getSchedule() != null) {
                        existTranslation.setSchedule(newTranslation.getSchedule());
                    }

                    existTranslation.setContactBody(existContactBody);
                }
            }
        }
        ContactBody save = contactBodyRepository.save(existContactBody);
        response.setData(save);
        return ResponseEntity.ok(response);
    }



    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<ContactBody> optionalContactBody = contactBodyRepository.findAll().stream().findFirst();
        if (optionalContactBody.isEmpty()) {
            response.setMessage("ContactBody is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalContactBody.get().getId();
        contactBodyRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }
}
