package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Chinese;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

/**
 * @author wangsong
 */
@Mapper
public interface ChineseMapper extends BaseMapper<Chinese> {
    int updateBatch(List<Chinese> list);

    int updateBatchSelective(List<Chinese> list);

    int batchInsert(@Param("list") List<Chinese> list);

    int insertOrUpdate(Chinese record);

    int insertOrUpdateSelective(Chinese record);

    /**
     * 根据汉字名称修改用户访问量
     *
     * @param chineseName 汉字名称
     * @param view        访问量
     * @return 修改是否成功，成功返回true，否则返回false
     */
    @Update("UPDATE chinese set view=#{view} WHERE name=#{chineseName};")
    boolean updateViewByName(String chineseName, Integer view);

    /**
     * 根据汉字名称获取汉字并增加汉字访问量
     *
     * @param chineseName 汉字名称
     * @return 对应汉字
     */
    @Cacheable(cacheNames = "chinese", key = "#chineseName", condition = "#result!=null", sync = true)
    @Select("select * from chinese where name=#{chineseName}")
    Chinese getChineseByName(String chineseName);
}