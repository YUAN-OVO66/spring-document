package com.yuan.springdocument.converter;

import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Component
public class HtmlToWordConverter {

    private static TemplateEngine templateEngine = new TemplateEngine();

    public HtmlToWordConverter(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * 将 HTML 模板渲染为 Word 文件并保存
     *
     * @param templateName Thymeleaf 模板名（不带后缀）
     * @param data         模板上下文数据
     * @param outputPath   输出文件路径（.docx）
     * @throws Exception 转换失败时抛出异常
     * @return 转换后的文件路径
     */
    public static String convertHtmlToWord(String templateName, Map<String, Object> data, String outputPath) throws Exception {
        // 渲染 HTML
        Context context = new Context();
        context.setVariables(data);
        String renderedHtml = templateEngine.process(templateName, context);

        // 转换为 Word
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(importer.convert(renderedHtml, null));

        // 保存文件
        File file = new File(outputPath);
        wordMLPackage.save(file);

        return file.getAbsolutePath();
    }


    /**
     * 读取 HTML 文件并转换为 Word 文件
     *
     * @param htmlFilePath HTML 文件路径
     * @param outputPath   输出 Word 文件路径（.docx）
     * @throws Exception 若转换失败抛出异常
     * @return 转换后的 Word 文件路径
     */
    public static String convertHtmlFileToWord(String htmlFilePath, String outputPath) throws Exception {
        // 读取 HTML 文件内容为字符串
        String htmlContent = readHtmlFile(htmlFilePath);

        // 创建 Word 包并导入 HTML
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(importer.convert(htmlContent, null));

        // 保存 Word 文件
        File outputFile = new File(outputPath);
        wordMLPackage.save(outputFile);
        return outputFile.getAbsolutePath();
    }

    /**
     * 读取 HTML 文件内容
     *
     * @param htmlFilePath 文件路径
     * @return HTML 字符串内容
     * @throws IOException 读取失败抛出异常
     */
    private static String readHtmlFile(String htmlFilePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(htmlFilePath)), "UTF-8");
    }



}
