package com.healthproject.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.healthproject.model.CheckGroup;
import com.healthproject.model.SetMeal;

import java.util.List;
import java.util.Map;

public interface SetMealService extends IService<SetMeal> {
    IPage<SetMeal> getPage(int currentPage, int pageSize, String queryString);

    Boolean addRelation(Integer[] checkGroupIds,int mealId);

    List<Integer> findRelationById(int mealId);

    List<Map<String,Object>> findCount();

    List<String> findAllNames();
}
