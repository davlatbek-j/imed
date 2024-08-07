package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Client;
import uz.imed.entity.Photo;
import uz.imed.exeptions.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ClientDTO;
import uz.imed.repository.ClientRepository;
import uz.imed.repository.PhotoRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Client>> create(String json, MultipartFile icon, List<MultipartFile> gallery) {

        ApiResponse<Client> response = new ApiResponse<>();
        try {
            Client client = objectMapper.readValue(json, Client.class);
            Long id = clientRepository.save(new Client()).getId();
            client.setIcon(photoService.save(icon));
            client.setId(id);
            client.setSlug(id + "-" + client.getNameUz());
            ArrayList<Photo> objects = new ArrayList<>();

            client.setGallery(new ArrayList<>());
            for (MultipartFile multipartFile : gallery)
                client.getGallery().add(photoService.save(multipartFile));

            response.setData(clientRepository.save(client));
            response.setMessage("Client succesfully created");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse json: " + e.getMessage());
        }

    }


    public ResponseEntity<ApiResponse<?>> getById(Long id, String lang) {


        if (lang == null) {
            ApiResponse<Client> response = new ApiResponse<>();
            Optional<Client> bySlugIgnoreCase = clientRepository.findById(id);
            Client client = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Client not found by : " + id));
            response.setData(client);
            response.setMessage("Found all client(s)");
            return ResponseEntity.ok(response);
        }

        ApiResponse<ClientDTO> response = new ApiResponse<>();

        if (!clientRepository.existsById(id)) {
            response.setMessage("Client with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Client client = clientRepository.findById(id).get();
        response.setMessage("Found client with id " + id);

        response.setData(new ClientDTO(client, lang));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang) {

        if (lang == null) {
            ApiResponse<List<Client>> response = new ApiResponse<>();
            List<Client> clients = clientRepository.findAll();
            response.setData(clients);
            response.setMessage("Found all client(s)");
            return ResponseEntity.ok(response);
        }

        ApiResponse<List<ClientDTO>> response = new ApiResponse<>();
        List<Client> clients = clientRepository.findAll();
        response.setMessage("Found " + clients.size() + " client(s)");
        response.setData(new ArrayList<>());
        clients.forEach(i -> response.getData().add(new ClientDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<?>> get(String slug, String lang) {
        if (lang == null) {
            ApiResponse<Client> response = new ApiResponse<>();
            Optional<Client> bySlugIgnoreCase = clientRepository.findBySlugIgnoreCase(slug);
            Client client = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Client not found by slug : " + slug));
            response.setData(client);
            response.setMessage("Found all client(s)");
            return ResponseEntity.ok(response);
        }

        ApiResponse<ClientDTO> response = new ApiResponse<>();
        Optional<Client> bySlugIgnoreCase = clientRepository.findBySlugIgnoreCase(slug);
        Client client = bySlugIgnoreCase.orElseThrow(() -> new NotFoundException("Client not found by slug : " + slug));
        response.setData(new ClientDTO(client, lang));
        response.setMessage("Found in language : " + lang);
        return ResponseEntity.ok(response);

    }

    public ResponseEntity<ApiResponse<Client>> update(String json) {
        try {
            Client client = objectMapper.readValue(json, Client.class);
            Long id = client.getId();

            ApiResponse<Client> response = new ApiResponse<>();
            if (!clientRepository.existsById(id)) {
                response.setMessage("Client with id " + id + " does not exist");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Client client1 = clientRepository.findById(id).get();

            if (client.getNameUz() != null ) {
                client1.setNameUz(client.getNameUz());
                String slug = client.getId() + "-" + SlugUtil.makeSlug(client.getNameUz());
                clientRepository.updateSlug(slug, client.getId());
                client1.setSlug(slug);
            }
            if (client.getNameRu() != null ) {
                client1.setNameRu(client.getNameRu());
            }
            if (client.getNameEng() != null ) {
                client1.setNameEng(client.getNameEng());

            }
            if (client.getDescriptionUz() != null ) {
                client1.setDescriptionUz(client.getDescriptionUz());
            }
            if (client.getDescriptionRu() != null ) {
                client1.setDescriptionRu(client.getDescriptionRu());
            }
            if (client.getDescriptionEng() != null ) {
                client1.setDescriptionUz(client.getDescriptionEng());
            }
            if (client.getLocationUrl() != null ) {
                client1.setLocationUrl(client.getLocationUrl());
            }

            client1.setId(id);
            Client save = clientRepository.save(client1);

            response.setMessage("Successfully updated");
            response.setData(save);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public ResponseEntity<ApiResponse<Client>> delete(Long id) {
        ApiResponse<Client> response = new ApiResponse<>();
        try {
            clientRepository.deleteById(id);
            response.setMessage("Successfully deleted");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.setMessage("Error deleting client" + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


}
