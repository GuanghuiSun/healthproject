package com.healthproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healthproject.mapper.SetMealMapper;
import com.healthproject.model.SetMeal;
import com.healthproject.service.SetMealService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {

    @Resource
    private SetMealMapper setMealMapper;

    /**
     * 分页条件查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public IPage<SetMeal> getPage(int currentPage, int pageSize, String queryString) {
        IPage<SetMeal> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<SetMeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(queryString), SetMeal::getCode,queryString)//编码
                .or().like(Strings.isNotEmpty(queryString),SetMeal::getName,queryString)//名称
                .or().like(Strings.isNotEmpty(queryString),SetMeal::getHelpCode,queryString);//助记码
        return setMealMapper.selectPage(page,wrapper);
    }

    /**
     * 添加关系
     * @param checkGroupIds
     * @param mealId
     * @return
     */
    @Override
    public Boolean addRelation(Integer[] checkGroupIds, int mealId) {
        for(Integer id : checkGroupIds){
            if(!(setMealMapper.addRelation(id,mealId))){
                return false;
            }
        }
        return true;
    }


    /**
     * 寻找关系
     * @param mealId
     * @return
     */
    @Override
    public List<Integer> findRelationById(int mealId) {
        return findRelationById(mealId);
    }

    /**
     * 查询套餐及对应的人数
     * @return
     */
    @Override
    public List<Map<String, Object>> findCount() {
        return setMealMapper.findSetMealCount();
    }

    @Override
    public List<String> findAllNames() {
        return setMealMapper.findAllNames();
    }


}
