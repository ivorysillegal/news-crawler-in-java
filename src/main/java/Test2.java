import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class Test2 {

    public static void main(String args[]) {

        Workbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet("NewsData");

        // 网易新闻
        String url = "https://www.163.com/search?keyword=%E5%8F%8D%E8%AF%88%E9%AA%97";
        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        所有的东西都暂时存在document中了


        // 文字新闻
        Elements texts = document.getElementsByClass("cm_ul_round");
        for (Element e : texts) {
            Elements tags = e.getElementsByTag("a");
            for (Element tag : tags) {
                // 标题
                String title = tag.getElementsByAttribute("href").text();
                // 链接地址，可以根据需求继续解析网址，获取新闻详细信息
                String href = tag.attributes().get("href");
                // 所属分类
                String classification = null;

                // 创建一行
                Row row = sheet.createRow(sheet.getLastRowNum() + 1);

                // 创建单元格并设置数据
                Cell cellTitle = row.createCell(0);
                cellTitle.setCellValue(title);

                Cell cellHref = row.createCell(1);
                cellHref.setCellValue(href);

                Cell cellClassification = row.createCell(2);
                cellClassification.setCellValue(classification);

                if (href.contains("?") && href.contains("clickfrom=w_")) {
                    classification = href.substring(href.lastIndexOf("?") + 1).replace("clickfrom=w_", "");
                }
                System.out.println(title);
                System.out.println(href);
                System.out.println(classification);
            }
        }

        try (FileOutputStream fileOut = new FileOutputStream("news_data.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Data written to Excel file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 图片新闻
        Elements imgs = document.getElementsByClass("cm_bigimg");
        for (Element img : imgs) {
            Elements photos = img.getElementsByClass("photo");
            for (Element photo : photos) {
                // 标题
                String title = photo.attributes().get("title");
                // 链接地址，可以根据需求继续解析网址，获取新闻详细信息
                String href = photo.attributes().get("href");
                // 封面图
                String imgSrc = null;
                List<Node> child = photo.childNodes();
                for(Node node : child) {
                    if (node.hasAttr("data-original")) {
                        imgSrc = node.attributes().get("data-original");
                        break;
                    }
                }
                // 所属分类
                String classification = null;
                if (href.contains("?") && href.contains("clickfrom=w_")) {
                    classification = href.substring(href.lastIndexOf("?") + 1).replace("clickfrom=w_", "");
                }
                System.out.println(title);
                System.out.println(href);
                System.out.println(imgSrc);
                System.out.println(classification);
            }
        }
    }
}