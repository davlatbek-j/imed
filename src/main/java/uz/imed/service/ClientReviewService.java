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
import uz.imed.entity.ClientReview;
import uz.imed.entity.New;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ClientReviewDTO;
import uz.imed.payload.NewDTO;
import uz.imed.repository.ClientReviewRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientReviewService {

    private final ClientReviewRepository clientReviewRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<ClientReview>> create(String json, MultipartFile photoFile) {
        ApiResponse<ClientReview> response = new ApiResponse<>();
        try {
            ClientReview clientReview = objectMapper.readValue(json, ClientReview.class);
            clientReview.setLogo(photoService.save(photoFile));
            ClientReview save = clientReviewRepository.save(clientReview);
            response.setData(save);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> findById(Long id, String lang) {
        ClientReview clientReview = clientReviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment is not found by id: " + id));
        if (lang != null) {
            ApiResponse<ClientReviewDTO> response = new ApiResponse<>();
            response.setData(new ClientReviewDTO(clientReview, lang));
            return ResponseEntity.ok(response);
        }
        ApiResponse<ClientReview> response = new ApiResponse<>();
        response.setData(clientReview);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAll(String lang, int page, int size) {
        Pageable pageable=PageRequest.of(page-1,size);
        Page<ClientReview> all = clientReviewRepository.findAll(pageable);
        if (lang != null) {
            ApiResponse<List<ClientReviewDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            all.forEach(clientReview -> response.getData().add(new ClientReviewDTO(clientReview, lang)));
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<ClientReview>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        all.forEach(clientReview -> response.getData().add(clientReview));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<ClientReview>> update(ClientReview clientReview) {
        ApiResponse<ClientReview> response = new ApiResponse<>();
        ClientReview fromDB = clientReviewRepository.findById(clientReview.getId())
                .orElseThrow(() -> new NotFoundException("Comment is not found by id: " + clientReview.getId()));

        if (clientReview.getClientName() != null) {
            fromDB.setClientName(clientReview.getClientName());
        }

        if (clientReview.getCommentUz() != null) {
            fromDB.setCommentUz(clientReview.getCommentUz());
        }
        if (clientReview.getCommentRu() != null) {
            fromDB.setCommentRu(clientReview.getCommentRu());
        }
        if (clientReview.getCommentEn() != null) {
            fromDB.setCommentEn(clientReview.getCommentEn());
        }

        response.setData(clientReviewRepository.save(fromDB));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!clientReviewRepository.existsById(id)) {
            throw new NotFoundException("Comment is not found by id: " + id);
        }
        clientReviewRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
