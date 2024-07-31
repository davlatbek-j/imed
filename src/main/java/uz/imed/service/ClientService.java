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
import uz.imed.entity.ClientTranslation;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ClientDTO;
import uz.imed.repository.ClientRepository;
import uz.imed.repository.ClientTranslationRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    private final ClientTranslationRepository clientTranslationRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Client>> create(String json, MultipartFile photoFile) {
        ApiResponse<Client> response = new ApiResponse<>();
        try {
            Client client = objectMapper.readValue(json, Client.class);
            client.setActive(true);
            client.setLogo(photoService.save(photoFile));
            Client savedClient = clientRepository.save(client);
            for (ClientTranslation translation : client.getTranslations()) {
                translation.setClient(savedClient);
                clientTranslationRepository.save(translation);
            }
            String slug = savedClient.getId() + "-" + SlugUtil.makeSlug(getClientNameForSlug(client.getTranslations()));
            clientRepository.updateSlug(slug, savedClient.getId());
            savedClient.setSlug(slug);
            response.setData(savedClient);
            return ResponseEntity.ok(response);

        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    private String getClientNameForSlug(List<ClientTranslation> translations) {
        return translations
                .stream()
                .filter(translation -> translation.getLanguage().equals("en"))
                .findFirst()
                .map(ClientTranslation::getName)
                .orElse(null);
    }

    public ResponseEntity<ApiResponse<ClientDTO>> findBySlug(String slug, String lang) {
        ApiResponse<ClientDTO> response = new ApiResponse<>();
        Client client = clientRepository.findBySlug(slug).orElseThrow(() -> new NotFoundException("Client is not found by slug: " + slug));
        response.setData(new ClientDTO(client, lang));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Client>> findFullDataById(Long id) {
        ApiResponse<Client> response = new ApiResponse<>();
        Client client = clientRepository.findById(id).orElseThrow(() -> new NotFoundException("Client is not found by id: " + id));
        response.setData(client);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<ClientDTO>>> findAll(String lang) {
        ApiResponse<List<ClientDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Client> all = clientRepository.findAll();
        all.forEach(client -> response.getData().add(new ClientDTO(client, lang)));
        response.setMessage("Found " + all.size() + " client(s)");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<Client>> update(Client newClient) {
        ApiResponse<Client> response = new ApiResponse<>();
        Client existClient = clientRepository.findById(newClient.getId()).orElseThrow(() -> new NotFoundException("Client is not found by id: " + newClient.getId()));
        if (newClient.getTranslations() != null) {
            for (ClientTranslation newTranslation : newClient.getTranslations()) {
                ClientTranslation existTranslation = existClient.getTranslations()
                        .stream()
                        .filter(clientTranslation -> clientTranslation.getLanguage().equals(newTranslation.getLanguage()))
                        .findFirst()
                        .orElseThrow(null);
                if (existTranslation != null) {
                    if (newTranslation.getName() != null) {
                        if (newTranslation.getName().equals("en")) {
                            String slug = existClient.getId() + "-" + SlugUtil.makeSlug(newTranslation.getName());
                            existClient.setSlug(slug);
                        }
                        existTranslation.setName(newTranslation.getName());
                    }
                    if (newTranslation.getDescription() != null) {
                        existTranslation.setDescription(newTranslation.getDescription());
                    }
                    if (newTranslation.getAddress() != null) {
                        existTranslation.setAddress(newTranslation.getAddress());
                    }
                    existTranslation.setClient(existClient);
                }
            }
        }
        Client save = clientRepository.save(existClient);
        response.setData(save);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!clientRepository.existsById(id)) {
            throw new NotFoundException("Client is not found by id: " + id);
        }
        clientRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
