package com.github.innovationforge;

import org.springframework.web.multipart.MultipartFile;

public interface BackendApiClient {
    boolean processFile(MultipartFile file, String digitalSignature);
}
