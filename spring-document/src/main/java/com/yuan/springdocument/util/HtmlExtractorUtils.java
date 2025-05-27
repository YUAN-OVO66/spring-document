package com.yuan.springdocument.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HtmlExtractorUtils {

    /**
     * 从HTML字符串中提取指定标签的内容
     *
     * @param html      HTML字符串
     * @param tagName   要提取的标签名
     * @return 包含标签内容的列表
     */
    public static List<String> extractTagContent(String html, String tagName) {
        List<String> result = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(tagName);

        for (Element element : elements) {
            result.add(element.text());
        }

        return result;
    }

    /**
     * 从HTML字符串中提取指定标签的HTML内容（包含标签本身）
     *
     * @param html      HTML字符串
     * @param tagName   要提取的标签名
     * @return 包含完整标签HTML的列表
     */
    public static List<String> extractTagHtml(String html, String tagName) {
        List<String> result = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(tagName);

        for (Element element : elements) {
            result.add(element.outerHtml());
        }

        return result;
    }

    /**
     * 从URL加载HTML并提取指定标签的内容
     *
     * @param url       网页URL
     * @param tagName   要提取的标签名
     * @param timeout   超时时间（毫秒）
     * @return 包含标签内容的列表
     * @throws IOException 如果加载网页时发生错误
     */
    public static List<String> extractTagContentFromUrl(String url, String tagName, int timeout) throws IOException {
        List<String> result = new ArrayList<>();
        Document doc = Jsoup.connect(url).timeout(timeout).get();
        Elements elements = doc.select(tagName);

        for (Element element : elements) {
            result.add(element.text());
        }

        return result;
    }

    /**
     * 从HTML字符串中提取指定标签的特定属性值
     *
     * @param html      HTML字符串
     * @param tagName   要提取的标签名
     * @param attrName  要提取的属性名
     * @return 包含属性值的列表
     */
    public static List<String> extractTagAttribute(String html, String tagName, String attrName) {
        List<String> result = new ArrayList<>();
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(tagName);

        for (Element element : elements) {
            String attrValue = element.attr(attrName);
            if (!attrValue.isEmpty()) {
                result.add(attrValue);
            }
        }

        return result;
    }

    /**
     * 从HTML字符串中提取指定class的内容
     *
     * @param html      HTML字符串
     * @param className class名称
     * @return 包含class内容的列表
     */
    public static List<String> extractClassContent(String html, String className) {
        return extractTagContent(html, "." + className);
    }

    /**
     * 从HTML字符串中提取指定id的内容
     *
     * @param html      HTML字符串
     * @param id        id名称
     * @return 包含id内容的列表（通常只有一个元素）
     */
    public static List<String> extractIdContent(String html, String id) {
        return extractTagContent(html, "#" + id);
    }

}
