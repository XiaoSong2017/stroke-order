package com.ws.strokeorder.controller;

import com.ws.strokeorder.service.ChineseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangsong
 */
@Controller
public class ChineseOrderController {
    @Autowired
    private ChineseOrderService chineseOrderService;

    @ResponseBody
    @PostMapping(value = "/stroke-sort")
    public List<String> chineseOrder(@RequestBody @NonNull List<String> strings) {
        final String chineseMatch = "[\\u4e00-\\u9fa5]";
        strings.sort((a, b) -> {
            for (int i = 0, j = 0; i < a.length() && j < b.length(); ++i, ++j) {
                while (i < a.length() && !a.substring(i, i + 1).matches(chineseMatch)) {
                    i++;
                }
                while (j < b.length() && !b.substring(j, j + 1).matches(chineseMatch)) {
                    j++;
                }
                if (i < a.length() && j < b.length()) {
                    String chineseA = a.substring(i, i + 1);
                    String chineseB = b.substring(j, j + 1);
                    try {
                        if (!chineseA.equals(chineseB)) {
                            String[] strokeA = chineseStroke(chineseA);
                            String[] strokeB = chineseStroke(chineseB);
                            for (int k = 0; k < strokeA.length && k < strokeB.length; ++k) {
                                if (!chineseOrderService.containStrokeByName(strokeA[k])) {
                                    System.out.println("未知笔顺：" + strokeA[k]);
                                }
                                if (!chineseOrderService.containStrokeByName(strokeB[k])) {
                                    System.out.println("未知笔顺：" + strokeB[k]);
                                }
                                Integer categoryA = chineseOrderService.getCategoryByName(strokeA[k]);
                                Integer categoryB = chineseOrderService.getCategoryByName(strokeB[k]);
                                if (categoryA > categoryB) {
                                    return -1;
                                } else if (categoryA < categoryB) {
                                    return 1;
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("未知汉字比较：" + chineseA + "++:++" + chineseB);
                    }
                }
            }
            return 0;
        });
        return strings;
    }

    @ResponseBody
    @PostMapping(value = "/chineseStroke")
    public String[] chineseStroke(@NonNull String chinese) {
        if (chineseOrderService.containChineseByName(chinese)) {
            chineseOrderService.updateViewByName(chinese);
        }
        return chineseOrderService.chineseStroke(chinese);
    }

    @ResponseBody
    @PostMapping(value = "/chinesesStroke")
    public Map<String, String[]> chinesesStroke(@NonNull String chineses) {
        Map<String, String[]> res = new LinkedHashMap<>();
        for (char chinese : chineses.toCharArray()) {
            if (chineseOrderService.containChineseByName(String.valueOf(chinese))) {
                chineseOrderService.updateViewByName(String.valueOf(chinese));
            }
            res.put(String.valueOf(chinese), chineseOrderService.chineseStroke(String.valueOf(chinese)));
        }
        return res;
    }

    @PostMapping(value = "/chineseSort")
    public String chineseSort() {
        return "chinese-stroke.html";
    }
}
