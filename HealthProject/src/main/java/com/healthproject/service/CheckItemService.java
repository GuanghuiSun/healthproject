package com.healthproject.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.healthproject.model.CheckItem;

public interface CheckItemService extends IService<CheckItem> {
    IPage<CheckItem> getPage(int currentPage, int pageSize, String queryString);
}
