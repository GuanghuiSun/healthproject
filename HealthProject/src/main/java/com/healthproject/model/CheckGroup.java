package com.healthproject.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 检查组
 */
@Data
@TableName("t_checkgroup")
public class CheckGroup implements Serializable {
    private Integer id;//主键
    private String code;//编码
    private String name;//名称
    @TableField("helpCode")
    private String helpCode;//助记
    private String sex;//适用性别
    private String remark;//介绍
    private String attention;//注意事项
    @TableField(exist = false)
    private List<CheckItem> checkItems;//一个检查组合包含多个检查项
    @TableLogic
    private int isDeleted;
}
