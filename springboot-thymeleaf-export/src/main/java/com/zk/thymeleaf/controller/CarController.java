package com.zk.thymeleaf.controller;

import com.zk.thymeleaf.service.CarService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 导入导出功能参考博客
 * https://blog.csdn.net/y1534414425/article/details/106665202/
 * https://blog.csdn.net/xinbumi/article/details/82700929
 * https://blog.csdn.net/zahngjialiang/article/details/78395045
 * 这个项目中excel版本需要是2007+版本，该程序在office 2016上测试通过，导出的excel文件后缀是.xls，导入的excel文件后缀是.xlsx
 */
@Controller
@RequestMapping(value = "springboot")
public class CarController {
    @Autowired
    private CarService carService;

    @RequestMapping("/index")
    public String carList() {
        return "car";
    }

    // 将excel导入到数据库
    @RequestMapping("/insertCarByExcel")
    public String insertCarByExcel(@RequestParam("file") MultipartFile multipartFile, ModelMap map) {
        Integer integer = carService.insertCarByExcel(multipartFile);
        if (integer > 0) {
            map.addAttribute("msg", "通过Excel插入成功！");
            return "success";
        }
        map.addAttribute("msg", "通过Excel插入失败！");
        return "success";
    }

    // 将数据库导出成excel
    @RequestMapping("/exportCarByExcel")
    public void exportCarByExcel(HttpServletResponse response) {
        HSSFWorkbook workbook = carService.exportExcel();
        // 获取输出流
        OutputStream os = null;
        try {
            // 获取输出流
            os = response.getOutputStream();
            // 重置输出流
            response.reset();
            // 设定输出文件头
            response.setHeader("Content-disposition",
                    "attachment; filename=" + new String("car".getBytes("GB2312"), "8859_1") + ".xls");
            // 定义输出类型
            response.setContentType("application/msexcel");
            workbook.write(os);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                assert os != null;
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
