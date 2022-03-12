package com.healthproject.mapper;

import com.healthproject.model.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    @Select("select count(*) from t_order where orderDate = #{date,jdbcType=DATE}")
    Integer findTodayOrderCount(Date date);

    @Select("select count(*) from t_order where orderDate <= #{date,jdbcType=DATE}")
    Integer findThisWeekOrderCount(Date date);

    @Select("select count(*) from t_order where orderDate <= #{date,jdbcType=DATE}")
    Integer findThisMonthOrderCount(Date date);

    @Select("select count(*) from t_order where orderStatus = '已到诊' and orderDate = #{date,jdbcType=DATE}")
    Integer findTodayVisitsCount(Date date);

    @Select("select count(*) from t_order where orderStatus = '已到诊' and orderDate <= #{date,jdbcType=DATE} ")
    Integer findThisWeekVisitsCount(Date date);

    @Select("select count(*) from t_order where orderStatus = '已到诊' and orderDate <= #{date,jdbcType=DATE} ")
    Integer findThisMonthVisitsCount(Date date);

//    {"proportion":0.4545,"name":"粉红珍爱(女)升级TM12项筛查体检套餐","setmeal_count":5},
    @Select("select count(o.id)/(select count(*) from t_order) as proportion,s.name,count(o.id) as setmeal_count\n" +
            "from t_order o,t_setmeal s\n" +
            "where o.setmeal_id = s.id\n" +
            "group by s.name\n" +
            "order by setmeal_count desc\n" +
            "limit 0,4 ")
    List<Map<String,Object>> findHotSetMeal();

}
