package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Chinese;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface ChineseMapper extends BaseMapper<Chinese> {
    boolean updateBatch(List<Chinese> list);

    boolean updateBatchSelective(List<Chinese> list);

    boolean batchInsert(@Param("list") List<Chinese> list);

    int insertOrUpdate(Chinese record);

    boolean insertOrUpdateSelective(Chinese record);

    //    @Cacheable(cacheNames = {"#{name}" + "select"}, sync = true)
    @Select("select * from chinese where name=#{name}")
    Chinese getChineseByName(String name);
}