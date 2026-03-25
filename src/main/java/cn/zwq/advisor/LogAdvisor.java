package cn.zwq.advisor;

import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.stereotype.Component;

/**
 * 日志 advisor
 *
 * 这玩意 不就是aop吗？
 */
@Component
public class LogAdvisor implements CallAdvisor {

    @Override
    public String getName() {
        return "LogAdvisor";
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        before();
        ChatClientResponse response = callAdvisorChain.nextCall(chatClientRequest);
        after();
        return response;
    }

    /**
     * chat client可以设置多个advisor，用此方法调整顺序 ，不试了没意义
     */
    @Override
    public int getOrder() {
        return 0;
    }

    private void before(){
        System.err.println("=== LogAdvisor BEFORE ===");
    }
    private void after(){
        System.err.println("=== LogAdvisor AFTER ===");
    }
}
