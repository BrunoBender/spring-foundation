package br.com.bruno.controllers;

import br.com.bruno.data.dto.UploadFileResponseDto;
import br.com.bruno.services.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Tag(name = "File", description = "Endpoint for managing download and upload files")
@RestController
@RequestMapping("api/file")
public class FileController {

    private final Logger logger = Logger.getLogger(FileController.class.getName());

    @Autowired
    private FileStorageService service;

    @Operation(
            summary = "Upload file",
            description = "Upload a single file",
            tags = "File"
    )
    @PostMapping("uploadFile")
    public UploadFileResponseDto uploadFile(
            @RequestParam("file") MultipartFile file
    ) {
        logger.info("Storing file to disk");

        var fileName = service.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/file/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponseDto(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @Operation(
            summary = "Upload multiple files",
            description = "Upload multiple files",
            tags = "File"
    )
    @PostMapping("uploadMultipleFiles")
    public List<UploadFileResponseDto> uploadMultipleFiles(
            @RequestParam("files") MultipartFile[] files
    ) {
        logger.info("Storing files to disk");

        return Arrays.stream(files)
                .map(this::uploadFile)
                .collect(Collectors.toList());
    }

    @Operation(
            summary = "Download file",
            description = "Download file",
            tags = "File"
    )
    //Ex: My_file.txt
    @GetMapping("downloadFile/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable("filename") String filename,
            HttpServletRequest request
    ) {

        logger.info("Reading a file on disk");

        Resource resource = service.loadFileAsResource(filename);
        String contentType = "application/octet-stream"; //Define um contentType genérico por padrão

        try {
            contentType = request
                    .getServletContext()
                    .getMimeType(
                            resource.getFile().getAbsolutePath()
                    );
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not determine file type!", e);
        }


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\""
                )
                .body(resource);
    }
}
