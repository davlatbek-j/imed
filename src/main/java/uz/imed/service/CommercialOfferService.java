package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.CommercialOffer;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.CommercialOfferRepository;

import java.util.List;


@RequiredArgsConstructor

@Service
public class CommercialOfferService
{
    private final CommercialOfferRepository offerRepository;

    public ResponseEntity<ApiResponse<CommercialOffer>> add(CommercialOffer commercialOffer)
    {
        ApiResponse<CommercialOffer> response = new ApiResponse<>();

        commercialOffer.getProductLink().forEach(i -> i.setCommercialOffer(commercialOffer));
        response.setData(offerRepository.save(commercialOffer));
        response.setMessage("Added");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<List<CommercialOffer>>> getAll()
    {
        List<CommercialOffer> all = offerRepository.findAll();
        ApiResponse<List<CommercialOffer>> response = new ApiResponse<>();
        response.setData(all);
        response.setMessage("All Commercial Offers");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (!offerRepository.existsById(id))
        {
            response.setMessage("Not Found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        offerRepository.deleteById(id);
        response.setMessage("Deleted");
        return ResponseEntity.ok(response);
    }
}
