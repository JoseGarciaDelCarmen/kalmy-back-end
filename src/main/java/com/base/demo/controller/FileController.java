package com.base.demo.controller;

import com.base.demo.model.CustomApiResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

  private final Storage storage;
  private final String bucket;

  public FileController(Storage storage, @Value("${app.storage.bucket}") String bucket) {
    this.storage = storage;
    this.bucket = bucket;
  }

  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public CustomApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
    String objectName = "uploads/" + UUID.randomUUID() + "-" + sanitize(file.getOriginalFilename());
    BlobInfo info = BlobInfo.newBuilder(BlobId.of(bucket, objectName))
                            .setContentType(file.getContentType())
                            .build();
    storage.create(info, file.getBytes());
    return new CustomApiResponse<>("uploaded", Map.of("bucket", bucket, "object", objectName));
  }

  @GetMapping("/{*objectName}")
  public ResponseEntity<byte[]> download(@PathVariable("objectName") String objectName) {
    Blob blob = storage.get(BlobId.of(bucket, objectName));
    if (blob == null || !blob.exists()) {
      return ResponseEntity.notFound().build();
    }
    String filename = objectName.substring(objectName.lastIndexOf('/') + 1);
    return ResponseEntity.ok()
                         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" +
                             URLEncoder.encode(filename, StandardCharsets.UTF_8))
                         .contentType(MediaType.parseMediaType(blob.getContentType() == null ? "application/octet-stream" : blob.getContentType()))
                         .body(blob.getContent());
  }

  private static String sanitize(String name) {
    if (name == null) return "file.bin";
    return name.replaceAll("[\\r\\n\\\\\"]", "_");
  }
}
