package cn.zwq.control;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ai.chat.client.advisor.api.BaseChatMemoryAdvisor;


/**
 * chapt 3 advisors
 *
 * 和上一个Control进行区分
 * @see AdvisorsController
 *
 * 这个是直接用spring-ai官方自带的上下文消息存储advisor
 *
 * @author zhao weiqiang
 * @date 2026/3/25 15:26
 */

@RestController
@RequestMapping("/advisorsChatMemory")
@SuppressWarnings("all")
public class AdvisorsChatMemoryController {

    private final ChatClient chatClient;

    public AdvisorsChatMemoryController(ChatClient.Builder builder) {
        MessageWindowChatMemory memory = MessageWindowChatMemory.builder()
                /**
                 * 存储最大消息数
                 */
                .maxMessages(20)
                /**
                 * 这个chatMemoryRepository 本质其实就是个ConcurrentHashMap
                 * @see InMemoryChatMemoryRepository chatMemoryStore
                 */
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();

        /**
         * 官方自带的消息存储Advisor
         * @see MessageChatMemoryAdvisor
         */
        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(memory)
                .build();
        /**
         * 简单看一下源码吧
         * @see MessageChatMemoryAdvisor before切面 51行
         *  this.getConversationId(chatClientRequest.context(), this.defaultConversationId);
         *  这个方法是取
         * @see BaseChatMemoryAdvisor
         * 上下文是否包含 chat_memory_conversation_id，为空取默认的conversationId，默认的conversationId
         *  @see MessageChatMemoryAdvisor 83行初始化为default
         * 其实我觉得这个玩意不应该写到构造器里边，应该写个Bean来配置
         */
        this.chatClient = builder
                .defaultAdvisors(chatMemoryAdvisor)
                .build();
    }

    /**
     * advisor 上下文消息存储测试
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
                .system("你是一个王者荣耀资深高手，会各种职业级的对线经验")
                .user(query)
                //在这里把会话id放到上下文
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .options(chatOptions)
                .call()
                .content();

    }
}
