package com.yuan.springdocument.service;

import com.yuan.springdocument.config.PromptConfig;
import com.yuan.springdocument.converter.HtmlToWordConverter;
import com.yuan.springdocument.util.ExtractorUtils;
import com.yuan.springdocument.util.HtmlExtractorUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class DocumentServiceImpl  implements DocumentService{

    private ChatClient chatClient;

    @Autowired
    public DocumentServiceImpl(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .build();
    }

    @Override
    public File documentGeneration(String context) {

        // 使用API调用聊天
        String content = chatClient.prompt(PromptConfig.PROMPT_TEMPLATE)
                .user(context)
                .call()
                .content();
        String element = ExtractorUtils.extractCodeByLanguage(content, "html");
        if (element.isEmpty())
            throw new RuntimeException("生成失败");
        String title = HtmlExtractorUtils.extractTagContent(element, "title").get(0);
        System.out.println(element);
        System.out.println(title);
        //创建一个临时的html文件并且写入内容
        File tempFile = new File("temp.html");
        try {
            tempFile.createNewFile();
            FileUtils.writeStringToFile(tempFile, element, "utf-8");
            HtmlToWordConverter.convertHtmlFileToWord(tempFile.getAbsolutePath(), title+".docx");
            tempFile.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}
