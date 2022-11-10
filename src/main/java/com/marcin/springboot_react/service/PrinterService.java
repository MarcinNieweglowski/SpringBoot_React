package com.marcin.springboot_react.service;

import com.marcin.springboot_react.model.Product;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface PrinterService {

    File download(List<Product> productList) throws IOException;
}
