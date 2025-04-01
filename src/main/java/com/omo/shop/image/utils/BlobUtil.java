package com.omo.shop.image.utils;

import java.io.InputStream;
import java.sql.Blob;
import java.util.Base64;

public class BlobUtil {

    public static String convertBlobToBase64(Blob blob) {
        if (blob == null) return null;

        try (InputStream inputStream = blob.getBinaryStream()) {
            byte[] bytes = inputStream.readAllBytes(); // Read all bytes
            return Base64.getEncoder().encodeToString(bytes); // Convert to Base64
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert Blob to Base64", e);
        }
    }
}
