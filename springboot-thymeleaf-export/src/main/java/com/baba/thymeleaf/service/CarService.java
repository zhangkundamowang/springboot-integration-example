package com.baba.thymeleaf.service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public interface CarService {

    Integer insertCarByExcel(MultipartFile multipartFile);

    HSSFWorkbook exportExcel();
}
