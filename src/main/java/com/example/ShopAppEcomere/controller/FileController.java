package com.example.ShopAppEcomere.controller;

import com.example.ShopAppEcomere.dto.response.ApiResponse;
import com.example.ShopAppEcomere.dto.response.FileResponse;
import com.example.ShopAppEcomere.exception.StorageException;
import com.example.ShopAppEcomere.service.FileService;
import com.example.ShopAppEcomere.validator.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    @Value("${huyhoang.upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;

    @PostMapping("/upload")
    @ApiMessage("Upload a single file")
    public ApiResponse<FileResponse> uploadFile(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder) throws IOException, StorageException, URISyntaxException {

        // Validate file
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload a valid file.");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(ext -> fileName.toLowerCase().endsWith(ext));

        if (!isValid) {
            throw new StorageException("Invalid file extension. Only allows: " + allowedExtensions);
        }

        // Create directory if not exists and store file
        fileService.createDirectory(baseURI + folder);
        String uploadedFilePath = fileService.store(file, folder);

        // Prepare response
        FileResponse fileResponse = new FileResponse(uploadedFilePath, Instant.now());
        return ApiResponse.<FileResponse>builder()
                .code(200)
                .message("File uploaded successfully.")
                .result(fileResponse)
                .build();
    }

//    @GetMapping("/download")
//    @ApiMessage("Download a file")
//    public ApiResponse<Resource> downloadFile(
//            @RequestParam("fileName") String fileName,
//            @RequestParam("folder") String folder) throws IOException, StorageException, URISyntaxException {
//
//        // Validate parameters
//        if (fileName == null || folder == null) {
//            throw new StorageException("Missing required parameters: fileName or folder.");
//        }
//
//        // Get file resource
//        Resource fileResource = fileService.getResource(fileName, folder);
//        if (!fileResource.exists()) {
//            throw new StorageException("File with name = " + fileName + " not found.");
//        }
//
//        // Prepare response
//        return ApiResponse.<Resource>builder()
//                .code(200)
//                .message("File downloaded successfully.")
//                .result(fileResource)
//                .build();
//    }
    @GetMapping("/download")
    @ApiMessage("Download a file")
    public ApiResponse<String> download(
            @RequestParam(name = "fileName", required = false) String fileName,
            @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, URISyntaxException, FileNotFoundException {
        if (fileName == null || folder == null) {
            throw new StorageException("Missing required params: fileName or folder.");
        }

        // Check if the file exists
        long fileLength = this.fileService.getFileLength(fileName, folder);
        if (fileLength == 0) {
            throw new StorageException("File not found: " + fileName);
        }

        // Return the file's download URL or a message
        String fileUrl = "/api/v1/files/download?fileName=" + fileName + "&folder=" + folder;
        return new ApiResponse<>(2000, "File is ready for download", fileUrl);
    }


}
