package com.yuan.springdocument.service;



import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.convert.in.xhtml.XHTMLImporterImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.util.Map;

@Service
public class DocxExportService {

    @Autowired
    private TemplateEngine templateEngine;

    // 将 Thymeleaf 渲染的 HTML 转为 DOCX 文件并保存
    public void generateDocxFromTemplate(Map<String, Object> data, String templateName, String outputPath) throws Exception {
        // 1. 渲染 HTML
        Context context = new Context();
        context.setVariables(data);
        String renderedHtml = templateEngine.process(templateName, context);

        // 2. 用 docx4j 转换 HTML 为 docx
        WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
        XHTMLImporterImpl importer = new XHTMLImporterImpl(wordMLPackage);
        wordMLPackage.getMainDocumentPart().getContent().addAll(importer.convert(renderedHtml, null));

        // 3. 保存到本地
        File file = new File(outputPath);
        wordMLPackage.save(file);
    }
}
