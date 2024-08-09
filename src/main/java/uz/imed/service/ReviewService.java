package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.Product;
import uz.imed.entity.Review;
import uz.imed.entity.ReviewOption;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.ReviewDTO;
import uz.imed.repository.ProductRepository;
import uz.imed.repository.ReviewOptionRepository;
import uz.imed.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

@Service
public class ReviewService
{
    private final ReviewRepository reviewRepo;
    private final ObjectMapper objectMapper;
    private final ProductRepository productRepository;
    private final PhotoService photoService;
    private final ReviewOptionRepository reviewOptionRepo;

    public ResponseEntity<ApiResponse<Review>> add(String json, Long productId, MultipartFile docPhoto)
    {
        try
        {
            Review review = objectMapper.readValue(json, Review.class);
            Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found by id: " + productId));
            review.getOptions().forEach(i -> i.setReview(review));
            review.setProduct(product);
            review.setDoctorPhoto(photoService.save(docPhoto));

            ApiResponse<Review> response = new ApiResponse<>();
            response.setMessage("Review added");
            response.setData(reviewRepo.save(review));
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e)
        {
            throw new NotFoundException("Error while parsing json: " + e.getMessage());
        }
    }

    public ResponseEntity<ApiResponse<Review>> update(Review newReview)
    {
        Review dbReview = reviewRepo.findById(newReview.getId()).orElseThrow(() -> new RuntimeException("Review not found by id: " + newReview.getId()));

        boolean leastOneGiven = false;
        //--------------Name of doctor--------------
        if (newReview.getNameDoctorUz() != null)
        {
            dbReview.setNameDoctorUz(newReview.getNameDoctorUz());
            leastOneGiven = true;
        }

        if (newReview.getNameDoctorRu() != null)
        {
            dbReview.setNameDoctorRu(newReview.getNameDoctorRu());
            leastOneGiven = true;
        }

        if (newReview.getNameDoctorEn() != null)
        {
            dbReview.setNameDoctorEn(newReview.getNameDoctorEn());
            leastOneGiven = true;
        }

        //--------------Position doctor--------------
        if (newReview.getPositionUz() != null)
        {
            dbReview.setPositionUz(newReview.getPositionUz());
            leastOneGiven = true;
        }

        if (newReview.getPositionRu() != null)
        {
            dbReview.setPositionRu(newReview.getPositionRu());
            leastOneGiven = true;
        }
        if (newReview.getPositionEn() != null)
        {
            dbReview.setPositionEn(newReview.getPositionEn());
            leastOneGiven = true;
        }

        //--------------Review Options--------------
        if (newReview.getOptions() != null && !newReview.getOptions().isEmpty())
        {
            updateReviewOptions(dbReview, newReview.getOptions());
            leastOneGiven = true;
        }

        //if not given the least one parameter delete it
        if (!leastOneGiven)
        {
            reviewRepo.deleteById(newReview.getId());
        }

        ApiResponse<Review> response = new ApiResponse<>();
        response.setData(reviewRepo.save(dbReview));
        response.setMessage("Review updated");
        return ResponseEntity.ok(response);
    }

    //--------------Review--------------

    private void updateReviewOptions(Review dbReview, List<ReviewOption> newOptions)
    {
        List<ReviewOption> dbOptions = dbReview.getOptions();
        if (dbOptions == null)
            dbOptions = new ArrayList<>();

        for (ReviewOption newOption : newOptions)
        {
            if (newOption.getId() != null)
            {

                for (ReviewOption dbOption : dbOptions)
                {
                    if (dbOption.getId().equals(newOption.getId()))
                    {
                        boolean leastOneGiven = false;

                        //--------------Title--------------
                        if (newOption.getTitleUz() != null)
                        {
                            dbOption.setTitleUz(newOption.getTitleUz());
                            leastOneGiven = true;
                        }

                        if (newOption.getTitleRu() != null)
                        {
                            dbOption.setTitleRu(newOption.getTitleRu());
                            leastOneGiven = true;
                        }

                        if (newOption.getTitleEn() != null)
                        {
                            dbOption.setTitleEn(newOption.getTitleEn());
                            leastOneGiven = true;
                        }

                        //--------------Value--------------
                        if (newOption.getValueUz() != null)
                        {
                            dbOption.setValueUz(newOption.getValueUz());
                            leastOneGiven = true;
                        }

                        if (newOption.getValueRu() != null)
                        {
                            dbOption.setValueRu(newOption.getValueRu());
                            leastOneGiven = true;
                        }

                        if (newOption.getValueEn() != null)
                        {
                            dbOption.setValueEn(newOption.getValueEn());
                            leastOneGiven = true;
                        }
                        //if not given the least one delete it
                        if (!leastOneGiven)
                        {
                            System.err.println("DELETE OPTION " + newOption.getId());
//                            dbReview.getOptions().removeIf(p -> p.getId().equals(newOption.getId()));
                            reviewOptionRepo.deleteByOptionId(newOption.getId());
                        }

                    }

                }

            }
            //if id not given save it
            else
            {
                newOption.setReview(dbReview);
                dbReview.getOptions().add(newOption);
//                reviewOptionRepo.save(newOption);
            }
        }
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        if (!reviewRepo.existsById(id))
            throw new NotFoundException("Review not found by id: " + id);

        reviewRepo.deleteById(id);
        ApiResponse<Review> response = new ApiResponse<>();
        response.setMessage("Review deleted");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> get(String lang, Long productId, int page, int size)
    {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<Review> all = reviewRepo.findAllByProductId(productId, pageable);
        if (lang != null)
        {
            ApiResponse<List<ReviewDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            all.forEach(i -> response.getData().add(new ReviewDTO(i, lang)));
            response.setMessage("Found " + all.size() + " review(s)");
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<Review>> response = new ApiResponse<>();
        response.setData(all);
        response.setMessage("All languages " + all.size() + " review(s)");
        return ResponseEntity.ok(response);
    }
}
