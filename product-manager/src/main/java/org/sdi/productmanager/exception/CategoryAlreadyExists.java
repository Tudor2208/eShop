package org.sdi.productmanager.exception;

public class CategoryAlreadyExists extends RuntimeException {

    public CategoryAlreadyExists(String message) {
        super(message);
    }
}
