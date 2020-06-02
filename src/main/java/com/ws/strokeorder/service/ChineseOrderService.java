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
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author wangsong
 */
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
    public String[] insertChineseStrokeFromNet(@NonNull String name) {
        String[] strokes = getOrderOfStrokes(name);
        if (strokes != null) {
            Chinese chinese = new Chinese(name);
            chineseMapper.insert(chinese);
            chinese.setId(Objects.requireNonNull(chineseMapper.getChineseByName(name)).getId());
            List<ChineseStroke> chineseStrokeList = new ArrayList<>();
            for (int i = 0; i < strokes.length; ++i) {
                if (Objects.nonNull(strokes[i])&&containStrokeByName(strokes[i])) {
                    ChineseStroke chineseStroke = new ChineseStroke(Objects.requireNonNull(chinese).getId(), Objects.requireNonNull(strokeMapper.getStrokeByName(strokes[i])).getId(), i + 1);
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
    public boolean containChineseByName(@NonNull String name) {
        return Objects.nonNull(chineseMapper.getChineseByName(name));
    }

    /**
     * 判断是否包含当前笔顺
     *
     * @param name 笔顺名称
     * @return 判断结果：存在则为true否则为false
     */
    @Transactional(readOnly = true)
    public boolean containStrokeByName(@NonNull String name) {
        return Objects.nonNull(strokeMapper.getStrokeByName(name));
    }

    /**
     * 从网络上获取汉字笔顺
     *
     * @param chinese 查询的汉字
     * @return 对应汉字的笔顺
     */
    public synchronized String[] getOrderOfStrokes(@NonNull String chinese) {
//        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
//        String returnJson=null;
//        try {
//            CloseableHttpResponse closeableHttpResponse = closeableHttpClient.execute(new HttpGet("http://bishun.shufaji.com/" + chineseConvertToUnicode(chinese) + ".html"));
//            returnJson = new String(closeableHttpResponse.getEntity().getContent().readAllBytes());
//            closeableHttpResponse.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (closeableHttpClient != null) {
//                    closeableHttpClient.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        int idx1 = returnJson != null ? returnJson.indexOf("<div id=\"hzcanvas\">") : -1;
//        String[] res;
//        if (idx1 != -1) {
//            idx1 += 19;
//            int idx2 = returnJson.indexOf("</div>", idx1);
//            returnJson = returnJson.substring(idx1, idx2 == -1 ? returnJson.length() : idx2);
//            returnJson = returnJson.substring(returnJson.indexOf("：") + 1);
//            returnJson = returnJson.substring(returnJson.indexOf("：") + 1);
//            res = returnJson.split(",");
//        } else {
//            res = null;
//        }
//        try {
//            String div=Jsoup.connect( "http://bishun.shufaji.com/"+chineseConvertToUnicode(chinese) + ".html" ).get().getElementById("hzcanvas").text();
//            if(div!=null&&!div.isEmpty()) {
//                return div.substring(div.indexOf("：")).substring(div.indexOf("：")+1).split(",");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            return Jsoup.connect("http://bs.kaishicha.com/" + chineseToUnicode(chinese) + ".html").get().getElementsByClass("bishun").textNodes().get(1).text().split("、");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
     * @param chineseName 指定汉字
     * @return 对应笔顺
     */
    @Transactional
    @Cacheable(cacheNames = "chinese_stroke:60000000", condition = "#result!=null", sync = true)
    public String[] chineseStroke(@NonNull String chineseName) {
        return containChineseByName(chineseName) ? chineseStrokeMapper.selectStrokesByChinese(chineseName) : insertChineseStrokeFromNet(chineseName);
    }

    /**
     * 获取笔顺所属类别
     *
     * @param strokeName 笔顺
     * @return 对应类别的值
     */
    @Transactional(readOnly = true)
    public Integer getCategoryByName(@NonNull String strokeName) {
        return strokeMapper.getStrokeByName(strokeName).getCategory();
    }

    /**
     * 根据汉字名称修改用户访问量并更新缓存
     *
     * @param chineseName 汉字名称
     * @return 修改后的汉字信息
     */
    @Transactional
    @CachePut(cacheNames = "chinese")
    public Chinese updateViewByName(@NonNull String chineseName) {
        return chineseMapper.updateViewByName(chineseName, chineseMapper.getChineseByName(chineseName).increaseView()) ? chineseMapper.getChineseByName(chineseName) : null;
    }

    /**
     * 中文转Unicode编码
     *
     * @param chinese 待转汉字
     * @return 转换后的Unicode十六进制串
     */
    public String chineseToUnicode(String chinese) {
        StringBuilder s1 = new StringBuilder();
        for (char i : chinese.toCharArray()) {
            s1.append(Integer.toHexString(i & 0xffff));
        }
        return s1.toString();
    }
}
