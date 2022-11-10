package com.marcin.springboot_react.exception;

public class ProductPrinterException extends RuntimeException {

    public ProductPrinterException(String message) {
        super(message);
    }

    public ProductPrinterException(String message, Throwable error) {
        super(message, error);
    }
}
