package cn.zwq.control;


import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * chapt 2 chat client
 *
 * 相当于在 chatmodel上又做了一层封装。功能更多，更灵活
 * @author  zhao weiqiang
 * @date    2026/3/25 00:42
 */

@RestController
@RequestMapping("/chatClient")
@SuppressWarnings("all")
public class ChatClientController {

    private final ChatClient chatClient;


    public ChatClientController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * 把chat model control 里的方法封装了一下
     * @param query
     * @return
     */
    @GetMapping("/ask")
    public String ask(@RequestParam String query) {

        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        return chatClient.prompt()
                .system("你是一个王者荣耀资深高手，会各种职业级的对线经验")
                .user(query)
                .options(chatOptions)
                .call()
                .content();
    }
}
