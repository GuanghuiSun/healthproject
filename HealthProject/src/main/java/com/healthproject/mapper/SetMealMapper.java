package com.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthproject.model.SetMeal;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetMealMapper extends BaseMapper<SetMeal> {

    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{mealId}")
    List<Integer> findRelationById(int mealId);

    @Insert("insert into t_setmeal_checkgroup (#{mealID},#{groupId})")
    Boolean addRelation(int groupId, int mealId);

    @SuppressWarnings("all")
    List<Map<String, Object>> findSetMealCount();

    @Select("select name from t_setmeal")
    List<String> findAllNames();
}
