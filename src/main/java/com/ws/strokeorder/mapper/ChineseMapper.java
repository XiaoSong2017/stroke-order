package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Chinese;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.CachePut;

@Mapper
public interface ChineseMapper extends BaseMapper<Chinese> {
    int updateBatch(List<Chinese> list);

    int updateBatchSelective(List<Chinese> list);

    int batchInsert(@Param("list") List<Chinese> list);

    int insertOrUpdate(Chinese record);

    int insertOrUpdateSelective(Chinese record);

    @Update("UPDATE chinese SET chinese.view=#{view} WHERE chinese.name=#{name}")
    int updateViewByName(String name,Integer view);

    //    @Cacheable(value = {"chinese"},sync = true,condition ="#result!=null")
    @Select("select * from chinese where name=#{name}")
    Chinese getChineseByName(String name);
}