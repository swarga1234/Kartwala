package com.swarga.Kartwala.exception;

public class FileNotUploadedException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public FileNotUploadedException() {
    }

    public FileNotUploadedException(String message) {
        super(message);
    }

    public FileNotUploadedException(String message, Throwable cause) {
        super(message, cause);
    }
}
