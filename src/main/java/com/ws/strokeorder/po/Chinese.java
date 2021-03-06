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
@TableName(value = "chinese")
public class Chinese implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 汉字名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 访问量
     */
    @TableField(value = "view")
    private Integer view;

    private static final long serialVersionUID = 1L;

    public Chinese(String name) {
        this.name = name;
        this.view=1;
    }

    public Integer increaseView(){
        return ++view;
    }
}