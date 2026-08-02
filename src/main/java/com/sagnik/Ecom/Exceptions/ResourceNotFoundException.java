package com.sagnik.Ecom.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String field;
    Long fieldId;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String field) {
        super(String.format("%s not found with %s: %s",resourceName,fieldName, field));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }

    public ResourceNotFoundException(Long fieldId, String fieldName, String resourceName) {
        super(String.format("%s not found with %s: %d",resourceName,fieldName, fieldId));
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }
}
