package com.pharmacyInventory.inventory.config;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {
    
    @Override
    public Exception decode(String methodKey, Response response) {
        return switch (response.status()) {
            case 400 -> new RuntimeException("Bad Request");
            case 404 -> new RuntimeException("Resource not found");
            case 401 -> new RuntimeException("Unauthorized");
            case 403 -> new RuntimeException("Forbidden");
            case 500 -> new RuntimeException("Internal Server Error");
            default -> new RuntimeException("Error occurred while processing request");
        };
    }
}
