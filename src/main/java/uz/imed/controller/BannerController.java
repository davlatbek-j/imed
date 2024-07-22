package uz.imed.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Banner;
import uz.imed.entity.BannerWrapper;
import uz.imed.payload.ApiResponse;
import uz.imed.service.BannerService;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController
{

    private final BannerService bannerService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<BannerWrapper>> createBanner(
            @RequestParam(value = "link") String link,
            @RequestParam(value = "active") Boolean active,
            @RequestParam(value = "photo") MultipartFile gallery)
    {
        return bannerService.addSlider(link, active, gallery);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<BannerWrapper>> get()
    {
        return bannerService.get();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<BannerWrapper>> update(
            @RequestBody Banner banner)
    {
        return bannerService.update(banner);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete()
    {
        return bannerService.delete();
    }


}
