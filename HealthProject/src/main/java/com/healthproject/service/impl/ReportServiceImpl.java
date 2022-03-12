package com.healthproject.service.impl;

import com.healthproject.mapper.MemberMapper;
import com.healthproject.mapper.OrderMapper;
import com.healthproject.service.ReportService;
import com.healthproject.utils.DateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Resource
    private MemberMapper memberMapper;

    @Resource
    private OrderMapper orderMapper;

    /**
     * 查询所有信息
     * @return
     */
    @Override
    public Map<String,Object> getBusinessData(){
        //获得本周的第一天
        Date thisWeekMonday = DateUtils.getThisWeekMonday();
        //获取本月第一天
        Date firstDay4ThisMonth = DateUtils.getFirstDay4ThisMonth();

        Map<String,Object> map = new HashMap<>();
        //今日新增
        map.put("todayNewMember",memberMapper.findTodayNewMember(new Date()));
        //会员总量
        map.put("totalMember",memberMapper.selectCount(null));
        //获取本周新增
        map.put("thisWeekNewMember",memberMapper.findThisWeekNewMember(thisWeekMonday));
        //获取本月新增
        map.put("thisMonthNewMember",memberMapper.findThisMonthNewMember(firstDay4ThisMonth));
        //获取今日预约数
        map.put("todayOrderNumber",orderMapper.findTodayOrderCount(new Date()));
        //获取本周预约数
        map.put("thisWeekOrderNumber",orderMapper.findThisWeekOrderCount(thisWeekMonday));
        //获取本月预约数
        map.put("thisMonthOrderNumber",orderMapper.findThisMonthOrderCount(firstDay4ThisMonth));
        //获取今日到诊数
        map.put("todayVisitsNumber",orderMapper.findTodayVisitsCount(new Date()));
        //获取本周到诊数
        map.put("thisWeekVisitsNumber",orderMapper.findThisWeekVisitsCount(thisWeekMonday));
        //获取本月到诊数
        map.put("thisMonthVisitsNumber",orderMapper.findThisMonthVisitsCount(firstDay4ThisMonth));
        //上传当天时间
        map.put("reportDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        //上传热门套餐，取前四个
        map.put("hotSetMeal",orderMapper.findHotSetMeal());

        return map;
    }
}
