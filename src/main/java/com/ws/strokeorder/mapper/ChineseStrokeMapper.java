package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.ChineseStroke;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.lang.NonNull;

/**
 * @author wangsong
 */
@Mapper
public interface ChineseStrokeMapper extends BaseMapper<ChineseStroke> {
    int updateBatch(@NonNull List<ChineseStroke> list);

    int updateBatchSelective(@NonNull List<ChineseStroke> list);

    int batchInsert(@Param("list") @NonNull List<ChineseStroke> list);

    int insertOrUpdate(@NonNull ChineseStroke record);

    int insertOrUpdateSelective(@NonNull ChineseStroke record);

    @Select("select stroke.name from chinese,chinese_stroke,stroke where chinese.name=#{name}  and chinese.id = chinese_stroke.chinese_id and stroke.id = chinese_stroke.stroke_id order by chinese_stroke.strokes")
    String[] selectStrokesByChinese(@NonNull String name);
}
