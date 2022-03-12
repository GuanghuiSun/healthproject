package com.healthproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healthproject.mapper.CheckGroupMapper;
import com.healthproject.mapper.CheckItemMapper;
import com.healthproject.model.CheckGroup;
import com.healthproject.service.CheckGroupService;
import com.healthproject.service.CheckItemService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class CheckGroupServiceImpl extends ServiceImpl<CheckGroupMapper, CheckGroup> implements CheckGroupService {

    @Resource
    private CheckGroupMapper checkGroupMapper;

    /**
     * 分页条件查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public IPage<CheckGroup> getPage(int currentPage, int pageSize, String queryString) {
        IPage<CheckGroup> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<CheckGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(queryString),CheckGroup::getCode,queryString)//编码
                .or().like(Strings.isNotEmpty(queryString),CheckGroup::getName,queryString)//名称
                .or().like(Strings.isNotEmpty(queryString),CheckGroup::getHelpCode,queryString);//助记码
        return checkGroupMapper.selectPage(page,wrapper);
    }

    /**
     * 查找关系
     * @param groupId
     * @return
     */
    public List<Integer> findRelationById(int groupId){
        return checkGroupMapper.findRelationById(groupId);
    }

    /**
     * 添加关系
     * @param checkItemIds
     * @param groupId
     * @return
     */
    @Override
    public boolean addRelation(Integer[] checkItemIds,int groupId){
        for(Integer itemId : checkItemIds){
            if(!checkGroupMapper.addRelation(itemId,groupId)){
                return false;
            }
        }
        return true;
    }

    /**
     * 移除关系
     * @param groupId
     * @return
     */
    @Override
    public Boolean removeRelation(int groupId) {
        return checkGroupMapper.removeRelation(groupId);
    }


}
