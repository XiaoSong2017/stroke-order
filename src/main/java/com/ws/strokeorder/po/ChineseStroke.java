package com.ws.strokeorder.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "chinese_stroke")
public class ChineseStroke implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "chinese_id")
    private Integer chineseId;

    @TableField(value = "stroke_id")
    private Integer strokeId;

    /**
     * 第几笔
     */
    @TableField(value = "strokes")
    private Integer strokes;

    private static final long serialVersionUID = 1L;

    public ChineseStroke(Integer chineseId, Integer strokeId, Integer strokes) {
        this.chineseId = chineseId;
        this.strokeId = strokeId;
        this.strokes = strokes;
    }
}