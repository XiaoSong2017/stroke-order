package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Stroke;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StrokeMapper extends BaseMapper<Stroke> {
    int updateBatch(List<Stroke> list);

    int updateBatchSelective(List<Stroke> list);

    int batchInsert(@Param("list") List<Stroke> list);

    int insertOrUpdate(Stroke record);

    int insertOrUpdateSelective(Stroke record);
}