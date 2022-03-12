package com.healthproject.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.healthproject.Base.BaseResponse;
import com.healthproject.Base.ResultUtils;
import com.healthproject.model.CheckItem;
import com.healthproject.service.CheckItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {

    @Resource
    private CheckItemService checkItemService;

    /**
     * 查询信息
     *
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @GetMapping("{currentPage}/{pageSize}")
    public BaseResponse<IPage<CheckItem>> getPage(@PathVariable int currentPage, @PathVariable int pageSize, String queryString) {
        IPage<CheckItem> page = checkItemService.getPage(currentPage, pageSize, queryString);
        if (currentPage > page.getPages()) {
            page = checkItemService.getPage((int) page.getPages(), pageSize, queryString);
        }
        return ResultUtils.success(page);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @GetMapping
    public BaseResponse<List<CheckItem>> findAll() {
        return ResultUtils.success(checkItemService.list());
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @GetMapping("{id}")
    public BaseResponse<CheckItem> findById(@PathVariable int id){
        return ResultUtils.success(checkItemService.getById(id));
    }

    /**
     * 添加
     *
     * @param checkItem
     * @return
     */
    @PostMapping
    public BaseResponse<Integer> save(@RequestBody CheckItem checkItem) {
        return checkItemService.save(checkItem) ? ResultUtils.success(checkItem.getId()) : ResultUtils.error(checkItem.getId());
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public BaseResponse<Boolean> delete(@PathVariable int id) {
        return ResultUtils.success(checkItemService.removeById(id));
    }

    /**
     * 修改
     *
     * @param checkItem
     * @return
     */
    @PutMapping
    public BaseResponse<Boolean> update(@RequestBody CheckItem checkItem) {
        return ResultUtils.success(checkItemService.updateById(checkItem));
    }
}
