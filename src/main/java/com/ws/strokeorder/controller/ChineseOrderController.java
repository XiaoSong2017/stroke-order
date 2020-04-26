package com.ws.strokeorder.controller;

import com.ws.strokeorder.service.ChineseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @RequestMapping(value = "/stroke-sort", method = RequestMethod.POST)
    public List<String> chineseOrder(@RequestBody @NonNull List<String> strings) {
//        System.out.println(strings.toString());
        strings.sort((a, b) -> {
            for (int i = 0; i < a.length() && i < b.length(); ++i) {
                String chineseA = a.substring(i, i + 1);
                String chineseB = b.substring(i, i + 1);
                try {
                    if (!chineseA.equals(chineseB)) {
                        String[] strokeA = chineseStroke(chineseA);
                        String[] strokeB = chineseStroke(chineseB);
                        for (int j = 0; j < strokeA.length && j < strokeB.length; ++j) {
                            if (!chineseOrderService.containStrokeByName(strokeA[j])) {
                                System.out.println("未知笔顺：" + strokeA[j]);
                            }
                            if (!chineseOrderService.containStrokeByName(strokeB[j])) {
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
        return strings;
    }

    @ResponseBody
    @RequestMapping(value = "/chineseStroke", method = RequestMethod.POST)
    public String[] chineseStroke(@NonNull String chinese) {
        if (chineseOrderService.containChineseByName(chinese)) {
            chineseOrderService.updateViewByName(chinese);
        }
        return chineseOrderService.chineseStroke(chinese);
    }

    @ResponseBody
    @RequestMapping(value = "/chinesesStroke", method = RequestMethod.POST)
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

    @RequestMapping(value = "/chineseSort")
    public String chineseSort() {
        return "chinese-stroke.html";
    }
}
