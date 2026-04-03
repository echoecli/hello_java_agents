package cn.zwq.control;

import cn.hutool.db.sql.Order;
import cn.zwq.component.OrderComponent;
import cn.zwq.util.ai.DateUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * tool calling
 */
@RestController
@RequestMapping("/toolCalling")
public class ToolCallingController {

    private final OrderComponent orderComponent;

    private final ChatClient chatClient;


    public ToolCallingController(OrderComponent orderComponent, ChatClient.Builder builder) {
        this.orderComponent = orderComponent;
        this.chatClient = builder.build();
    }

    /**
     *
     * @param query 问题示例：巅峰赛还有多久开始
     * @return
     * 当前时间是：**2026年3月30日 16:34:13（北京时间）**
     * 巅峰赛今日开放时段为 **18:00–24:00**（常规设定，除非官方临时调整）。
     *
     * ✅ 所以距离巅峰赛开启还有：
     * **18:00 − 16:34:13 = 1小时25分47秒**
     *
     * ⚠️ 温馨提示：
     * - 巅峰赛需达到 **最强王者段位 + 1200分以上** 才可进入；
     * - 每日首次进入需消耗 **10点巅峰积分**（胜+20，败+10，平局+15）；
     * - 若你已满1200分但无法进入，请检查是否处于「巅峰赛禁赛期」（如举报违规、挂机处罚等）。
     *
     * 需要我帮你规划今晚巅峰赛的英雄池/阵容搭配/冲分节奏吗？🙂
     */
    @GetMapping("/ask")
    public String ask(@RequestParam String query) {

        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        return chatClient.prompt()
                .system("你是一个王者荣耀资深高手，会各种职业级的对线经验")
                //给ai时间工具类
                .tools(new DateUtil())
                .user(query)
                .options(chatOptions)
                .call()
                .content();
    }

    /**
     * 接了一个查询其他项目的订单详情功能
     * @param query 问题
     */
    @GetMapping("/ask2")
    public String ask2(@RequestParam String query) {

        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        return chatClient.prompt()
                .system("你是订单智能客服助手")
                .user(query)
                .tools(orderComponent)
                .options(chatOptions)
                .call()
                .content();
    }
}
