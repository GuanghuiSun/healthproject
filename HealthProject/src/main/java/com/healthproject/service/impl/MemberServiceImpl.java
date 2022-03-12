package com.healthproject.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.healthproject.mapper.MemberMapper;
import com.healthproject.model.Member;
import com.healthproject.service.MemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Override
    public List<Integer> findMemberCountByMonths(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for(String date : months){
            date += "-31";//2022-2-31
            list.add(memberMapper.findMemberCountBeforeDate(date));
        }
        return list;
    }
}
