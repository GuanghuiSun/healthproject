package com.healthproject.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.healthproject.Base.BaseResponse;
import com.healthproject.Base.ResultUtils;
import com.healthproject.config.TencentCOS;
import com.healthproject.constant.RedisConstant;
import com.healthproject.model.SetMeal;
import com.healthproject.model.SetMeal;
import com.healthproject.service.SetMealService;
import org.apache.ibatis.annotations.ResultMap;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Resource
    private SetMealService setMealService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 查找所有信息
     * @param currentPage
     * @param pageSize
     * @param queryString
     * @return
     */
    @GetMapping("{currentPage}/{pageSize}")
    public BaseResponse<IPage<SetMeal>> getPages(@PathVariable int currentPage, @PathVariable int pageSize, String queryString){
        IPage<SetMeal> page = setMealService.getPage(currentPage, pageSize, queryString);
        if (currentPage > page.getPages()) {
            page = setMealService.getPage((int) page.getPages(), pageSize, queryString);
        }

        return ResultUtils.success(page);
    }

    /**
     * 添加
     * @param setMeal
     * @param checkGroupIds
     * @return
     */
    @PostMapping
    public BaseResponse<Integer> save(@RequestBody SetMeal setMeal, Integer[] checkGroupIds) {
        if(setMealService.save(setMeal) && setMealService.addRelation(checkGroupIds, setMeal.getId())){
            stringRedisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setMeal.getImg());
            return ResultUtils.success(setMeal.getId());
        }else{
            return ResultUtils.error(setMeal.getId());
        }
    }

    
    /**
     * 上传图片
     * @param multfile
     * @return
     */
    @RequestMapping("/upload")
    public BaseResponse<String> upload(@RequestParam("imgFile") MultipartFile multfile) {
        try {
            // 获取文件名
            String fileName1 = multfile.getOriginalFilename();
            // 获取文件后缀
            assert fileName1 != null;
            String prefix = fileName1.substring(fileName1.lastIndexOf("."));
            // 用uuid作为文件名，防止生成的临时文件重复
            final File excelFile = File.createTempFile("imagesFile-" + System.currentTimeMillis(), prefix);
            // 将MultipartFile转为File
            multfile.transferTo(excelFile);
            //调用腾讯云工具上传文件
            String fileName = TencentCOS.uploadfile(excelFile);
            //程序结束时，删除临时文件
            deleteFile(excelFile);
//        //存入图片名称，用于网页显示
//        map.put("imageName",fileName);
            //返回图片名称
            // 将图片名字存入redis
            stringRedisTemplate.opsForSet().add(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            return ResultUtils.success(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtils.error("上传失败");
        }

    }

    /**
     * 删除临时文件
     *
     * @param files
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }
}
