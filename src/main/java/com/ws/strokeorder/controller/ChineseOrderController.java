package com.ws.strokeorder.controller;

import com.ws.strokeorder.service.ChineseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangsong
 */
@RestController
public class ChineseOrderController {
    @Autowired
    private ChineseOrderService chineseOrderService;

    @RequestMapping(value = "/stroke-sort", method = RequestMethod.POST)
    public List<String> chineseOrder(@NonNull ArrayList<String> arrayList) {
        arrayList.sort((a, b) -> {
            for (int i = 0; i < a.length() && i < b.length(); ++i) {
                String chineseA = a.substring(i, i + 1);
                String chineseB = b.substring(i, i + 1);
                try {
                    if (!chineseA.equals(chineseB)) {
                        String[] strokeA = chineseStroke(chineseA);
                        String[] strokeB = chineseStroke(chineseB);
                        for (int j = 0; j < strokeA.length && j < strokeB.length; ++j) {
                            if (chineseOrderService.containStrokeByName(strokeA[j])) {
                                System.out.println("未知笔顺：" + strokeA[j]);
                            }
                            if (chineseOrderService.containStrokeByName(strokeB[j])) {
                                System.out.println("未知笔顺：" + strokeB[j]);
                            }
                            Integer categoryA = chineseOrderService.getCategoryByName(strokeA[j]);
                            Integer categoryB = chineseOrderService.getCategoryByName(strokeB[j]);
                            if (categoryA > categoryB) {
                                return -1;
                            } else if (categoryA < categoryB) {
                                return 1;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.print(chineseA + "++:++" + chineseB);
                    e.printStackTrace();
                }
            }
            return 0;
        });
        return arrayList;
    }

    @RequestMapping(value = "/chineseStroke", method = RequestMethod.POST)
    public String[] chineseStroke(@NonNull String chinese) {
        chineseOrderService.updateViewByName(chinese);
        return chineseOrderService.chineseStroke(chinese);
    }

    @RequestMapping(value = "/chinesesStroke", method = RequestMethod.POST)
    public Map<String, String[]> chinesesStroke(@NonNull String chineses) {
        Map<String, String[]> res = new LinkedHashMap<>();
        for (char i : chineses.toCharArray()) {
            chineseOrderService.updateViewByName(String.valueOf(i));
            res.put(String.valueOf(i), chineseOrderService.chineseStroke(String.valueOf(i)));
        }
        return res;
    }
}
