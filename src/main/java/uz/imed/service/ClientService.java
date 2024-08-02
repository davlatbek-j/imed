package uz.imed.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Client;
import uz.imed.entity.Photo;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ClientDTO;
import uz.imed.repository.ClientRepository;
import uz.imed.repository.PhotoRepository;
import uz.imed.util.SlugUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;
    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Client>> create(Client client){

        ApiResponse<Client> response = new ApiResponse<>();
        Client save = clientRepository.save(client);
        String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getNameEng());
        clientRepository.updateSlug(slug, save.getId());
        save.setSlug(slug);
        response.setData(save);
        response.setMessage("Client succesfully created");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    public ResponseEntity<ApiResponse<Client>> uploadImage(Long id, MultipartFile icon, List<MultipartFile> gallery) {
        ApiResponse<Client> response = new ApiResponse<>();

        if (!(icon.getContentType().equals("image/png") ||
                icon.getContentType().equals("image/svg+xml"))) {
            response.setMessage("Invalid file , only image/png or image/svg+xml");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        if (!clientRepository.existsById(id)) {
            response.setMessage("Client with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        Client client = clientRepository.findById(id).get();
        Photo save = photoService.save(icon);
        client.setIcon(save);

        client.setGallery(new ArrayList<>());
        for (MultipartFile multipartFile : gallery)
            client.getGallery().add(photoService.save(multipartFile));

        Client save1 = clientRepository.save(client);
        response.setMessage("Photo succesfully saved");
        response.setData(save1);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<ApiResponse<ClientDTO>> getById(Long id, String lang) {
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

    public ResponseEntity<ApiResponse<List<ClientDTO>>> findAll(String lang) {
        ApiResponse<List<ClientDTO>> response = new ApiResponse<>();
        List<Client> clients = clientRepository.findAll();
        response.setMessage("Found " + clients.size() + " client(s)");
        response.setData(new ArrayList<>());
        clients.forEach(i -> response.getData().add(new ClientDTO(i, lang)));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<ApiResponse<Client>> update(Long id, Client client) {
        ApiResponse<Client> response = new ApiResponse<>();
        if (!clientRepository.existsById(id)) {
            response.setMessage("Client with id " + id + " does not exist");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        Client client1 = clientRepository.findById(id).get();

        if (client.getNameUz() != null || !client.getNameUz().isEmpty()) {
            client1.setNameUz(client.getNameUz());
        }
        if (client.getNameRu() != null || !client.getNameRu().isEmpty()) {
            client1.setNameRu(client.getNameRu());
        }
        if (client.getNameEng() != null || !client.getNameEng().isEmpty()) {
            client1.setNameEng(client.getNameEng());
            String slug = client.getId() + "-" + SlugUtil.makeSlug(client.getNameEng());
            clientRepository.updateSlug(slug, client.getId());
            client1.setSlug(slug);
        }
        if (client.getDescriptionUz() != null || !client.getDescriptionUz().isEmpty()) {
            client1.setDescriptionUz(client.getDescriptionUz());
        }
        if (client.getDescriptionRu() != null || !client.getDescriptionRu().isEmpty()) {
            client1.setDescriptionRu(client.getDescriptionRu());
        }
        if (client.getDescriptionEng() != null || !client.getDescriptionEng().isEmpty()) {
            client1.setDescriptionUz(client.getDescriptionEng());
        }
        if (client.getLocationUrl() != null || !client.getLocationUrl().isEmpty()) {
            client1.setLocationUrl(client.getLocationUrl());
        }

        client1.setId(id);
        Client save = clientRepository.save(client1);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    public ResponseEntity<ApiResponse<Client>> updatePhoto(Long id, MultipartFile icon, List<MultipartFile> newGallery) {

        ApiResponse<Client> response = new ApiResponse<>();


        if (!(icon.getContentType().equals("image/png") ||
                icon.getContentType().equals("image/svg+xml"))) {
            response.setMessage("Invalid file , only image/png or image/svg+xml");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        if (!clientRepository.existsById(id)) {
            if (!clientRepository.existsById(id)) {
                response.setMessage("Client with id " + id + " does not exist");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        }
        Client client = clientRepository.findById(id).get();

        Photo oldIcon = client.getIcon();
        List<Photo> oldGallery = client.getGallery();

        if (icon == null || icon.isEmpty())
            client.setIcon(oldIcon);
        else
            client.setIcon(photoService.save(icon));

        if (newGallery == null || newGallery.get(0).isEmpty())
            client.setGallery(oldGallery);
        else {
            client.setGallery(new ArrayList<>());
            for (MultipartFile multipartFile : newGallery)
                if (multipartFile.getSize() > 0)
                    client.getGallery().add(photoService.save(multipartFile));
        }


        client.setId(id);
        Client save = clientRepository.save(client);

        response.setMessage("Successfully updated");
        response.setData(save);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
