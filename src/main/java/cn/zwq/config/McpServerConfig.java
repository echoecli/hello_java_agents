package cn.zwq.config;

import cn.zwq.component.OrderComponent;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * MCP Server 配置
 * 将 OrderComponent 的工具通过 MCP 协议暴露给外部 AI 客户端
 * 
 * 依赖: spring-ai-starter-mcp-server-webflux
 * 端点:
 */
@Configuration
public class McpServerConfig {

    /**
     * Spring AI 1.0.0 版本的 MCP Server 自动配置会自动检测 ToolCallbackProvider Bean 并暴露为 MCP 工具
     */
    @Bean
    public MethodToolCallbackProvider orderToolCallbackProvider(OrderComponent orderComponent) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(orderComponent)
                .build();
    }
}
