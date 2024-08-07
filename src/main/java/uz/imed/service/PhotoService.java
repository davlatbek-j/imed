package uz.imed.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Photo;
import uz.imed.exception.IllegalPhotoTypeException;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PhotoDTO;
import uz.imed.repository.PhotoRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PhotoService {

    @Value("${photo.upload.path}")
    private String photoUploadPath;

    @Value("${server.base-url}")
    private String baseUrl;

    private final PhotoRepository photoRepository;

    public Photo save(MultipartFile file) {
        if (file.getContentType() != null && !(file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/svg+xml") ||
                file.getContentType().equals("image/jpeg"))) {
            throw new IllegalPhotoTypeException("Unsupported image type: " + file.getContentType());
        }

        try {
            Photo photo = photoRepository.save(new Photo());
            String originalFileName = photo.getId() + "-" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "%20");

            Path filePath = Paths.get(photoUploadPath + File.separator + originalFileName);
            File uploadDir = new File(filePath.toUri());
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            file.transferTo(uploadDir);

            photo.setName(originalFileName);
            photo.setFilepath(uploadDir.getAbsolutePath());
            photo.setType(file.getContentType());
            photo.setHttpUrl(baseUrl + "/photo/" + photo.getName());

            return photoRepository.save(photo);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<byte[]> findByNameOrId(String nameOrId) {
        try {
            Long id = null;
            try {
                id = Long.valueOf(nameOrId);
            } catch (NumberFormatException ignored) {
                nameOrId = nameOrId.replaceAll(" ", "%20");
            }

            Photo photo = photoRepository.findByIdOrName(id, nameOrId);

            Path imagePath = Paths.get(photo.getFilepath());
            byte[] imageBytes = Files.readAllBytes(imagePath);

            switch (photo.getType()) {
                case "image/png" -> {
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageBytes);
                }
                case "image/jpeg" -> {
                    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageBytes);
                }
                case "image/svg+xml" -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.add(HttpHeaders.CONTENT_TYPE, "image/svg+xml");

                    return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new NotFoundException(e.getMessage());
        }
        return null;
    }

    public ResponseEntity<ApiResponse<PhotoDTO>> update(Long id, MultipartFile file) {
        ApiResponse<PhotoDTO> response = new ApiResponse<>();
        Optional<Photo> byId = photoRepository.findById(id);
        if (byId.isEmpty())
            throw new NotFoundException("Photo not found with id: " + id);

        if (file.getContentType() != null && !(file.getContentType().equals("image/png") ||
                file.getContentType().equals("image/svg+xml") ||
                file.getContentType().equals("image/jpeg"))) {
            throw new IllegalPhotoTypeException("Unsupported image type: " + file.getContentType() + " , Support only image/png or image/svg+xml or image/jpeg");
        }

        try {
            Photo photo = new Photo();
            photo.setId(id);

            String originalFileName = photo.getId() + "-" + Objects.requireNonNull(file.getOriginalFilename()).replaceAll(" ", "%20");

            Path filePath = Paths.get(photoUploadPath + File.separator + originalFileName);
            File uploadDir = new File(filePath.toUri());
            if (!uploadDir.exists())
                uploadDir.mkdirs();

            file.transferTo(uploadDir);

            photo.setName(originalFileName);
            photo.setFilepath(uploadDir.getAbsolutePath());
            photo.setType(file.getContentType());
            photo.setHttpUrl(baseUrl + "/photo/" + photo.getName());

            response.setMessage("Updated");
            response.setData(new PhotoDTO(photoRepository.save(photo)));
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
