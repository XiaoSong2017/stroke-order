package com.ws.strokeorder.controller;

import com.ws.strokeorder.service.ChineseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class ChineseOrderController {
    @Autowired
    private ChineseOrderService chineseOrderService;

    @RequestMapping(value = "/stroke-sort", method = RequestMethod.POST)
    public List<String> chineseOrder(@NonNull ArrayList<String> arrayList) {
        arrayList.sort((a, b) -> {
            for (int i = 0; i < a.length() && i < b.length(); ++i) {
                String temp_a = a.substring(i, i + 1);
                String temp_b = b.substring(i, i + 1);
                try {
                    if (!temp_a.equals(temp_b)) {
                        String[] bishun_a = chineseStroke(temp_a);
                        String[] bishun_b = chineseStroke(temp_b);
                        for (int j = 0; j < bishun_a.length && j < bishun_b.length; ++j) {
                            if (chineseOrderService.containStrokeByName(bishun_a[j])) System.out.println("未知笔顺：" + bishun_a[j]);
                            if (chineseOrderService.containStrokeByName(bishun_b[j])) System.out.println("未知笔顺：" + bishun_b[j]);
                            Integer category_a = chineseOrderService.getCategoryByName(bishun_a[j]);
                            Integer category_b = chineseOrderService.getCategoryByName(bishun_b[j]);
                            if (category_a > category_b) return -1;
                            else if (category_a < category_b) return 1;
                        }
                    }
                } catch (Exception e) {
                    System.out.print(temp_a + "++:++" + temp_b);
                    e.printStackTrace();
                }
            }
            return 0;
        });
        return arrayList;
    }

    @RequestMapping(value = "/chineseStroke", method = RequestMethod.POST)
    public String[] chineseStroke(@NonNull String chinese) {
        return chineseOrderService.chineseStroke(chinese);
    }

    @RequestMapping(value = "/chinesesStroke", method = RequestMethod.POST)
    public Map<String, String[]> chinesesStroke(@NonNull String chineses) {
//        System.out.println(Collections.singletonList(chineses).toString());
        Map<String, String[]> res = new LinkedHashMap<>();
        for (char i:chineses.toCharArray()){
            res.put(String.copyValueOf(new char[]{i}),chineseOrderService.chineseStroke(String.copyValueOf(new char[]{i})));
        }
        return res;
    }
}
