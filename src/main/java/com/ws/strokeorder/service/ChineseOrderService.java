package com.ws.strokeorder.service;

import com.ws.strokeorder.mapper.ChineseMapper;
import com.ws.strokeorder.mapper.ChineseStrokeMapper;
import com.ws.strokeorder.mapper.StrokeMapper;
import com.ws.strokeorder.po.Chinese;
import com.ws.strokeorder.po.ChineseStroke;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class ChineseOrderService {
    @Autowired
    private ChineseMapper chineseMapper;
    @Autowired
    private ChineseStrokeMapper chineseStrokeMapper;
    @Autowired
    private StrokeMapper strokeMapper;

    /**
     * 从网络中获取的数据指定汉字的笔顺并保存
     *
     * @param name 汉字名称
     * @return 汉字的笔顺
     */
    @Transactional
    public String[] insertChineseStrokeFromNet(String name) {
        String[] strokes = getOrderOfStrokes(name);
        if (strokes != null) {
            Chinese chinese = new Chinese(name);
            chineseMapper.insert(chinese);
            chinese.setId(Objects.requireNonNull(getChineseAndUpdateViewByName(name)).getId());
            List<ChineseStroke> chineseStrokeList = new ArrayList<>();
            for (int i = 0; i < strokes.length; ++i) {
//            System.out.println(strokes[i]);
                if (containStrokeByName(strokes[i])) {
                    ChineseStroke chineseStroke = new ChineseStroke(Objects.requireNonNull(chinese).getId(), Objects.requireNonNull(strokeMapper.getStrokeByName(strokes[i])).getId(), i + 1);
//            chineseStrokeMapper.insert(chineseStroke);
                    chineseStrokeList.add(chineseStroke);
                }
            }
            chineseStrokeMapper.batchInsert(chineseStrokeList);
        }
        return strokes;
    }

    /**
     * 判断是否包含当前汉字
     *
     * @param name 汉字名称
     * @return 判断结果：存在则为true否则为false
     */
    @Transactional(readOnly = true)
    protected boolean containChineseByName(String name) {
        return Objects.nonNull(chineseMapper.getChineseByName(name));
    }

    /**
     * 判断是否包含当前笔顺
     *
     * @param name 笔顺名称
     * @return 判断结果：存在则为true否则为false
     */
    @Transactional(readOnly = true)
    public boolean containStrokeByName(String name) {
        return Objects.nonNull(strokeMapper.getStrokeByName(name));
    }

    /**
     * 从网络上获取汉字笔顺
     *
     * @param chinese 查询的汉字
     * @return 对应汉字的笔顺
     */
    public synchronized String[] getOrderOfStrokes(String chinese) {
/*
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(10);
        connectionManager.getParams().setConnectionTimeout(300000000);
        connectionManager.getParams().setSoTimeout(300000000);
*/
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        String returnJson = null;
        try {
//            System.out.println(chineseConvertToUnicode(chinese));
            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(new HttpGet("http://bishun.shufaji.com/" + chineseConvertToUnicode(chinese) + ".html"));
            returnJson = new String(closeableHttpResponse.getEntity().getContent().readAllBytes());
            closeableHttpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (closeableHttpClient != null) closeableHttpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
/*
        GetMethod method = new GetMethod("http://bishun.shufaji.com/" + chineseConvertToUnicode(str) + ".html");
        System.out.println(method.getURI());
        client.executeMethod(method);
        String returnJson = new String(method.getResponseBody(), StandardCharsets.UTF_8);
*/
        int idx1 = returnJson != null ? returnJson.indexOf("<div id=\"hzcanvas\">") : -1;
        String[] res;
        if (idx1 != -1) {
            idx1 += 19;
            int idx2 = returnJson.indexOf("</div>", idx1);
            returnJson = returnJson.substring(idx1, idx2 == -1 ? returnJson.length() : idx2);
            returnJson = returnJson.substring(returnJson.indexOf("：") + 1);
            returnJson = returnJson.substring(returnJson.indexOf("：") + 1);
            res = returnJson.split(",");
        } else res = null;
        return res;
    }

    /**
     * &#x4e2d;&#x6587;&#x8f6c;Unicode
     *
     * @param str 传入字符串
     * @return returnStr 转换后的字符串
     */
    private String chineseConvertToUnicode(@NonNull String str) {
        StringBuilder returnStr = new StringBuilder();
        for (char aChar : str.toCharArray()) {
//            System.out.println(aChar);
            returnStr.append("0x").append(Integer.toString(aChar, 16));
        }
        return returnStr.toString();
    }

    /**
     * 本地查询指定汉字的笔顺
     *
     * @param chinese 指定汉字
     * @return 对应笔顺
     */
    @Transactional
    @Cacheable(cacheNames = "chinese_stroke", sync = true)
    public String[] chineseStroke(String chinese) {
        String[]res;
        if(containChineseByName(chinese)){
            res=chineseStrokeMapper.selectStrokesByChinese(chinese);
            getChineseAndUpdateViewByName(chinese);
        }else {
            res=insertChineseStrokeFromNet(chinese);
        }
        return  res;
    }

    /**
     * 获取笔顺所属类别
     *
     * @param stroke 笔顺
     * @return 对应类别的值
     */
    public Integer getCategoryByName(String stroke) {
        return strokeMapper.getCategoryByName(stroke);
    }

    /**
     * 根据汉字名称获取汉字并增加汉字访问量
     * @param name 汉字名称
     * @return 对应汉字
     */
    @Transactional
    @CachePut(cacheNames = "chinese",key = "#name")
    public Chinese getChineseAndUpdateViewByName(String name) {
        Chinese chinese=chineseMapper.getChineseByName(name);
        chineseMapper.updateViewByName(name,chinese.increaseView());
        return chinese;
    }
}
