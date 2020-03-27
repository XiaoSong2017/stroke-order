package com.ws.strokeorder.controller;

import com.ws.strokeorder.service.ChineseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class ChineseOrderController {
    @Autowired
    private ChineseOrderService chineseOrderService;

    @RequestMapping(value = "/stroke-sort",method = RequestMethod.POST)
    public List<String> chineseOrder(@NonNull ArrayList<String> arrayList) {
        return chineseOrderService.strokeOrder(arrayList);
    }

    @RequestMapping(value = "/chineseStroke",method = RequestMethod.POST)
    public String chineseStroke(@NonNull String chinese) {
        return Arrays.asList(chineseOrderService.chineseStroke(chinese)).toString();
    }
}
