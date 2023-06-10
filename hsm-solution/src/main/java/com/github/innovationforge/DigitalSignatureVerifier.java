package com.github.innovationforge;

public interface DigitalSignatureVerifier {
    boolean verifyDigitalSignature(String fileHash, String digitalSignature);
}
