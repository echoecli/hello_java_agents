package cn.zwq.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 图生视频Controller
 * 基于阿里云百炼万相模型
 *
 * @author zhao weiqiang
 * @date 2026/3/25
 */
@Slf4j
@RestController
@RequestMapping("/video")
@RequiredArgsConstructor
public class ImageToVideoController {

    @Value("${video.generation.api-key}")
    private String apiKey;

    @Value("${video.generation.base-url:https://dashscope.aliyuncs.com/api/v1/services/aigc/video-generation/video-synthesis}")
    private String baseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 提交图生视频任务（文件上传方式）
     * <p>
     * 模型说明：
     * - wan2.6-i2v-flash: 最新模型，效果最佳，支持5秒或10秒（默认）
     * - wan2.5-i2v-preview: 稳定版本，支持5秒或10秒
     * <p>
     * 时长限制：阿里云百炼图生视频目前最长支持10秒，超出会被自动限制
     *
     * @param image    图片文件
     * @param prompt   提示词
     * @param model    模型名称（可选，默认wan2.6-i2v-flash）
     * @param duration 视频时长（可选，默认10秒，可选值：5、10）
     * @return 任务ID
     */
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> generateVideo(
            @RequestParam("image") MultipartFile image,
            @RequestParam String prompt,
            @RequestParam(required = false, defaultValue = "wan2.6-i2v-flash") String model,
            @RequestParam(required = false) String resolution,
            @RequestParam(required = false) Integer duration) {

        // 校验并修正时长参数
        if (duration == null) {
            duration = 10; // 默认10秒
        } else if (duration < 5) {
            duration = 5;
        } else if (duration > 10) {
            log.warn("请求的时长{}秒超过最大限制，已自动调整为10秒", duration);
            duration = 10;
        }

        log.info("接收到图生视频请求，model: {}, 时长: {}秒, 文件名: {}", model, duration, image.getOriginalFilename());

        // 将图片转为Base64
        String base64Image;
        try {
            byte[] imageBytes = image.getBytes();
            String base64Data = Base64.getEncoder().encodeToString(imageBytes);
            // 构建data URI格式
            String mimeType = image.getContentType() != null ? image.getContentType() : "image/jpeg";
            base64Image = "data:" + mimeType + ";base64," + base64Data;
        } catch (IOException e) {
            log.error("图片文件读取失败", e);
            return ResponseEntity.badRequest().body(Map.of("error", "图片文件读取失败", "message", e.getMessage()));
        }

        // 构建请求体
        Map<String, Object> input = new HashMap<>();
        input.put("prompt", prompt);
        input.put("img_url", base64Image);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("prompt_extend", true);
        if (resolution != null) {
            parameters.put("resolution", resolution);
        }
        if (duration != null) {
            parameters.put("duration", duration);
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", model);
        requestBody.put("input", input);
        requestBody.put("parameters", parameters);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);
        headers.set("X-DashScope-Async", "enable");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    baseUrl,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Map<String, Object> output = (Map<String, Object>) body.get("output");
                if (output != null) {
                    String taskId = (String) output.get("task_id");
                    log.info("图生视频任务创建成功，taskId: {}", taskId);

                    Map<String, Object> result = new HashMap<>();
                    result.put("taskId", taskId);
                    result.put("model", model);
                    result.put("duration", duration);
                    result.put("message", "任务创建成功");
                    result.put("note", "阿里云百炼图生视频最长支持10秒，视频将带有AI水印");
                    return ResponseEntity.ok(result);
                }
            }

            log.error("图生视频任务创建失败，响应: {}", response.getBody());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "任务创建失败", "detail", response.getBody()));

        } catch (Exception e) {
            log.error("图生视频任务创建异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "任务创建异常", "message", e.getMessage()));
        }
    }

    /**
     * 查询视频生成任务结果
     *
     * @param taskId 任务ID
     * @return 任务状态和结果
     */
    @GetMapping("/result/{taskId}")
    public ResponseEntity<?> getVideoResult(@PathVariable String taskId) {
        log.info("查询视频生成结果，taskId: {}", taskId);

        String queryUrl = baseUrl + "/" + taskId;

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    queryUrl,
                    HttpMethod.GET,
                    entity,
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Map<String, Object> output = (Map<String, Object>) body.get("output");

                if (output != null) {
                    String taskStatus = (String) output.get("task_status");

                    Map<String, Object> result = new HashMap<>();
                    result.put("taskId", taskId);
                    result.put("status", taskStatus);

                    // PENDING、RUNNING、SUCCEEDED、FAILED
                    switch (taskStatus) {
                        case "SUCCEEDED":
                            Map<String, Object> videoResult = (Map<String, Object>) output.get("video_url");
                            if (videoResult != null) {
                                result.put("videoUrl", videoResult.get("video_url"));
                                result.put("videoUrlExpire", videoResult.get("expire_time"));
                            }
                            result.put("message", "视频生成成功");
                            break;
                        case "FAILED":
                            result.put("message", "视频生成失败");
                            Map<String, Object> error = (Map<String, Object>) output.get("error");
                            if (error != null) {
                                result.put("errorCode", error.get("code"));
                                result.put("errorMessage", error.get("message"));
                            }
                            break;
                        case "PENDING":
                            result.put("message", "任务等待中");
                            break;
                        case "RUNNING":
                            result.put("message", "视频生成中");
                            break;
                        default:
                            result.put("message", "未知状态");
                    }

                    return ResponseEntity.ok(result);
                }
            }

            log.error("查询视频结果失败，响应: {}", response.getBody());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询失败", "detail", response.getBody()));

        } catch (Exception e) {
            log.error("查询视频结果异常", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "查询异常", "message", e.getMessage()));
        }
    }
}
