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
import uz.imed.entity.AboutUsHeader;
import uz.imed.entity.Event;
import uz.imed.entity.EventAbout;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.EventDTO;
import uz.imed.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Event>> add(Event event) {
        ApiResponse<Event> response = new ApiResponse<>();


        Long id = eventRepository.save(new Event()).getId();
        event.setId(id);
        event.setSlug(id + "-" + event.getHeadingEng());
        response.setData(eventRepository.save(event));
        response.setMessage("Added");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    public ResponseEntity<ApiResponse<Event>> uploadImage(Long id, MultipartFile file) {
        ApiResponse<Event> response = new ApiResponse<>();

        if (!(file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/svg+xml"))) {
            response.setMessage("Invalid file , only image/png or image/svg+xml");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!eventRepository.existsById(id)) {
            response.setMessage("Data with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Event event = eventRepository.findById(id).get();
        response.setMessage("Found data with id " + id);
        event.setCoverPhoto(photoService.save(file));
        Event saved = eventRepository.save(event);
        response.setMessage("Successfully created");
        response.setData(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<EventDTO>> get(String slug, String lang) {
        ApiResponse<EventDTO> response = new ApiResponse<>();
        Optional<Event> bySlug = eventRepository.findBySlug(slug);
        if (bySlug.isEmpty())
            throw new NotFoundException("Event with slug \'" + slug + "\' not found");
        response.setData(new EventDTO(bySlug.get(), lang));
        response.setMessage("Found");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<List<EventDTO>>> getAll(String city, int page, int size, String[] sort, String lang) {
        ApiResponse<List<EventDTO>> response = new ApiResponse<>();
        List<Event> all = new ArrayList<>();

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort sortOrder = Sort.by(direction, sort[0]);

        page = Math.max(page - 1, 0); // Pagination Default to 1 instead of 0
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (city == null) {
            all = eventRepository.findAll(pageable).getContent();
        } else {
            all = eventRepository.findByCityEqualsIgnoreCase(city, pageable);
        }
        ArrayList<EventDTO> objects = new ArrayList<>();
        all.forEach(i -> objects.add(new EventDTO(i, lang)));
        response.setData(objects);
        response.setMessage("Found " + all.size() + " event(s)");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<List<String>>> getCityList()
    {
        ApiResponse<List<String>> response = new ApiResponse<>();
        response.setData(eventRepository.getCity());
        response.setMessage("Found " + response.getData().size() + " event(s)");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<Event>> update(Event newEvent)
    {
        ApiResponse<Event> response = new ApiResponse<>();
        Optional<Event> byId = eventRepository.findById(newEvent.getId());
        if (byId.isEmpty())
            throw new NotFoundException("Event not found with id " + newEvent.getId());

        Event fromDB = byId.get();

        if (newEvent.getHeadingEng() != null)
        {
            fromDB.setHeadingEng(newEvent.getHeadingEng());
            fromDB.setSlug(fromDB.getId() + "-" + fromDB.getHeadingEng());
        }

        if (newEvent.getHeadingEng() != null)
        {
            fromDB.setHeadingUz(newEvent.getHeadingUz());
        }
        if (newEvent.getHeadingRu() != null)
        {
            fromDB.setHeadingRu(newEvent.getHeadingRu());
        }


        if (newEvent.getDateFrom() != null) fromDB.setDateFrom(newEvent.getDateFrom());
        if (newEvent.getDateTo() != null) fromDB.setDateTo(newEvent.getDateTo());
        if (newEvent.getTimeFrom() != null) fromDB.setTimeFrom(newEvent.getTimeFrom());
        if (newEvent.getTimeTo() != null) fromDB.setTimeTo(newEvent.getTimeTo());
        if (newEvent.getOrganizerUz() != null) fromDB.setOrganizerUz(newEvent.getOrganizerUz());
        if (newEvent.getOrganizerRu() != null) fromDB.setOrganizerRu(newEvent.getOrganizerRu());
        if (newEvent.getOrganizerEng() != null) fromDB.setOrganizerEng(newEvent.getOrganizerEng());
        if (newEvent.getCity() != null) fromDB.setCity(newEvent.getCity());
        if (newEvent.getAddressUz() != null) fromDB.setAddressUz(newEvent.getAddressUz());
        if (newEvent.getAddressRu() != null) fromDB.setAddressUz(newEvent.getAddressRu());
        if (newEvent.getAddressEng() != null) fromDB.setAddressUz(newEvent.getAddressEng());
        if (newEvent.getAbouts() != null)
        {
            List<EventAbout> newEventAbouts = newEvent.getAbouts();
            List<EventAbout> dbAbouts = fromDB.getAbouts();
            for (EventAbout newEventAbout : newEventAbouts)
            {
                Long id = newEventAbout.getId();
                for (EventAbout dbAbout : dbAbouts)
                {
                    if (dbAbout.getId().equals(id))
                    {
                        dbAbout.setHeadingUz(newEventAbout.getHeadingUz());
                        dbAbout.setTextUz(newEventAbout.getTextUz());
                    }
                }
                if (id == null)
                {
                    dbAbouts.add(newEventAbout);
                }
            }
        }

        response.setData(eventRepository.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (!eventRepository.existsById(id))
            throw new NotFoundException("Event not found with id " + id);

        eventRepository.deleteById(id);
        response.setMessage("Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteAbout(Long aboutId)
    {
        ApiResponse<?> response = new ApiResponse<>();
        List<Event> fromDB = eventRepository.findAll();
        for (int i = 0; i < fromDB.size(); i++)
        {
            List<EventAbout> abouts = fromDB.get(i).getAbouts();
            for (int j = 0, aboutsSize = abouts.size(); j < aboutsSize; j++)
            {
                if (abouts.get(j).getId().equals(aboutId))
                {
                    abouts.remove(j);
                    eventRepository.save(fromDB.get(i));
                    break;
                }
            }
        }
        response.setMessage("Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
