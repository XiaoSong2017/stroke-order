package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Chinese;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChineseMapper extends BaseMapper<Chinese> {
    int updateBatch(List<Chinese> list);

    int updateBatchSelective(List<Chinese> list);

    int batchInsert(@Param("list") List<Chinese> list);

    int insertOrUpdate(Chinese record);

    int insertOrUpdateSelective(Chinese record);
}