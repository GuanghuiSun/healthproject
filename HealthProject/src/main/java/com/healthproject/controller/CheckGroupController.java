package com.healthproject.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.healthproject.Base.BaseResponse;
import com.healthproject.Base.ResultUtils;
import com.healthproject.model.CheckGroup;
import com.healthproject.service.CheckGroupService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/checkGroup")
public class CheckGroupController {

    @Resource
    private CheckGroupService checkGroupService;

    /**
     * 查询信息
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @GetMapping("{currentPage}/{pageSize}")
    public BaseResponse<IPage<CheckGroup>> getPage(@PathVariable int currentPage, @PathVariable int pageSize, String queryString) {
        IPage<CheckGroup> page = checkGroupService.getPage(currentPage, pageSize, queryString);
        if (currentPage > page.getPages()) {
            page = checkGroupService.getPage((int) page.getPages(), pageSize, queryString);
        }
        return ResultUtils.success(page);
    }

    @GetMapping
    public BaseResponse<List<CheckGroup>> findAll(){
        return ResultUtils.success(checkGroupService.list());
    }

    /**
     * 根据id查询信息
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public BaseResponse<CheckGroup> findById(@PathVariable int id){
        return ResultUtils.success(checkGroupService.getById(id));
    }

    /**
     * 根据id查找关系
     * @param id
     * @return
     */
    @GetMapping("/findRelationById/{id}")
    public BaseResponse<List<Integer>> findRelationById(@PathVariable int id){
        return ResultUtils.success(checkGroupService.findRelationById(id));
    }

    /**
     * 添加
     *
     * @param checkGroup
     * @return
     */
    @PostMapping
    public BaseResponse<Integer> save(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        return checkGroupService.save(checkGroup) && checkGroupService.addRelation(checkItemIds, checkGroup.getId()) ? ResultUtils.success(checkGroup.getId()) : ResultUtils.error(checkGroup.getId());
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public BaseResponse<Boolean> delete(@PathVariable int id) {
        if(checkGroupService.removeById(id) && checkGroupService.removeRelation(id)) {
            return ResultUtils.success(true);
        }
        return ResultUtils.error(false);
    }

    /**
     * 修改
     *
     * @param checkGroup
     * @return
     */
    @PutMapping
    public BaseResponse<Boolean> update(@RequestBody CheckGroup checkGroup, Integer[] checkItemIds) {
        return ResultUtils.success(checkGroupService.updateById(checkGroup) && checkGroupService.removeRelation(checkGroup.getId()) && checkGroupService.addRelation(checkItemIds,checkGroup.getId()));
    }

}
