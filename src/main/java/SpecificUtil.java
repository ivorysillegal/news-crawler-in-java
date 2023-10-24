import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SpecificUtil {

    public static String specificNews(String url) {
        // 对应文章的url
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            System.out.println("error happen");
            return  null;
        }
//        所有的东西都暂时存在document中了

        String msg = "";
        // 文字新闻
//        每一个post_body是一篇文章
        Elements texts = document.getElementsByClass("post_body");
        for (Element text : texts) {
            Elements p = text.getElementsByTag("p");
            for (Element element : p) {
//                获取图片原链接
                Elements img = element.getElementsByClass("f_center");
                for (Element element1 : img) {
                    Elements img1 = element1.getElementsByTag("img");
                    for (Element element2 : img1) {
                        String src = element2.attr("src");
                        msg+= src;
                        msg += "\n";
                    }
                }
                msg += element.text();
                msg += "\n";
            }
        }
        return msg;
    }

}
