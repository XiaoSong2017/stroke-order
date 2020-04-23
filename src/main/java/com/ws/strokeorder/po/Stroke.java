package com.ws.strokeorder.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangsong
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "stroke")
public class Stroke implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 笔顺名称
     */
    @TableField(value = "name")
    private String name;
    /**
     * 笔顺所属类别
     */
    @TableField(value = "category")
    private Integer category;

    private static final long serialVersionUID = 1L;
}