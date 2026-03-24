package cn.zwq.control;

import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * chapt 1 chat model
 *
 * @author  zhao weiqiang
 * @date    2026/3/25 00:34
 */

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
@SuppressWarnings("all")
public class ChatModelController {

    private final ChatModel chatModel;


    /**
     * 简单的向llm进行提问
     *
     * @param query
     * @return
     */
    @GetMapping("/ask")
    public String ask(@RequestParam String query) {
        return chatModel.call(query);
    }

    /**
     * 规定角色向llm提问
     *
     * @param query
     * @return
     */
    @GetMapping("/askByRole")
    public String askByRole(String query) {
        //系统级：规定llm角色
        SystemMessage systemMessage = new SystemMessage("你是一个王者荣耀资深高手，会各种职业级的对线经验");
        UserMessage userMessage = new UserMessage(query);
        return chatModel.call(systemMessage, userMessage);
    }

    /**
     * 小记：
     * prompt 提示词
     * 概念：是引导ai模型生成特定输出的输入格式。prompt的设计和措辞会影响模型生成的响应和内容
     *
     * import os
     * from openai import OpenAI
     *
     * try:
     *     client = OpenAI(
     *         # 若没有配置环境变量，请用阿里云百炼API Key将下行替换为: api_key="sk-xxx",
     *         api_key=os.getenv("DASHSCOPE_API_KEY"),
     *         base_url="https://dashscope.aliyuncs.com/compatible-mode/v1",
     *     )
     *
     *     completion = client.chat.completions.create(
     *         model="qwen-plus",  # 模型列表: https://help.aliyun.com/model-studio/getting-started/models
     *         messages=[
     *             {'role': 'system', 'content': 'You are a helpful assistant.'},
     *             {'role': 'user', 'content': '你是谁？'}
     *         ]
     *     )
     *     print(completion.choices[0].message.content)
     * except Exception as e:
     *     print(f"错误信息：{e}")
     *     print("请参考文档：https://help.aliyun.com/model-studio/developer-reference/error-code")
     *
     *
     *     其实你规定的模型，最大token数，角色，是system还是user 本质上从广义来说，都属于prompt
     *    {@link org.springframework.ai.chat.prompt.Prompt}
     *    所以，spring ai的prompt是有消息列表和对话选项组成的
     */



    /**
     * 提示词测试
     *
     * @param query
     * @return
     */
    @GetMapping("/prompt")
    public ChatResponse prompt(@RequestParam String query) {


        SystemMessage systemMessage = new SystemMessage("你是一个王者荣耀资深高手，会各种职业级的对线经验");
        UserMessage userMessage = new UserMessage(query);

        //对话选项
        DashScopeChatOptions chatOptions = new DashScopeChatOptions();
        chatOptions.setModel("qwen-plus");
        chatOptions.setTemperature(0.2);

        //提示词
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage), chatOptions);

        return chatModel.call(prompt);
    }


    /**
     * 流式响应
     * 类似于你去问deepseek，一个字一个字蹦出来那种。
     * @param query
     * @return
     */
    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam String query) {
        //系统级：规定llm角色
        SystemMessage systemMessage = new SystemMessage("你是一个王者荣耀资深高手，会各种职业级的对线经验");
        UserMessage userMessage = new UserMessage(query);
        return chatModel.stream(systemMessage, userMessage);
    }

}
