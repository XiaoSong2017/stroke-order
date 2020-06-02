package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Stroke;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;

/**
 * @author wangsong
 */
@Mapper
public interface StrokeMapper extends BaseMapper<Stroke> {
    int updateBatch(@NonNull List<Stroke> list);

    int updateBatchSelective(@NonNull List<Stroke> list);

    int batchInsert(@Param("list") @NonNull  List<Stroke> list);

    int insertOrUpdate(@NonNull Stroke record);

    int insertOrUpdateSelective(@NonNull Stroke record);

    @Cacheable(cacheNames = "stroke:60000000", condition = "#result!=null", sync = true)
    @Select("select * from stroke where name=#{name}")
    Stroke getStrokeByName(@NonNull String name);

    @Select("select * from stroke")
    List<Stroke> getAll();
}