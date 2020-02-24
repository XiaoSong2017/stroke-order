package com.ws.strokeorder.controller;

import com.ws.strokeorder.service.ChineseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChineseOrderController {
    @Autowired
    private ChineseOrderService chineseOrderService;

    @RequestMapping(value = "/stroke-sort")
    public List<String> ChineseOrder(@NonNull ArrayList<String> arrayList){
        return chineseOrderService.strokeOrder(arrayList);
    }
}
