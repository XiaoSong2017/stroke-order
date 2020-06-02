package com.ws.strokeorder.service;

import com.ws.strokeorder.mapper.ChineseMapper;
import com.ws.strokeorder.mapper.ChineseStrokeMapper;
import com.ws.strokeorder.mapper.StrokeMapper;
import com.ws.strokeorder.po.Chinese;
import com.ws.strokeorder.po.ChineseStroke;
import com.ws.strokeorder.po.Stroke;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class ChineseOrderServiceTest {
    @Autowired
    private ChineseStrokeMapper chineseStrokeMapper;
    @Autowired
    private ChineseMapper chineseMapper;
    @Autowired
    private StrokeMapper strokeMapper;
    @Autowired
    private ChineseOrderService chineseOrderService;

    @Test
    void insertChineseStrokeFromNet() {
//        System.out.println(chineseMapper);
//        System.out.println(String.format("%X",19968));
//        System.out.println(unicodeToString("\\u"+String.format("%X",32669)));
//        String ascii="\u4f01\u4e1a";//这两个unicode码就是企业的
//        System.out.println(ascii);//打印出来
//        String result= null;
//        try {
//            result = Jsoup.connect("http://bs.kaishicha.com/" + String.format("%X",28907) + ".html").get().getElementsByClass("bishun").get(0).text();
//            System.out.println(result.substring(result.indexOf("\"")+1,result.indexOf("\"")+2));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Document document=null;
        for (long uniocde = 19968,count; uniocde <= 40869; ++uniocde) {
            for (count=0;count<7;++count) {
                try {
                    Connection connection = Jsoup.connect("http://bs.kaishicha.com/" + String.format("%X", uniocde) + ".html");
                    document = connection.get();
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (count==7){
                System.out.println("查找失败："+uniocde);
            }else {
                String[] strokes = Objects.requireNonNull(document).getElementsByClass("bishun").textNodes().get(1).text().split("、");
                String name = unicodeToString("\\u" + String.format("%X", uniocde));
                Chinese chinese = new Chinese(name);
                chineseMapper.insertOrUpdate(chinese);
                chinese.setId(Objects.requireNonNull(chineseMapper.getChineseByName(name)).getId());
                List<ChineseStroke> chineseStrokeList = new ArrayList<>();
                for (int i = 0; i < strokes.length; ++i) {
                    if (Objects.nonNull(strokes[i]) && !strokes[i].equals(" ")) {
                        if (!chineseOrderService.containStrokeByName(strokes[i])) {
                            Stroke stroke = new Stroke();
                            stroke.setName(strokes[i]);
                            stroke.setCategory(0);
                            strokeMapper.insertOrUpdate(stroke);
                        }
                        ChineseStroke chineseStroke = new ChineseStroke(Objects.requireNonNull(chinese).getId(), Objects.requireNonNull(strokeMapper.getStrokeByName(strokes[i])).getId(), i + 1);
                        chineseStrokeList.add(chineseStroke);
                    }
                }
                chineseStrokeMapper.batchInsert(chineseStrokeList);
                document = null;
            }
        }
        System.out.println("导入完成");

    }

    public static String unicodeToString(String str) {

        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            str = str.replace(matcher.group(1), ch + "");
        }
        return str;
    }
}