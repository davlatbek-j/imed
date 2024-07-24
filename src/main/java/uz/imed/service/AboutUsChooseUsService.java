package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.imed.entity.AboutUsChooseUs;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.AboutUsChooseUsRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsChooseUsService {


    private final AboutUsChooseUsRepository aboutUsChooseUsRepository;

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> create(AboutUsChooseUs aboutUsChooseUs) {
        ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
        AboutUsChooseUs save = aboutUsChooseUsRepository.save(aboutUsChooseUs);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> findById(Long id) {
        ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
        Optional<AboutUsChooseUs> optionalAboutUsChooseUs = aboutUsChooseUsRepository.findById(id);
        if (optionalAboutUsChooseUs.isEmpty()) {
            response.setMessage("AboutUsChooseUs is not found by id");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsChooseUs aboutUsChooseUs = optionalAboutUsChooseUs.get();
        response.setData(aboutUsChooseUs);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<AboutUsChooseUs>>> findAll() {
        ApiResponse<List<AboutUsChooseUs>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<AboutUsChooseUs> all = aboutUsChooseUsRepository.findAll();
        all.forEach(aboutUsChooseUs -> response.getData().add(aboutUsChooseUs));
        response.setMessage("Found " + all.size() + " AboutUsChooseUs");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsChooseUs>> update(Long id, AboutUsChooseUs aboutUsChooseUs) {
        ApiResponse<AboutUsChooseUs> response = new ApiResponse<>();
        Optional<AboutUsChooseUs> optionalAboutUsChooseUs = aboutUsChooseUsRepository.findById(id);
        if (optionalAboutUsChooseUs.isEmpty()) {
            response.setMessage("AboutUsChooseUs is not found by id");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsChooseUs oldAboutUsChooseUs = optionalAboutUsChooseUs.get();
        oldAboutUsChooseUs.setName(aboutUsChooseUs.getName());
        oldAboutUsChooseUs.setDescription(aboutUsChooseUs.getDescription());
        AboutUsChooseUs save = aboutUsChooseUsRepository.save(oldAboutUsChooseUs);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsChooseUsRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsChooseUs is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsChooseUsRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
