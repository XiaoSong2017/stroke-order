package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.ChineseStroke;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author wangsong
 */
@Mapper
public interface ChineseStrokeMapper extends BaseMapper<ChineseStroke> {
    int updateBatch(List<ChineseStroke> list);

    int updateBatchSelective(List<ChineseStroke> list);

    int batchInsert(@Param("list") List<ChineseStroke> list);

    int insertOrUpdate(ChineseStroke record);

    int insertOrUpdateSelective(ChineseStroke record);

    @Select("select stroke.name from chinese,chinese_stroke,stroke where chinese.name=#{name}  and chinese.id = chinese_stroke.chinese_id and stroke.id = chinese_stroke.stroke_id order by chinese_stroke.strokes")
    String[] selectStrokesByChinese(String name);
}