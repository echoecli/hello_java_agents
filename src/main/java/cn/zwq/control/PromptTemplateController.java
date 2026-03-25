package cn.zwq.control;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * PromptTemplate 控制器
 * 演示如何使用 PromptTemplate 和 SystemPromptTemplate 渲染提示词
 *
 * @author zhao weiqiang
 * @date 2026/3/25 16:42
 */
@RestController
@RequestMapping("/prompt-template")
@SuppressWarnings("all")
public class PromptTemplateController {

    /**
     * 渲染用户提示词
     * @param name 助手名称
     * @param voice 语音风格
     * @param userQuestion 用户问题
     * @return 渲染后的提示词
     */
    @GetMapping("/render-user-prompt")
    public String renderUserPrompt(
            @RequestParam(defaultValue = "小白") String name,
            @RequestParam(defaultValue = "幽默") String voice,
            @RequestParam(defaultValue = "推荐上海的三个景点") String userQuestion) {

        // 创建用户提示词模板
        PromptTemplate userPrompt = new PromptTemplate(
                "你是一个有用的人工智能助手，名字是{name}请用{voice}的风格回答以下问题：{userQuestion}"
        );

        // 创建消息
        Message message = userPrompt.createMessage(
                Map.of("name", name, "voice", voice, "userQuestion", userQuestion)
        );

        System.out.println("=== 用户提示词 ===");
        System.out.println(message.getText());
        
        return message.getText();
    }

    /**
     * 渲染系统提示词
     * @param name 助手名称
     * @param voice 语音风格
     * @param userQuestion 用户问题
     * @return 渲染后的系统提示词
     */
    @GetMapping("/render-system-prompt")
    public String renderSystemPrompt(
            @RequestParam(defaultValue = "小白") String name,
            @RequestParam(defaultValue = "幽默") String voice,
            @RequestParam(defaultValue = "推荐上海的三个景点") String userQuestion) {

        // 创建系统提示词模板
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
                "你是一个有用的人工智能助手，名字是{name}请用{voice}的风格回答以下问题：{userQuestion}"
        );

        // 创建消息
        Message message = systemPromptTemplate.createMessage(
                Map.of("name", name, "voice", voice, "userQuestion", userQuestion)
        );

        System.out.println("=== 系统提示词 ===");
        System.out.println(message.getText());
        
        return message.getText();
    }

    /**
     * 同时渲染用户和系统提示词进行对比
     * @param name 助手名称
     * @param voice 语音风格
     * @param userQuestion 用户问题
     * @return 对比结果
     */
    @GetMapping("/compare-prompts")
    public String comparePrompts(
            @RequestParam(defaultValue = "小白") String name,
            @RequestParam(defaultValue = "幽默") String voice,
            @RequestParam(defaultValue = "推荐上海的三个景点") String userQuestion) {

        StringBuilder result = new StringBuilder();
        
        // 渲染用户提示词
        PromptTemplate userPrompt = new PromptTemplate(
                "你是一个有用的人工智能助手，名字是{name}请用{voice}的风格回答以下问题：{userQuestion}"
        );
        Message userMessage = userPrompt.createMessage(
                Map.of("name", name, "voice", voice, "userQuestion", userQuestion)
        );
        
        result.append("=== 用户提示词 ===\n");
        result.append(userMessage.toString()).append("\n\n");

        // 渲染系统提示词
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(
                "你是一个有用的人工智能助手，名字是{name}请用{voice}的风格回答以下问题：{userQuestion}"
        );
        Message systemMessage = systemPromptTemplate.createMessage(
                Map.of("name", name, "voice", voice, "userQuestion", userQuestion)
        );
        
        result.append("=== 系统提示词 ===\n");
        result.append(systemMessage.toString());

        System.out.println(result.toString());
        return result.toString();
    }
}