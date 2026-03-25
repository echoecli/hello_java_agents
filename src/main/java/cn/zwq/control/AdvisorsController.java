package cn.zwq.control;


import cn.zwq.advisor.LogAdvisor;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * chapt 3 advisors
 * 类似于AOP 在请求llm之前和之后 进行增强
 * <p>
 * 优势：
 * 将常见的生成式 AI 模式（如对话记忆、敏感词过滤、RAG 检索）打包成可重用单元，简化开发流程
 * 创建可跨不同模型和用例工作的可重用转换组件，提升代码灵活性
 *
 * @author zhao weiqiang
 * @date 2026/3/25 01:30
 */

@RestController
@RequestMapping("/advisors")
@SuppressWarnings("all")
public class AdvisorsController {

    private final ChatClient chatClient;
    private final LogAdvisor logAdvisor;

    public AdvisorsController(ChatClient.Builder builder, LogAdvisor logAdvisor) {
        this.chatClient = builder
                .build();
        this.logAdvisor = logAdvisor;
    }

    /**
     * advisor 测试
     * @param query
     * @return
     */
    @GetMapping("/stream")
    public String stream(@RequestParam String query) {
        System.err.println("=== Controller.stream() called ===");
        System.err.println("LogAdvisor instance: " + logAdvisor);
        System.err.println("LogAdvisor class: " + logAdvisor.getClass().getName());

        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        return chatClient.prompt()
                .system("你是一个王者荣耀资深高手，会各种职业级的对线经验")
                .user(query)
                .advisors(logAdvisor)
                .options(chatOptions)
                .call()
                .content();
    }
}
