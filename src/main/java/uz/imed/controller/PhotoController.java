package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import uz.imed.service.PhotoService;


@RequiredArgsConstructor

@Controller
@RequestMapping("/photo")
public class PhotoController
{
    private final PhotoService photoService;

    @GetMapping("/{nameOrId}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "nameOrId") String nameOrId) {
        return photoService.findByNameOrId(nameOrId);
    }

}
