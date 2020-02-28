package com.ws.strokeorder.mapper;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileNotFoundException;

public class ChineseStrokeMapperTest {
    @Autowired
    private static ChineseStrokeMapper mapper;

    @BeforeClass
    public static void setUpMybatisDatabase() {
        SqlSessionFactory builder = new SqlSessionFactoryBuilder().build(ChineseStrokeMapperTest.class.getClassLoader().getResourceAsStream("mybatisTestConfiguration/ChineseStrokeMapperTestConfiguration.xml"));
        //you can use builder.openSession(false) to not commit to database
        mapper = builder.getConfiguration().getMapper(ChineseStrokeMapper.class, builder.openSession(true));
    }

    @Test
    public void testBatchInsert() throws FileNotFoundException {

//        mapper.batchInsert();
    }
}
