package com.healthproject.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.healthproject.model.Member;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

    @Select("select count(*) from t_member where regTime <= #{date}")
    Integer findMemberCountBeforeDate(String date);

    @Select("select count(*) from t_member where regTime = #{date,jdbcType=DATE}")
    Integer findTodayNewMember(Date date);

    @Select("select count(*) from t_member where regTime >= #{date,jdbcType=DATE}")
    Integer findThisWeekNewMember(Date date);

    @Select("select count(*) from t_member where regTime >= #{date,jdbcType=DATE}")
    Integer findThisMonthNewMember(Date date);
}
