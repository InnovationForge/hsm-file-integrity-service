package com.github.innovationforge;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClientController {

    private HsmApiClient hsmApiClient; // Inject the HSM API client
    private BackendApiClient backendApiClient; // Inject the Backend API client

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate file hash
            String fileHash = generateFileHash(file);

            // Call HSM API to get digital signature
            String digitalSignature = hsmApiClient.generateDigitalSignature(fileHash);

            // Send file and digital signature to backend for further processing
            boolean isSignatureValid = backendApiClient.processFile(file, digitalSignature);

            if (isSignatureValid) {
                return ResponseEntity.ok("File uploaded and processed successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file signature.");
            }
        } catch (Exception e) {
            // Handle exception and provide appropriate response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred during file upload.");
        }
    }

    // Other endpoints for file retrieval and download
    // ...

    private String generateFileHash(MultipartFile file) throws NoSuchAlgorithmException, IOException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] fileBytes = file.getBytes();
        byte[] fileHashBytes = md.digest(fileBytes);
        return Hex.encodeHexString(fileHashBytes);
    }
}
