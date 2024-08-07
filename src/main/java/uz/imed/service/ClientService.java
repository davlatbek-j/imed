package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Client;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ClientMainDataDTO;
import uz.imed.repository.ClientRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService
{

    private final ClientRepository clientRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Client>> create(String json, MultipartFile icon, List<MultipartFile> gallery)
    {

        ApiResponse<Client> response = new ApiResponse<>();
        try
        {
            Client client = objectMapper.readValue(json, Client.class);
            Long id = clientRepository.save(new Client()).getId();
            client.setLogo(photoService.save(icon));
            client.setId(id);
            client.setSlug(id + "-" + SlugUtil.makeSlug(client.getName()));

            client.setGallery(new ArrayList<>());
            for (MultipartFile multipartFile : gallery)
                client.getGallery().add(photoService.save(multipartFile));

            response.setData(clientRepository.save(client));
            response.setMessage("Client succesfully created");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to parse json: " + e.getMessage());
        }

    }


    public ResponseEntity<ApiResponse<?>> getById(Long id, String lang)
    {


        if (lang == null)
        {
            ApiResponse<Client> response = new ApiResponse<>();
            Optional<Client> bySlugIgnoreCase = clientRepository.findById(id);
            Client client = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Client not found by : " + id));
            response.setData(client);
            response.setMessage("Found all client(s)");
            return ResponseEntity.ok(response);
        }

        ApiResponse<ClientDTO> response = new ApiResponse<>();

        if (!clientRepository.existsById(id))
        {
            response.setMessage("Client with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Client client = clientRepository.findById(id).get();
        response.setMessage("Found client with id " + id);

        response.setData(new ClientDTO(client, lang));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang, int page, int size)
    {
        Pageable pageable = PageRequest.of(page, size);

        ApiResponse<List<ClientMainDataDTO>> response = new ApiResponse<>();
        List<Client> clients = clientRepository.findAll(pageable).getContent();
        response.setMessage("Found " + clients.size() + " client(s)");
        response.setData(new ArrayList<>());
        clients.forEach(i -> response.getData().add(new ClientMainDataDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang, int gallerySize)
    {
        Client client = clientRepository.findBySlugIgnoreCase(slug).orElseThrow(() -> new NotFoundException("Client not found by slug : " + slug));

        if (lang == null)
        {
            ApiResponse<Client> response = new ApiResponse<>();
            response.setData(client);
            response.setMessage("All Language(s)");
            return ResponseEntity.ok(response);
        }

        ApiResponse<ClientDTO> response = new ApiResponse<>();
        response.setData(new ClientDTO(client, lang, gallerySize));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Client>> update(Client newClient)
    {
        if (newClient == null || newClient.getId() == null)
            throw new NotFoundException("Client id not given");

        Long id = newClient.getId();
        Client fromDB = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client not found by id: " + id));
        ApiResponse<Client> response = new ApiResponse<>();

        if (newClient.getName() != null)
        {
            fromDB.setName(newClient.getName());
            String slug = newClient.getId() + "-" + SlugUtil.makeSlug(newClient.getName());
            clientRepository.updateSlug(slug, newClient.getId());
            fromDB.setSlug(slug);
        }

        if (newClient.getDescriptionUz() != null)
        {
            fromDB.setDescriptionUz(newClient.getDescriptionUz());
        }
        if (newClient.getDescriptionRu() != null)
        {
            fromDB.setDescriptionRu(newClient.getDescriptionRu());
        }
        if (newClient.getDescriptionEn() != null)
        {
            fromDB.setDescriptionUz(newClient.getDescriptionEn());
        }
        if (newClient.getLocationUrl() != null)
        {
            fromDB.setLocationUrl(newClient.getLocationUrl());
        }

        fromDB.setId(id);
        Client save = clientRepository.save(fromDB);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<ApiResponse<Client>> delete(Long id)
    {
        if (!clientRepository.existsById(id))
            throw new NotFoundException("Client not found by id: " + id);

        ApiResponse<Client> response = new ApiResponse<>();
        clientRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
