package com.healthproject.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.healthproject.model.Member;
import org.apache.ibatis.javassist.bytecode.annotation.MemberValue;

import java.util.List;

public interface MemberService extends IService<Member> {

    List<Integer> findMemberCountByMonths(List<String> months);
}
