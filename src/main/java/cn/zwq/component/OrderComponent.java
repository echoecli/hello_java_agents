package cn.zwq.component;

import cn.hutool.http.HttpRequest;
import cn.zwq.dto.OmsOrderDetailDto;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 订单查询工具 - 通过 MCP 协议对外暴露
 * 外部 AI 客户端可通过 MCP 协议调用这些工具
 */
@Component
@Slf4j
public class OrderComponent {

    @Tool(description = "根据订单编号获取订单详情")
    public OmsOrderDetailDto getOrderDetail(@ToolParam(description = "订单编号") String orderSn){
        String cookie = "cna=5b51418a2709f206ca8831605f4421f9; " +
                "_abfpc=2fb726183fc5e8581239b5351098f3e54bfcb5ef_2.0; " +
                "CUSTOMER_SESSION_TOKEN=48461977c7384785abe98a82971aad4e; " ;
        String body = HttpRequest.get("https://test123.com/order-service/order/" + orderSn)
                .header("cookie", cookie)
                .execute()
                .body();
        log.info("getOrderDetail body: {}", body);
        return JSONObject.parseObject(JSONObject.parseObject(body).getString("data"), OmsOrderDetailDto.class);
    }
}
