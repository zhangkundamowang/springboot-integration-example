package com.baba.thymeleaf.service.impl;

import com.baba.thymeleaf.dao.CarDao;
import com.baba.thymeleaf.entity.Car;
import com.baba.thymeleaf.service.CarService;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    @Autowired
    private CarDao carDao;

    @Override
    public Integer insertCarByExcel(MultipartFile multipartFile) {
        List<Car> carList = new ArrayList<>();
        try {
            // 创建都Excel工作簿文件的引用
            XSSFWorkbook sheets = new XSSFWorkbook(multipartFile.getInputStream());
            // 获取Excel工作表总数
            int numberOfSheets = sheets.getNumberOfSheets();
            for (int i = 0; i < numberOfSheets; i++) {
                XSSFSheet sheet = sheets.getSheetAt(i);
                for (int j = 1; j < sheet.getLastRowNum() + 1; j++) {
                    Car car = new Car();
                    for (int k = 1; k < sheet.getRow(j).getPhysicalNumberOfCells(); k++) {
                        DataFormatter dataFormatter = new DataFormatter();
                        String stringCellValue = dataFormatter.formatCellValue(sheet.getRow(j).getCell(k));
                        switch (k) {
                            case 1:
                                car.setName(stringCellValue);
                                break;
                            case 2:
                                car.setPrice(Integer.parseInt(stringCellValue));
                                break;
                            case 3:
                                car.setColour(stringCellValue);
                                break;
                            case 4:
                                car.setBrand(stringCellValue);
                                break;
                        }
                    }
                    carList.add(car);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return carDao.insertCar(carList);
    }

    @Override
    public HSSFWorkbook exportExcel() {
        // 创建Execl工作薄
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 在Excel工作簿中建一工作表
        HSSFSheet sheet = hssfWorkbook.createSheet("car");
        HSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue(new HSSFRichTextString("主键(id)"));
        row.createCell(1).setCellValue(new HSSFRichTextString("名称(name)"));
        row.createCell(2).setCellValue(new HSSFRichTextString("价格(price)"));
        row.createCell(3).setCellValue(new HSSFRichTextString("颜色(colour)"));
        row.createCell(4).setCellValue(new HSSFRichTextString("品牌(brand)"));
        List<Car> cars = carDao.carList();
        Iterator<Car> iterator = cars.iterator();
        int num = 1;
        while (iterator.hasNext()) {
            Car car = iterator.next();
            HSSFRow rowNum = sheet.createRow(num);
            rowNum.createCell(0).setCellValue(new HSSFRichTextString(car.getId().toString()));
            rowNum.createCell(1).setCellValue(new HSSFRichTextString(car.getName()));
            rowNum.createCell(2).setCellValue(new HSSFRichTextString(car.getPrice().toString()));
            rowNum.createCell(3).setCellValue(new HSSFRichTextString(car.getColour()));
            rowNum.createCell(4).setCellValue(new HSSFRichTextString(car.getBrand()));
            num++;
        }
        return hssfWorkbook;
    }
}
