package org.test.between.msproductpricing.application.exception;

public class RecordNotFoundException extends RuntimeException {

    public RecordNotFoundException(String statusText) {
        super(statusText);
    }
}
