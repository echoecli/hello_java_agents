//package cn.zwq.control;
//
//import cn.zwq.util.ai.DateUtil;
//import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.tool.ToolCallbackProvider;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/mcp")
//public class McpController {
//
//    private final ChatClient chatClient;
//
//    public McpController(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
//        this.chatClient = builder
//                .defaultToolCallbacks(toolCallbackProvider)
//                .build();
//
//    }
//    @GetMapping("/ask")
//    public String ask(@RequestParam String query) {
//
//        //对话选项
//        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
//        chatOptions.setModel("qwen-plus");
//        chatOptions.setTemperature(0.2);
//
//        return chatClient.prompt()
//                .system("你是一个和信订单智能客服助手，你需要帮助用户解决和信订单相关的问题")
//                .user(query)
//                .options(chatOptions)
//                .call()
//                .content();
//    }
//}
