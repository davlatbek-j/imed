package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Event;
import uz.imed.entity.EventAbout;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.EventDTO;
import uz.imed.payload.EventForListDTO;
import uz.imed.repository.EventAboutRepository;
import uz.imed.repository.EventRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService
{

    private final EventRepository eventRepository;

    private final EventAboutRepository eventAboutRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Event>> create(String json, MultipartFile photoFile)
    {
        ApiResponse<Event> response = new ApiResponse<>();
        try
        {
            Event event = objectMapper.readValue(json, Event.class);
            event.setPhoto(photoService.save(photoFile));
            Event save = eventRepository.save(event);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getNameUz());
            eventRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e)
        {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    public ResponseEntity<ApiResponse<?>> findBySlug(String slug, String lang)
    {
        Event event = eventRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Event is not found by slug: " + slug));
        if (lang != null)
        {
            ApiResponse<EventDTO> response = new ApiResponse<>();
            response.setData(new EventDTO(event, lang));
            return ResponseEntity.ok(response);
        }
        ApiResponse<Event> response = new ApiResponse<>();
        response.setData(event);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAllByPageNation(String lang, int page, int size, String city)
    {
        if (city != null && lang == null)
            throw new NotFoundException("If you filter by city, you must send a 'Accept-Language'");

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Event> all = eventRepository.findAll(pageable);
        if (lang != null)
        {
            ApiResponse<List<EventForListDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            if (city != null)
            {
                List<Event> byLang = eventRepository.findAllByAddress(city);
                byLang.forEach(event -> response.getData().add(new EventForListDTO(event, lang)));
                response.setMessage("Found " + byLang.size() + " event(s) by city " + city);
                return ResponseEntity.ok(response);
            }
            all.forEach(event -> response.getData().add(new EventForListDTO(event, lang)));
            response.setMessage("Found " + all.getTotalElements() + " event(s)");
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<Event>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        all.forEach(event -> response.getData().add(event));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Event>> update(Event event)
    {
        ApiResponse<Event> response = new ApiResponse<>();
        Event fromDb = eventRepository.findById(event.getId())
                .orElseThrow(() -> new NotFoundException("Event is not found by id: " + event.getId()));

        if (event.getNameEn() != null)
        {
            fromDb.setNameEn(event.getNameEn());
        }
        if (event.getNameUz() != null)
        {
            fromDb.setNameUz(event.getNameUz());
            String slug = fromDb.getId() + "-" + SlugUtil.makeSlug(event.getNameUz());
            fromDb.setSlug(slug);
        }
        if (event.getNameRu() != null)
        {
            fromDb.setNameRu(event.getNameRu());
        }

        if (event.getOrganizerEn() != null)
        {
            fromDb.setOrganizerEn(event.getOrganizerEn());
        }
        if (event.getOrganizerUz() != null)
        {
            fromDb.setOrganizerUz(event.getOrganizerUz());
        }
        if (event.getOrganizerRu() != null)
        {
            fromDb.setOrganizerRu(event.getOrganizerRu());
        }

        if (event.getCountryEn() != null)
        {
            fromDb.setCountryEn(event.getCountryEn());
        }
        if (event.getCountryUz() != null)
        {
            fromDb.setCountryUz(event.getCountryUz());
        }
        if (event.getCountryRu() != null)
        {
            fromDb.setCountryRu(event.getCountryRu());
        }

        if (event.getDateFromEn() != null)
        {
            fromDb.setDateFromEn(event.getDateFromEn());
        }
        if (event.getDateFromUz() != null)
        {
            fromDb.setDateFromUz(event.getDateFromUz());
        }
        if (event.getDateFromRu() != null)
        {
            fromDb.setDateFromRu(event.getDateFromRu());
        }

        if (event.getDateToEn() != null)
        {
            fromDb.setDateToEn(event.getDateToEn());
        }
        if (event.getDateToUz() != null)
        {
            fromDb.setDateToUz(event.getDateToUz());
        }
        if (event.getDateToRu() != null)
        {
            fromDb.setDateToRu(event.getDateToRu());
        }

        if (event.getTimeFrom() != null)
        {
            fromDb.setTimeFrom(event.getTimeFrom());
        }
        if (event.getTimeTo() != null)
        {
            fromDb.setTimeTo(event.getTimeTo());
        }

        if (event.getAddressEn() != null)
        {
            fromDb.setAddressEn(event.getAddressEn());
        }
        if (event.getAddressUz() != null)
        {
            fromDb.setAddressUz(event.getAddressUz());
        }
        if (event.getAddressRu() != null)
        {
            fromDb.setAddressRu(event.getAddressRu());
        }

        if (event.getParticipationEn() != null)
        {
            fromDb.setParticipationEn(event.getParticipationEn());
        }
        if (event.getParticipationUz() != null)
        {
            fromDb.setParticipationUz(event.getParticipationUz());
        }
        if (event.getParticipationRu() != null)
        {
            fromDb.setParticipationRu(event.getParticipationRu());
        }

        if (event.getPhoneNum() != null)
        {
            fromDb.setPhoneNum(event.getPhoneNum());
        }
        if (event.getEmail() != null)
        {
            fromDb.setEmail(event.getEmail());
        }
        if (event.getAbouts() != null && !event.getAbouts().isEmpty())
        {
            List<EventAbout> newEventAbouts = event.getAbouts();
            List<EventAbout> dbEventAbouts = fromDb.getAbouts();

            if (dbEventAbouts == null)
                dbEventAbouts = new ArrayList<>();

            for (EventAbout newEventAbout : newEventAbouts)
            {
                Long id = newEventAbout.getId();
                for (EventAbout dbEventAbout : dbEventAbouts)
                {
                    if (dbEventAbout.getId().equals(id))
                    {

                        if (newEventAbout.getTitleEn() != null)
                        {
                            dbEventAbout.setTitleEn(newEventAbout.getTitleEn());
                        }
                        if (newEventAbout.getTitleUz() != null)
                        {
                            dbEventAbout.setTitleUz(newEventAbout.getTitleUz());
                        }
                        if (newEventAbout.getTitleRu() != null)
                        {
                            dbEventAbout.setTitleRu(newEventAbout.getTitleRu());
                        }

                        if (newEventAbout.getTextEn() != null)
                        {
                            dbEventAbout.setTextEn(newEventAbout.getTextEn());
                        }
                        if (newEventAbout.getTextUz() != null)
                        {
                            dbEventAbout.setTextUz(newEventAbout.getTextUz());
                        }
                        if (newEventAbout.getTextRu() != null)
                        {
                            dbEventAbout.setTextRu(newEventAbout.getTextRu());
                        }

                        if (newEventAbout.getTitleEn() == null && newEventAbout.getTitleRu() == null && newEventAbout.getTitleUz() == null
                                && newEventAbout.getTextUz() == null && newEventAbout.getTextRu() == null && newEventAbout.getTextEn() == null)
                        {
                            dbEventAbouts.remove(newEventAbout);
                            eventAboutRepository.delete(newEventAbout.getId());
                        }

                    }
                }
                if (id == null)
                {
                    newEventAbout.setEvent(fromDb);
                    dbEventAbouts.add(newEventAbout);
                }
            }
        }

        response.setData(eventRepository.save(fromDb));
        response.setMessage("Updated");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (!eventRepository.existsById(id))
        {
            throw new NotFoundException("Event is not found by id: " + id);
        }
        eventRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
