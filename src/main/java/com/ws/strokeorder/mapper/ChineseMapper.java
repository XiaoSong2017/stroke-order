package com.ws.strokeorder.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ws.strokeorder.po.Chinese;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * @author wangsong
 */
@Mapper
public interface ChineseMapper extends BaseMapper<Chinese> {
    int updateBatch(@NonNull List<Chinese> list);

    int updateBatchSelective(@NonNull List<Chinese> list);

    int batchInsert(@Param("list") @NonNull List<Chinese> list);

    int insertOrUpdate(@NonNull Chinese record);

    int insertOrUpdateSelective(@NonNull Chinese record);

    /**
     * 根据汉字名称修改用户访问量
     *
     * @param chineseName 汉字名称
     * @param view        访问量
     * @return 修改是否成功，成功返回true，否则返回false
     */
    @Update("UPDATE chinese set view=#{view} WHERE name=#{chineseName};")
    boolean updateViewByName(@NonNull String chineseName, Integer view);

    /**
     * 根据汉字名称获取汉字并增加汉字访问量
     *
     * @param chineseName 汉字名称
     * @return 对应汉字
     */
    @Cacheable(cacheNames = "chinese:60000000", key = "#chineseName", condition = "#result!=null", sync = true)
    @Select("select * from chinese where name=#{chineseName}")
    Chinese getChineseByName(@NonNull String chineseName);

    /**
     * 获取访问量最多的几个汉字
     * @param number 汉字数量
     * @return 对应的汉字集
     */
    @Cacheable(cacheNames = "normal_chinese",condition = "#result!=null",sync = true)
    @Select("SELECT * FROM chinese ORDER BY chinese.`view` DESC LIMIT #{number} ;")
    List<Chinese> getMostViewChinese(@NonNull Integer number);
}