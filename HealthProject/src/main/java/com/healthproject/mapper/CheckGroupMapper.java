package com.healthproject.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.healthproject.model.CheckGroup;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CheckGroupMapper extends BaseMapper<CheckGroup> {
//    String deletedSql = "select * from t_checkgroup where is_deleted = 0";
//    String wrapperSql = "select * from (" + deletedSql +") as p ${ew.customSqlSegment}";
//    String field = "t1.*,t3.*,t2.id iid,t2.code icode,t2.name iname,t2.sex isex,t2.attention iattention,t2.remark iremark,t2.is_deleted iis_deleted";
//    String querySql = "select " + field + " from (" + wrapperSql + ") as t1 left join t_checkgroup_checkitem t3 on t1.id=t3.checkgroup_id " +
//            "left join t_checkitem t2 on t2.id=t3.checkitem_id ";


    /**
     * 查找关系
     * @param groupId
     * @return
     */
    @Select(("select checkitem_id from t_checkgroup_checkitem where checkgroup_id=#{groupId}"))
    List<Integer> findRelationById(int groupId);

    /**
     * 添加关系
     * @param itemId
     * @param groupId
     * @return
     */
    @Insert("insert into t_checkgroup_checkitem value(#{groupId},#{itemId})")
    Boolean addRelation(int itemId, int groupId);

    /**
     * 移除关系
     * @param groupId
     * @return
     */
    @Delete("delete from t_checkgroup_checkitem where checkgroup_id=#{groupId}")
    Boolean removeRelation(int groupId);
}
