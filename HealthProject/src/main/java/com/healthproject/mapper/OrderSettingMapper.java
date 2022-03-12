package com.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthproject.model.OrderSetting;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderSettingMapper extends BaseMapper<OrderSetting> {

    @Insert("insert into t_ordersetting values(null,#{orderDate,jdbcType=DATE},#{number},#{reservations})")
    Boolean add(OrderSetting orderSetting);

    @Select("select count(*) from t_ordersetting where orderDate = #{orderDate,jdbcType=DATE} ")
    long findByDate(Date orderDate);

    @Update("update t_ordersetting set number = #{number} where orderDate = #{orderDate,jdbcType=DATE}")
    Boolean updateByDate(OrderSetting orderSetting);

    @Select("select * from t_ordersetting where orderDate between #{beginDate} and #{endDate} ")
    List<OrderSetting> findAll(Map map);
}
