package com.yuan.springdocument;

import com.yuan.springdocument.converter.HtmlToWordConverter;
import com.yuan.springdocument.service.DocumentServiceImpl;
import com.yuan.springdocument.util.HtmlExtractorUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringDocumentApplicationTests {

    @Autowired
    private DocumentServiceImpl  documentService;

    @Test
    void test(){
        String context = "VUE-JavaScript 框架易学易用，性能出色，适用场景丰富的 Web 前端框架。";
        String prompt ="请你根据"+context+"撰写一篇公众号内容";
        documentService.documentGeneration(prompt);
    }

}
