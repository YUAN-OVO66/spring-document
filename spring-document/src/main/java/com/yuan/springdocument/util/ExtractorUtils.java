package com.yuan.springdocument.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractorUtils {

    /**
     * 从文本中提取所有代码块
     * @param text 包含代码块的文本
     * @return 代码块列表，每个代码块包含语言和内容
     */
    public static List<CodeBlock> extractAllCodeBlocks(String text) {
        List<CodeBlock> codeBlocks = new ArrayList<>();

        // 匹配Markdown风格的代码块 (```language ... ```)
        Pattern mdPattern = Pattern.compile("```(\\w*)\\n([\\s\\S]*?)\\n```");
        Matcher mdMatcher = mdPattern.matcher(text);

        while (mdMatcher.find()) {
            String language = mdMatcher.group(1).trim();
            String content = mdMatcher.group(2).trim();
            codeBlocks.add(new CodeBlock(language, content));
        }

        // 如果没有找到Markdown代码块，尝试匹配缩进代码块
        if (codeBlocks.isEmpty()) {
            Pattern indentPattern = Pattern.compile("(?m)^( {4,}|\t+)(.*)$");
            Matcher indentMatcher = indentPattern.matcher(text);
            StringBuilder codeBuilder = new StringBuilder();
            boolean inCodeBlock = false;

            while (indentMatcher.find()) {
                if (!inCodeBlock) {
                    inCodeBlock = true;
                }
                codeBuilder.append(indentMatcher.group(2)).append("\n");
            }

            if (inCodeBlock) {
                codeBlocks.add(new CodeBlock("", codeBuilder.toString().trim()));
            }
        }

        return codeBlocks;
    }

    /**
     * 提取第一个代码块的内容
     * @param text 包含代码块的文本
     * @return 第一个代码块的内容，如果没有则返回空字符串
     */
    public static String extractFirstCodeContent(String text) {
        List<CodeBlock> blocks = extractAllCodeBlocks(text);
        return blocks.isEmpty() ? "" : blocks.get(0).getContent();
    }

    /**
     * 提取指定语言的第一个代码块
     * @param text 包含代码块的文本
     * @param language 目标语言(如java, python等)
     * @return 匹配的代码块内容，如果没有则返回空字符串
     */
    public static String extractCodeByLanguage(String text, String language) {
        List<CodeBlock> blocks = extractAllCodeBlocks(text);
        for (CodeBlock block : blocks) {
            if (block.getLanguage().equalsIgnoreCase(language)) {
                return block.getContent();
            }
        }
        return "";
    }

    /**
     * 代码块数据类
     */
    public static class CodeBlock {
        private final String language;
        private final String content;

        public CodeBlock(String language, String content) {
            this.language = language;
            this.content = content;
        }

        public String getLanguage() {
            return language;
        }

        public String getContent() {
            return content;
        }

        @Override
        public String toString() {
            return "Language: " + language + "\nContent:\n" + content;
        }
    }
}
