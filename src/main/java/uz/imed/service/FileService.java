package uz.imed.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.imed.entity.MyFile;
import uz.imed.entity.Product;
import uz.imed.exception.FileSavingException;
import uz.imed.exception.NotFoundException;
import uz.imed.payload.ApiResponse;
import uz.imed.repository.MyFileRepository;
import uz.imed.repository.ProductRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor

@Service
public class FileService
{
    private final ProductRepository productRepo;
    @Value("${file.upload.path}")
    private String filePath;

    @Value("${server.base-url}")
    private String baseUrl;


    private final MyFileRepository fileRepo;

    public ResponseEntity<ApiResponse<?>> upload(Long productId, List<MultipartFile> files)
    {
        Product product = productRepo.findById(productId).orElseThrow(() -> new NotFoundException("Product not found by id: " + productId));

        if (product.getFiles() == null)
            product.setFiles(new ArrayList<>());

        if (files != null && !files.isEmpty())
            files.forEach(file -> product.getFiles().add(save(product, file)));

        productRepo.save(product);

        ApiResponse<?> response = new ApiResponse<>();
        response.setMessage("Upload " + product.getFiles().size() + " file(s)");
        return ResponseEntity.ok(response);
    }

    public MyFile save(Product product, MultipartFile file)
    {
        try
        {
            if (file == null || file.isEmpty() || file.getOriginalFilename() == null)
                throw new FileSavingException("File is null or empty");

            MyFile save = fileRepo.save(new MyFile());
            String originalFileName = save.getId() + "-" + file.getOriginalFilename().replaceAll(" ", "%20");
//            String originalFileName = save.getId() + "-" + Objects.requireNonNull(file.getOriginalFilename());

            Path filePath = Paths.get(this.filePath + File.separator + originalFileName);
            File uploadDir = new File(filePath.toUri());
            if (!uploadDir.exists())
            {
                uploadDir.mkdirs();
            }
            file.transferTo(uploadDir);

            save.setName(originalFileName);
            save.setFilePath(uploadDir.getAbsolutePath());
            save.setType(file.getContentType());
            save.setSize(String.format("%.2f", (double) file.getSize() / 1_000_000) + " Mb");
            save.setDownloadLink(baseUrl + "/v1/product/file/" + save.getName());
            save.setProduct(product);

            return fileRepo.save(save);
        } catch (IOException e)
        {
            throw new FileSavingException("File not saved: " + e.getMessage());
        }
    }

    public ResponseEntity<Resource> download(String fileName)
    {
        MyFile myFile = fileRepo.findByName(fileName.replaceAll(" ", "%20")).orElseThrow(() -> new NotFoundException("File not found by name: " + fileName));

        Path path = Paths.get(myFile.getFilePath()).normalize();
        File file = path.toFile();

        if (!file.exists())
            throw new NotFoundException("File not found: " + fileName);

        Resource resource = new FileSystemResource(file);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);

    }

    public void delete(Long id)
    {
        MyFile myFile = fileRepo.findById(id).orElseThrow(() -> new NotFoundException("File not found by id: " + id));

        Path filePath = Paths.get(myFile.getFilePath());
        try
        {
            Files.delete(filePath);
        } catch (IOException e)
        {
            throw new NotFoundException("File not deleted: " + e.getMessage());
        }
        fileRepo.delete(id);

    }
}
