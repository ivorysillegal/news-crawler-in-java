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


public class Test {

    public static void main(String[] args) {

        Workbook workbook = new XSSFWorkbook();
        // 创建一个工作表
        Sheet sheet = workbook.createSheet("NewsData");

        // 仅用于网易新闻搜索页面
//        可搜索处页面新闻的概览图 新闻标题 及 新闻主体详细内容 新闻主体配图
        String url = "https://www.163.com/search?keyword=%E8%AF%88%E9%AA%97";

        Document document = null;
        try {
            document = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        所有的东西都暂时存在document中了

        int k = 0;
//        k为计数器 记录目前爬了多少条新闻下来

        Elements texts = document.getElementsByClass("keyword_new");
//        keyword_new 为每一个新闻概览页面每一个class页面的css样式

//        每一个e是每一条新闻
        for (Element singleNews : texts) {
//                每一次输入值的时候 首先定位到工作表新一行位置
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

//            h3为每一条新闻的标题位置
            Elements h3 = singleNews.getElementsByTag("h3");

//            keyword_img 为每一个图class的css样式
            Elements img = singleNews.getElementsByClass("keyword_img");

//            keyword_time 为每一个新闻的时间信息
            Elements time = singleNews.getElementsByClass("keyword_time");

//                如果没有图 就直接空一行
            if (img.size() == 0) {
                Cell cellImgSrc = row.createCell(0);
                cellImgSrc.setCellValue("\n");

            } else {
                for (Element element : img) {
//                    找出图的标签
                    Elements img1 = element.getElementsByTag("img");
                    for (Element element1 : img1) {
                        String imgSrc = element1.attr("src");
//                        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
                        Cell cellImgSrc = row.createCell(0);
                        cellImgSrc.setCellValue(imgSrc);
                    }
                    break;
//                    确保每条新闻只爬一张概览图（有的新闻有多张）
                }
            }

            for (Element timeMsg : time) {
                String date = timeMsg.text();
                Cell cellDate = row.createCell(3);
                cellDate.setCellValue(date);
            }

            for (Element element : h3) {
                Elements tags = element.getElementsByTag("a");
                for (Element tag : tags) {
                    // 标题
                    String title = tag.getElementsByAttribute("href").text();
                    // 链接地址，可以根据需求继续解析网址，获取新闻详细信息
                    String href = tag.attributes().get("href");

                    String news = Test3.specificNews(href);

                    // 创建一行
//                    Row row = sheet.createRow(sheet.getLastRowNum() + 1);

                    // 创建单元格并设置数据
                    Cell cellTitle = row.createCell(1);
                    cellTitle.setCellValue(title);

//                        Cell cellHref = row.createCell(2);
//                        cellHref.setCellValue(href);

                    Cell cellNews = row.createCell(2);
                    cellNews.setCellValue(news);
                }
            }

            System.out.println("finish " + k++);
        }

        System.out.println("正文数据爬取完毕");


        try (FileOutputStream fileOut = new FileOutputStream("news_data.xlsx")) {
            workbook.write(fileOut);
            System.out.println("Data written to Excel file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}