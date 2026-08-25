package com.sagnik.Ecom.Exceptions;

public class ResourceNotFoundException extends RuntimeException {
    String resourceName;
    String fieldName;
    String field;
    Long fieldId;

    /** Creates a resource-not-found exception without details. */
    public ResourceNotFoundException() {
    }

    /** Creates an exception describing a missing resource field value. */
    public ResourceNotFoundException(String resourceName, String fieldName, String field) {
        super(String.format("%s not found with %s: %s",fieldName,resourceName, field));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.field = field;
    }

    /** Creates an exception describing a missing resource identified by ID. */
    public ResourceNotFoundException(Long fieldId, String fieldName, String resourceName) {
        super(String.format("%s not found with %s: %d",fieldName,resourceName, fieldId));
        this.fieldId = fieldId;
        this.fieldName = fieldName;
        this.resourceName = resourceName;
    }
}
