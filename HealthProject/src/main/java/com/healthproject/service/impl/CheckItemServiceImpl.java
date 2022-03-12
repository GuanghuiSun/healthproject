package com.healthproject.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healthproject.mapper.CheckItemMapper;
import com.healthproject.model.CheckItem;
import com.healthproject.service.CheckItemService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional
public class CheckItemServiceImpl extends ServiceImpl<CheckItemMapper, CheckItem> implements CheckItemService {

    @Resource
    private CheckItemMapper checkItemMapper;

    /**
     * 分页查询
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @Override
    public IPage<CheckItem> getPage(int currentPage, int pageSize, String queryString) {
        IPage<CheckItem> page = new Page<>(currentPage,pageSize);
        LambdaQueryWrapper<CheckItem> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(queryString),CheckItem::getCode,queryString)
                .or().like(Strings.isNotEmpty(queryString),CheckItem::getName,queryString);
        return checkItemMapper.selectPage(page,wrapper);
    }
}
