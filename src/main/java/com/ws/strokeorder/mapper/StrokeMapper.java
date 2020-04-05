package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Stroke;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;

@Mapper
public interface StrokeMapper extends BaseMapper<Stroke> {
    int updateBatch(List<Stroke> list);

    int updateBatchSelective(List<Stroke> list);

    int batchInsert(@Param("list") List<Stroke> list);

    int insertOrUpdate(Stroke record);

    int insertOrUpdateSelective(Stroke record);

    @Cacheable(sync = true, value = {"stroke"},condition = "#result!=null")
    @Select("select * from stroke where name=#{name}")
    Stroke getStrokeByName(String name);

    //    @Cacheable(sync = true)
    @Select("select * from stroke")
    List<Stroke> getAll();

    @Select("select stroke.category from stroke where stroke.name=#{name}")
    Integer getCategoryByName(String name);
}