package com.healthproject.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.healthproject.model.CheckGroup;

import java.util.List;

public interface CheckGroupService extends IService<CheckGroup> {

    IPage<CheckGroup> getPage(int currentPage, int pageSize, String queryString);

    boolean addRelation(Integer[] checkItemIds,int groupId);

    Boolean removeRelation(int groupId);

    List<Integer> findRelationById(int groupId);

}
