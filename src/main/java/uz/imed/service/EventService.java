package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Event;
import uz.imed.entity.translation.EventTranslation;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.EventDTO;
import uz.imed.repository.EventRepository;
import uz.imed.repository.EventTranslationRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EventTranslationRepository eventTranslationRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Event>> create(String json,MultipartFile photo) {
        ApiResponse<Event> response = new ApiResponse<>();
        try {
            Event event = objectMapper.readValue(json, Event.class);

            event.setCoverPhoto(photoService.save(photo));
            Event save = eventRepository.save(event);
            for (EventTranslation translation : event.getEventTranslations()) {
                translation.setEvent(save);
                eventTranslationRepository.save(translation);
            }
            String slug = save.getId() + "-" + SlugUtil.makeSlug(getEventNameForSlug(event.getEventTranslations()));
            eventRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    private String getEventNameForSlug(List<EventTranslation> translations) {
        return translations
                .stream()
                .filter(translation -> translation.getLanguage().equals("en"))
                .findFirst()
                .map(EventTranslation::getHeading)
                .orElse(null);
    }


    public ResponseEntity<ApiResponse<EventDTO>> findBySlug(String slug, String lang) {
        ApiResponse<EventDTO> response = new ApiResponse<>();
        Event event = eventRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Event is not found by slug: " + slug));
        response.setData(new EventDTO(event, lang));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Event>> findFullDataById(Long id) {
        ApiResponse<Event> response = new ApiResponse<>();
        Event event = eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event is not found by id: " + id));
        response.setData(event);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<EventDTO>>> findAll(String lang) {
        ApiResponse<List<EventDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Event> all = eventRepository.findAll();
        all.forEach(event -> response.getData().add(new EventDTO(event, lang)));
        response.setMessage("Found " + all.size() + " client(s)");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Event>> update(Event newEvent) {
        ApiResponse<Event> response = new ApiResponse<>();
        Event existEvent = eventRepository.findById(newEvent.getId()).orElseThrow(() -> new NotFoundException("Event is not found by id: " + newEvent.getId()));
        if (newEvent.getEventTranslations() != null) {
            for (EventTranslation newTranslation : newEvent.getEventTranslations()) {
                EventTranslation existTranslation = existEvent.getEventTranslations()
                        .stream()
                        .filter(clientTranslation -> clientTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getHeading() != null) {
                        if (newTranslation.getHeading().equals("en")) {
                            String slug = existEvent.getId() + "-" + SlugUtil.makeSlug(newTranslation.getHeading());
                            existEvent.setSlug(slug);
                        }
                        existTranslation.setHeading(newTranslation.getHeading());
                    }
                    if (newTranslation.getOrganizer() != null) {
                        existTranslation.setOrganizer(newTranslation.getOrganizer());
                    }
                    if (newTranslation.getAddress() != null) {
                        existTranslation.setAddress(newTranslation.getAddress());
                    }
                    if (newTranslation.getText() != null) {
                        existTranslation.setText(newTranslation.getText());
                    }
                    existTranslation.setEvent(existEvent);
                }
            }
        }
        Event save = eventRepository.save(existEvent);
        response.setData(save);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!eventRepository.existsById(id)) {
            throw new NotFoundException("Event is not found by id: " + id);
        }
        eventRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }
}
