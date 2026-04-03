package cn.zwq.config;

import cn.zwq.component.OrderComponent;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置mcp工具对外提供
 */
@Configuration
public class McpConfig {
    @Bean
    public ToolCallbackProvider getOrderDetail(OrderComponent orderComponent){
        return MethodToolCallbackProvider.builder()
                .toolObjects(orderComponent)
                .build();
    }
}
