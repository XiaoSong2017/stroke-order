package com.ws.strokeorder.service;

import com.ws.strokeorder.mapper.ChineseMapper;
import com.ws.strokeorder.mapper.ChineseStrokeMapper;
import com.ws.strokeorder.mapper.StrokeMapper;
import com.ws.strokeorder.po.Chinese;
import com.ws.strokeorder.po.ChineseStroke;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        list.sort((a, b) -> {
            for (int i = 0; i < a.length() && i < b.length(); ++i) {
                String temp_a = a.substring(i, i + 1);
                String temp_b = b.substring(i, i + 1);
                try {
                    if (!temp_a.equals(temp_b)) {
                        String[] bishun_a;
                        if (containsChineseByName(temp_a))
                            bishun_a = chineseStrokeMapper.selectStrokesByChinese(temp_a);
                        else bishun_a = insertChineseStrokeFromNet(temp_a);
                        String[] bishun_b;
                        if (containsChineseByName(temp_b))
                            bishun_b = chineseStrokeMapper.selectStrokesByChinese(temp_b);
                        else bishun_b = insertChineseStrokeFromNet(temp_b);
                        for (int j = 0; j < bishun_a.length && j < bishun_b.length; ++j) {
                            if (!containsStrokeByName(bishun_a[j])) System.out.println("未知笔顺：" + bishun_a[j]);
                            if (!containsStrokeByName(bishun_b[j])) System.out.println("未知笔顺：" + bishun_b[j]);
                            Integer category_a = strokeMapper.getCategoryByName(bishun_a[j]);
                            Integer category_b = strokeMapper.getCategoryByName(bishun_b[j]);
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
        return list;
    }

    @Transactional
    public String[] insertChineseStrokeFromNet(String name) throws Exception {
        String[] strokes = getOrderOfStrokes(name);
        Chinese chinese = new Chinese(name);
        chineseMapper.insert(chinese);
        List<ChineseStroke>chineseStrokeList=new ArrayList<>();
        for (int i = 0; i < strokes.length; ++i) {
//            System.out.println(strokes[i]);
            ChineseStroke chineseStroke = new ChineseStroke(chinese.getId(), strokeMapper.getStrokeByName(strokes[i]).getId(), i + 1);
//            chineseStrokeMapper.insert(chineseStroke);
            chineseStrokeList.add(chineseStroke);
        }
//        chineseStrokeMapper.batchInsert(chineseStrokeList);
        return strokes;
    }

    /**
     * 判断是否包含当前汉字
     *
     * @param name 汉字名称
     * @return
     */
    @Transactional(readOnly = true)
    protected boolean containsChineseByName(String name) {
        return chineseMapper.getChineseByName(name) != null;
    }

    /**
     * 判断是否包含当前笔顺
     *
     * @param name 笔顺名称
     * @return
     */
    @Transactional(readOnly = true)
    protected boolean containsStrokeByName(String name) {
        return strokeMapper.getStrokeByName(name) != null;
    }

    public String[] getOrderOfStrokes(String str) throws Exception {
        HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
//        connectionManager.getParams().setDefaultMaxConnectionsPerHost(10);
//        connectionManager.getParams().setConnectionTimeout(300000000);
//        connectionManager.getParams().setSoTimeout(300000000);
        HttpClient client = new HttpClient(connectionManager);
        GetMethod method = new GetMethod("http://bishun.shufaji.com/" + chineseConvertToUnicode(str) + ".html");
//        System.out.println(method.getURI());
        client.executeMethod(method);
        String returnJson = new String(method.getResponseBody(), StandardCharsets.UTF_8);
        int idx1 = returnJson.indexOf("<div id=\"hzcanvas\">");
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
    private String chineseConvertToUnicode(String str) {
        char[] chars = str.toCharArray();
        StringBuilder returnStr = new StringBuilder();
        for (char aChar : chars) {
//            System.out.println(aChar);
            returnStr.append("0x").append(Integer.toString(aChar, 16));
        }
        return returnStr.toString();
    }
}
