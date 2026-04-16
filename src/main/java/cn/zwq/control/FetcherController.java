package cn.zwq.control;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fetcher")
@SuppressWarnings("all")
public class FetcherController {

    private final ChatClient chatClient;

    public FetcherController(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                /**
                 * 存储最大消息数
                 */
                .maxMessages(20)
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();

        /**
         * 官方自带的消息存储Advisor
         * @see MessageChatMemoryAdvisor
         */
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(memory)
                .build();
        this.chatClient = builder
                .defaultAdvisors(chatMemoryAdvisor)
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    /**
     * 爬虫mcp server接入测试
     * @param query
     * @return
     */
    @GetMapping("/test")
    public String test(@RequestParam String query,
                       @RequestParam String conversationId) {


        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        return chatClient.prompt()
                .system("你是一个网页爬虫高手，你可以运用工具爬取网页上的数据并进行总结")
                .user(query)
                //在这里把会话id放到上下文
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .options(chatOptions)
                .call()
                .content();

    }
}
