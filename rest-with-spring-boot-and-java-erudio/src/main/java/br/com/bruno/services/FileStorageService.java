package br.com.bruno.services;

import br.com.bruno.config.FileStorageConfig;
import br.com.bruno.exceptions.FileStorageException;
import br.com.bruno.exceptions.MyFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    @Autowired
    public FileStorageService(
            FileStorageConfig fileStorageConfig
    ) {
        this.fileStorageLocation = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath()
                .normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception e) {
            throw new FileStorageException("Could not create directory where uploaded files will be stored!", e);
        }
    }

    public String storeFile(MultipartFile file) throws FileStorageException {

        String originalFileName = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new FileStorageException("Sorry! Filename is not present"));
        String filename = StringUtils.cleanPath(originalFileName);

        try {
            if(filename.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + filename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return filename;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + filename + ". Please try again!", e);
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if(resource.exists()) {
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found");
            }
        } catch (Exception e) {
            throw new MyFileNotFoundException("File not found " + filename, e);
        }
    }
}
