package com.healthproject.controller;

import com.healthproject.Base.BaseResponse;
import com.healthproject.Base.ResultUtils;
import com.healthproject.model.OrderSetting;
import com.healthproject.service.OrderSettingService;
import com.healthproject.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {

    @Resource
    private OrderSettingService orderSettingService;

    /**
     * 查询所有信息
     * @param date
     * @return
     */
    @GetMapping
    public BaseResponse<List<Map<String, String>>> findAll(String date){
        List<Map<String, String>> all = orderSettingService.findAll(date);
        return ResultUtils.success(all);
    }

    /**
     * 读取上传的Excel，解析并写入数据库
     * @param excelFile
     * @return
     */
    @PostMapping("/upload")
    public BaseResponse<Boolean> upload(@RequestParam("excelFile") MultipartFile excelFile) {
        Boolean flag = false;
        try {
            List<String[]> content = POIUtils.readExcel(excelFile);
            if (content.size() > 0) {
                List<OrderSetting> list = new ArrayList<>();
                for (String[] strings : content) {
                    OrderSetting orderSetting = new OrderSetting();
                    orderSetting.setOrderDate(new Date(strings[0]));
                    orderSetting.setNumber(Integer.parseInt(strings[1]));
                    list.add(orderSetting);
                }
                flag = orderSettingService.add(list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (flag) {
            return ResultUtils.success(true);
        } else {
            return ResultUtils.error(false);
        }
    }

    @PutMapping("/{value}")
    public BaseResponse<Boolean> updateNumber(@PathVariable String value, @RequestBody String day){
        try {
            if(orderSettingService.updateNumber(value, day)){
                return ResultUtils.success(true);
            }else{
                return ResultUtils.error(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return ResultUtils.error(false);
        }
    }

}
