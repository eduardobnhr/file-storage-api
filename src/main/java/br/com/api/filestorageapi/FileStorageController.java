package br.com.api.filestorageapi;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.micrometer.common.util.StringUtils;

@Controller
@RequestMapping("/api/files")
public class FileStorageController {
    private final Path fileStorageLocation;


    public FileStorageController(FileStoragePropeties fileStoragePropeties){
        this.fileStorageLocation = Paths.get(fileStoragePropeties.getUploadDir())
        .toAbsolutePath().normalize();
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try{
            Path targetLocation = fileStorageLocation.resolve(fileName);
            file.transferTo(targetLocation);

            String fileDowloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/files/dowload/")
            .path(fileName)
            .toUriString();

            return ResponseEntity.ok("upload completed! Dowload link:" + fileDowloadUri);

        } catch (IOException ex){
       

        }
    }
    
}
