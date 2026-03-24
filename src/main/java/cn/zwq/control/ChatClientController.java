package cn.zwq.control;


import cn.zwq.dto.MentalHealthRiskDto;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;


/**
 * chapt 2 chat client
 *
 * 相当于在 chatmodel上又做了一层封装。功能更多，更灵活
 * @author  zhao weiqiang
 * @date    2026/3/25 01:16
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


    /**
     * 流式返回
     * @param query
     * @return
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam String query) {

        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        return chatClient.prompt()
                .system("你是一个王者荣耀资深高手，会各种职业级的对线经验")
                .user(query)
                .options(chatOptions)
                .stream()
                .content();
    }


    /**
     * 狠狠当一回心理老师
     *
     * query示例：老师，我有一些自残行为，我觉得这样很爽，我是m吗
     *
     * 我好想死，我觉得活着没有意义，老师你能帮帮我解脱吗，活着太压抑了
     *
     * 老师，我总想欺负我一个叫申昌昊的同事，只要欺负他，就会有莫名的爽感
     * @param query
     * @return
     */
    @GetMapping("/mentalHealthTest")
    public MentalHealthRiskDto mentalHealthAssess(@RequestParam String query) {

        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        String systemPrompt = """
                你是一名专业的心理健康评估专家。
                                
                【任务】
                根据用户描述进行风险评估，并给患者推荐散心的旅游城市，返回严格的 JSON 格式：
                {"riskLevel":"等级","suggest":"建议","city":"城市"}
                                
                【风险等级标准】
                - 高风险：出现想死/自杀/不想活等危险词汇
                - 中风险：出现自残等伤害自己身体或不爱护自己和他人的行为
                - 低风险：问题不严重，一般情绪波动
                
                【建议标准】
                根据患者的风险给出简短的建议。
                
                【城市标准】
                根据患者的病况，给出推荐旅游的城市。
                                
                【输出要求】
                - suggest：≤10 个汉字
                - city：仅城市名
                - 纯 JSON，无其他文字
                """;

        return chatClient.prompt()
                .system(systemPrompt)
                .user("请评估以下用户的心理健康状况：" + query)
                .options(chatOptions)
                .call()
                .entity(MentalHealthRiskDto.class);
    }



}
