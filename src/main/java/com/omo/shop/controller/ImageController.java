package com.omo.shop.controller;

import com.omo.shop.dto.ImageDto;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.response.ApiResponse;
import com.omo.shop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("${api.prefix}/images")
@RequiredArgsConstructor
public class ImageController {
    private final IImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImages(
            @RequestParam List<MultipartFile> files,
            @RequestParam Long productId) {
        try {
            List<ImageDto> imageDtos = imageService.saveImage(files, productId);
            return ResponseEntity.ok(new ApiResponse("successfully uploaded", imageDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed", e.getMessage()));
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        ImageDto imageDto = imageService.getImageById(imageId);

        byte[] imageBytes = imageService.decodeBase64ToBytes(imageDto.getImageBase64());

        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + imageDto.getFileName() + "\""
                )
                .body(resource);
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId, @RequestBody MultipartFile file) {
        try {
            ImageDto image = imageService.getImageById(imageId);
            if (image != null) {
                imageService.updateImage(file, imageId);
                return ResponseEntity.ok(new ApiResponse("Update success!", image));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("updated failed", INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
        try {
            ImageDto imageDto = imageService.getImageById(imageId);
            if (imageDto != null) {
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("deleted successfully!", imageDto));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("deletion failed", INTERNAL_SERVER_ERROR));
    }
}
