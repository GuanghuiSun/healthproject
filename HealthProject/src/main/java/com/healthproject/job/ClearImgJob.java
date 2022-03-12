package com.healthproject.job;

import com.healthproject.config.TencentCOS;
import com.healthproject.constant.RedisConstant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
@EnableScheduling
@Slf4j
public class ClearImgJob extends QuartzJobBean {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        //根据Redis中保存的两个set集合进行差值计算，获得垃圾图片名称集合
        Set<String> set = stringRedisTemplate.opsForSet().difference(RedisConstant.SETMEAL_PIC_RESOURCES,
                        RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        if(set!=null){
            for(String imgName : set){
                Set<String> members = stringRedisTemplate.opsForSet().members(RedisConstant.SETMEAL_PIC_RESOURCES);
                Set<String> members1 = stringRedisTemplate.opsForSet().members(RedisConstant.SETMEAL_PIC_DB_RESOURCES);
                System.out.println(members);
                System.out.println(members1);
                //删除服务器上的图片
                TencentCOS.deletefile(imgName);
                //删除redis里的记录
                stringRedisTemplate.opsForSet().remove(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
            }
        }

    }
}
