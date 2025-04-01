package com.omo.shop.image.controller;

import com.omo.shop.common.response.ApiResponse;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.image.dto.ImageDto;
import com.omo.shop.image.model.Image;
import com.omo.shop.image.service.IImageService;
import com.omo.shop.image.utils.BlobUtil;
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
            List<ImageDto> imageDtos = imageService.saveImages(files, productId);
            return ResponseEntity.ok(new ApiResponse("successfully uploaded", imageDtos));
        } catch (Exception e) {
            return ResponseEntity
                    .status(INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Upload failed", e.getMessage()));
        }
    }

    @GetMapping("/download/{imageId}")
    public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
        Image imageEntity = imageService.getImageEntityById(imageId);

        String base64Image = BlobUtil.convertBlobToBase64(imageEntity.getImage());
        byte[] imageBytes = imageService.decodeBase64ToBytes(base64Image);

        ByteArrayResource resource = new ByteArrayResource(imageBytes);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageEntity.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + imageEntity.getFileName() + "\""
                )
                .body(resource);
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ApiResponse> getImageDetailsById(@PathVariable Long imageId) {
        try {
            ImageDto imageDto = imageService.getImageById(imageId);

            return ResponseEntity.ok(new ApiResponse("Success!", imageDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
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
