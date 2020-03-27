package com.ws.strokeorder;

import com.ws.strokeorder.mapper.ChineseMapper;
import com.ws.strokeorder.mapper.ChineseStrokeMapper;
import com.ws.strokeorder.mapper.StrokeMapper;
import com.ws.strokeorder.service.ChineseOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class StrokeOrderApplicationTests {
    @Autowired
    ChineseOrderService chineseOrderService;
    @Autowired
    ChineseMapper chineseMapper;
    @Autowired
    StrokeMapper strokeMapper;
    @Autowired
    ChineseStrokeMapper chineseStrokeMapper;

    @Test
    void contextLoads() throws Exception {
//        System.out.println(strokeMapper.getStrokeByName("横").getId());
        System.out.println(Arrays.asList(chineseOrderService.insertChineseStrokeFromNet("的")));
//        for(Stroke stroke:strokeMapper.getAll()){
//            System.out.println(stroke.getName());
//        }
//        System.out.println(Arrays.asList(chineseStrokeMapper.selectStrokesByChinese("我")));
//        ArrayList<String> temp = new ArrayList<>();
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()) {
//            String t = scanner.nextLine();
////            System.out.println(t);
//            if (!t.equals("end")) temp.add(t);
//            else break;
//        }
//        scanner.close();
//        System.out.println(chineseOrderService.strokeOrder(temp));
    }

}
