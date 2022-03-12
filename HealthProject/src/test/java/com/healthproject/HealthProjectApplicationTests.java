package com.healthproject;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healthproject.constant.RedisConstant;
import com.healthproject.controller.SetMealController;
import com.healthproject.mapper.CheckGroupMapper;
import com.healthproject.model.CheckGroup;
import com.healthproject.utils.DateUtils;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SpringBootTest
class HealthProjectApplicationTests {

    @Resource
    private CheckGroupMapper checkGroupMapper;

    @Resource
    private StringRedisTemplate s;

    @Test
    void contextLoads() {
        String queryString = "";
        LambdaQueryWrapper<CheckGroup> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Strings.isNotEmpty(queryString),CheckGroup::getCode,queryString)//编码
                .or().like(Strings.isNotEmpty(queryString),CheckGroup::getName,queryString)//名称
                .or().like(Strings.isNotEmpty(queryString),CheckGroup::getHelpCode,queryString);//助记码
        IPage<CheckGroup> page = new Page<>(1,100);
//        checkGroupMapper.getPages(wrapper);
    }

    @Test
    void test(){
        s.opsForSet().add("cat","yes");
        s.opsForSet().add("cat","no");
        s.opsForSet().add("cat2","no");
        s.opsForSet().add("cat2","yes");
        s.opsForSet().add("cat2","kone");
        s.opsForSet().add("cat","none");
        s.opsForSet().add("cat","nqweqe");
        Set<String> cat = s.opsForSet().members("cat");
        cat.forEach(System.out::println);
        Set<String> difference = s.opsForSet().difference("cat", "cat2");
        difference.forEach(System.out::println);
        System.out.println();
        for(String set : difference){
            System.out.println(set);
            s.opsForSet().remove("cat",set);
        }
        Set<String> cat1 = s.opsForSet().members("cat");
        cat1.forEach(System.out::println);
    }

    @Test
    void test2(){
        Set<String> members = s.opsForSet().members(RedisConstant.SETMEAL_PIC_RESOURCES);
        Set<String> members1 = s.opsForSet().members(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        System.out.println(members);
        System.out.println(members1);

    }

    @Test
    void test3() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook("D:\\Java\\SpringBootDemo\\HealthProject\\src\\main\\resources\\templates\\ordersetting_template.xlsx");
        XSSFSheet sheetAt = workbook.getSheetAt(0);
        int lastRowNum = sheetAt.getLastRowNum();
        for(int i = 0; i <= lastRowNum; i++){
            XSSFRow row = sheetAt.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for(short j = 0; j < lastCellNum; j++){
                String stringCellValue = row.getCell(j).getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
        workbook.close();
    }

    @Test
    void test4(){
        Date date = new Date();
        System.out.println(date.getDate());
    }

    @Test
    void test5(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        System.out.println(calendar);
        System.out.println(Calendar.MONTH);
    }

    @Test
    void test6(){
        Date date = new Date();
        System.out.println(date);
        Date thisWeekMonday = DateUtils.getThisWeekMonday();
        System.out.println(thisWeekMonday);
        System.out.println(DateUtils.getFirstDay4ThisMonth());
    }

    @Test
    void test7(){
        System.out.println(File.separator);
    }
}
