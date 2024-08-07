package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.PhotoDTO;
import uz.imed.service.PhotoService;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController
{

    private final PhotoService photoService;

    @GetMapping("/{name-or-id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "name-or-id") String nameOrId)
    {
        return photoService.findByNameOrId(nameOrId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PhotoDTO>> updatePhoto(
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "new-photo") MultipartFile newPhoto)
    {
        return photoService.update(id, newPhoto);
    }
}
