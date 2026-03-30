package cn.zwq.util.ai;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {


    /**
     * @Tool 和 @ToolParam 是 Spring AI 提供的注解，用于定义工具和工具参数 给ai调用
     */
    @Tool(description = "通过时区id获取当前时间")
    public String getCurrentDate(@ToolParam(description = "时区id，比如Asia/Shanghai") String zoneId) {
        ZoneId zid = ZoneId.of(zoneId);
        ZonedDateTime now = ZonedDateTime.now(zid);
        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
