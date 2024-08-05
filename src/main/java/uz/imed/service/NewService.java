package uz.imed.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uz.imed.entity.New;
import uz.imed.entity.NewOption;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.payload.NewDTO;
import uz.imed.repository.NewOptionRepository;
import uz.imed.repository.NewRepository;
import uz.imed.util.SlugUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
public class NewService {

    private final NewRepository newRepository;

    private final NewOptionRepository newOptionRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<New>> create(String json, MultipartHttpServletRequest request) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<Integer> maxOrderNum = newRepository.getMaxOrderNum();
        try {
            New newness = objectMapper.readValue(json, New.class);
            newness.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
            newness.setActive(true);
            Iterator<String> fileNames = request.getFileNames();
            while (fileNames.hasNext()) {
                String key = fileNames.next();
                MultipartFile photo = request.getFile(key);
                setNewsPhoto(key, photo, newness);
            }

            newness.getNewOptions().sort(Comparator.comparing(NewOption::getOrderNum));
            New saved = newRepository.save(newness);
            String slug = saved.getId() + "-" + SlugUtil.makeSlug(saved.getHead().getHeadingUz());
            newRepository.updateSlug(slug, saved.getId());
            saved.setSlug(slug);
            response.setData(saved);
            return ResponseEntity.ok(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    private void setNewsPhoto(String key, MultipartFile photo, New newness) {
        if (key.equalsIgnoreCase("main-photo")) {
            newness.getHead().setPhoto(photoService.save(photo));
            return;
        }
        int index = Integer.parseInt(key.substring(12)) - 1;
        NewOption newOption = newness.getNewOptions().get(index);
        newOption.setPhoto(photoService.save(photo));
    }

    public ResponseEntity<ApiResponse<?>> findBySlug(String slug, String lang) {
        New newness = newRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("New is not found by slug: " + slug));
        if (lang != null) {
            ApiResponse<NewDTO> response = new ApiResponse<>();
            response.setData(new NewDTO(newness, lang));
            return ResponseEntity.ok(response);
        }
        ApiResponse<New> response = new ApiResponse<>();
        response.setData(newness);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> findAllByPageNation(String lang, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<New> all = newRepository.findAll(pageable);
        if (lang != null) {
            ApiResponse<List<NewDTO>> response = new ApiResponse<>();
            response.setData(new ArrayList<>());
            all.forEach(newness -> response.getData().add(new NewDTO(newness, lang)));
            return ResponseEntity.ok(response);
        }
        ApiResponse<List<New>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        all.forEach(newness -> response.getData().add(newness));
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<New>> update(New newness) {
        ApiResponse<New> response = new ApiResponse<>();
        New fromDB = newRepository.findById(newness.getId())
                .orElseThrow(() -> new NotFoundException("Newness is not found by id: " + newness.getId()));

        if (newness.getHead() != null) {
            NewOption fromDBHead = fromDB.getHead();
            NewOption newHead = newness.getHead();

            if (newHead.getHeadingEn() != null) {
                fromDBHead.setHeadingEn(newHead.getHeadingEn());
            }
            if (newHead.getHeadingRu() != null) {
                fromDBHead.setHeadingRu(newHead.getHeadingRu());
            }
            if (newHead.getHeadingUz() != null) {
                fromDBHead.setHeadingUz(newHead.getHeadingUz());
                String slug = fromDB.getId() + "-" + SlugUtil.makeSlug(newHead.getHeadingUz());
                fromDB.setSlug(slug);
            }

            if (newHead.getTextEn() != null) {
                fromDBHead.setTextEn(newHead.getTextEn());
            }
            if (newHead.getTextRu() != null) {
                fromDBHead.setTextRu(newHead.getTextRu());
            }
            if (newHead.getTextUz() != null) {
                fromDBHead.setTextUz(newHead.getTextUz());
            }

        }

        if (newness.getNewOptions() != null) {
            List<NewOption> fromDBOptions = fromDB.getNewOptions();
            List<NewOption> newOptions = newness.getNewOptions();

            for (NewOption newOption : newOptions) {
                for (NewOption fromDBOption : fromDBOptions) {
                    if (newOption.getId() != null && newOption.getId().equals(fromDBOption.getId())) {

                        if (newOption.getHeadingUz() != null) {
                            fromDBOption.setHeadingUz(newOption.getHeadingUz());
                        }
                        if (newOption.getHeadingEn() != null) {
                            fromDBOption.setHeadingEn(newOption.getHeadingEn());
                        }
                        if (newOption.getHeadingUz() != null) {
                            fromDBOption.setHeadingUz(newOption.getHeadingUz());
                        }

                        if (newOption.getTextUz() != null) {
                            fromDBOption.setTextUz(newOption.getTextUz());
                        }
                        if (newOption.getTextRu() != null) {
                            fromDBOption.setTextRu(newOption.getTextRu());
                        }
                        if (newOption.getTextEn() != null) {
                            fromDBOption.setTextEn(newOption.getTextEn());
                        }

                        if (newOption.getHeadingUz() == null && newOption.getHeadingRu() == null && newOption.getHeadingEn() == null
                                && newOption.getTextUz() == null && newOption.getTextRu() == null && newOption.getTextEn() == null) {
                            newOptionRepository.deleteById(newOption.getId());
                        }

                    }
                }

                if (newOption.getId() == null) {
                    newOption.setNewness(fromDB);
                    fromDBOptions.add(newOption);
                }
            }
            fromDBOptions.sort(Comparator.comparing(NewOption::getOrderNum));
        }
        response.setData(newRepository.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (!newRepository.existsById(id)) {
            throw new NotFoundException("Newness is not found by id: " + id);
        }
        newRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.ok(response);
    }

}
