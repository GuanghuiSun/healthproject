package com.healthproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healthproject.mapper.OrderSettingMapper;
import com.healthproject.model.OrderSetting;
import com.healthproject.service.OrderSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class OrderSettingServiceImpl extends ServiceImpl<OrderSettingMapper, OrderSetting> implements OrderSettingService {

    @Resource
    private OrderSettingMapper orderSettingMapper;

    /**
     * 修改总预约数
     * @param value
     * @param day
     * @return
     */
    @Override
    public Boolean updateNumber(String value, String day) throws ParseException {
        //转换格式为yyyy-MM-dd
        String replace = day.replace("\"", "");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date parse = sdf.parse(replace);
        OrderSetting orderSetting = new OrderSetting();
        orderSetting.setNumber(Integer.parseInt(value));
        orderSetting.setOrderDate(parse);
        return orderSettingMapper.updateByDate(orderSetting);
    }

    /**
     * 添加
     * @param list
     * @return
     */
    @Override
    public Boolean add(List<OrderSetting> list) {
        boolean flag = true;
        if(list != null && list.size()>0){
            for(OrderSetting orderSetting : list){
                long count = orderSettingMapper.findByDate(orderSetting.getOrderDate());
                if(count == 0){
                    flag = orderSettingMapper.add(orderSetting);
                }else{
                    flag = orderSettingMapper.updateByDate(orderSetting);
                }
            }
        }
        return flag;
    }

    /**
     * 查询所有
     * @param date
     * @return
     */
    @Override
    public List<Map<String,String>> findAll(String date) {
        //0年份 1月份
        String beginDate = date + "-1";//2019-3-1
        String endDate = date + "-31";//2019-3-31
        Map<String,String> map = new HashMap<>();
        map.put("beginDate",beginDate);
        map.put("endDate",endDate);
        List<OrderSetting> all = orderSettingMapper.findAll(map);
        List<Map<String,String>> result = new ArrayList<>();
        for(OrderSetting orderSetting : all){
            Map<String, String> orderSettingMap = new HashMap<>();
            orderSettingMap.put("date",String.valueOf(orderSetting.getOrderDate().getDate()));
            orderSettingMap.put("number",String.valueOf(orderSetting.getNumber()));
            orderSettingMap.put("reservations",String.valueOf(orderSetting.getReservations()));
            result.add(orderSettingMap);
        }
        return result;
    }
}
