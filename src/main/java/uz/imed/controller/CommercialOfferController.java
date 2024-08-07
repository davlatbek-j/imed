package uz.imed.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import uz.imed.entity.CommercialOffer;
import uz.imed.payload.ApiResponse;
import uz.imed.service.CommercialOfferService;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/v1/commercial-offer")
public class CommercialOfferController
{

    private final CommercialOfferService offerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommercialOffer>> addCommercialOffer(
            @RequestBody CommercialOffer commercialOffer)
    {
        return offerService.add(commercialOffer);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommercialOffer>>> getAll()
    {
        return offerService.getAll();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> deleteCommercialOffer(
            @PathVariable Long id)
    {
        return offerService.delete(id);
    }
}
