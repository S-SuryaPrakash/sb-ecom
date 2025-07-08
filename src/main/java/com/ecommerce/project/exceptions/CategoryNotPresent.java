package com.ecommerce.project.exceptions;

public class CategoryNotPresent extends RuntimeException {
    public CategoryNotPresent() {
    }

    public CategoryNotPresent(String message) {
        super(message);
    }
}
