package com.ws.strokeorder.service;

import com.ws.strokeorder.mapper.ChineseMapper;
import com.ws.strokeorder.mapper.ChineseStrokeMapper;
import com.ws.strokeorder.mapper.StrokeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChineseOrderService {
    @Autowired
    private ChineseMapper chineseMapper;
    @Autowired
    private ChineseStrokeMapper chineseStrokeMapper;
    @Autowired
    private StrokeMapper strokeMapper;

    @Transactional(readOnly = true)
    public List<String> strokeOrder(List<String> list) {
        return null;
    }

}
