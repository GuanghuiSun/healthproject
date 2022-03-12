package com.healthproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.healthproject.model.OrderSetting;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface OrderSettingService extends IService<OrderSetting> {
    Boolean add(List<OrderSetting> list);

    List<Map<String,String>> findAll(String date);

    Boolean updateNumber(String value, String day) throws ParseException;
}
