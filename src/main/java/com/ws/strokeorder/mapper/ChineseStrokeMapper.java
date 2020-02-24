package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.ChineseStroke;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChineseStrokeMapper extends BaseMapper<ChineseStroke> {
    int updateBatch(List<ChineseStroke> list);

    int updateBatchSelective(List<ChineseStroke> list);

    int batchInsert(@Param("list") List<ChineseStroke> list);

    int insertOrUpdate(ChineseStroke record);

    int insertOrUpdateSelective(ChineseStroke record);
}