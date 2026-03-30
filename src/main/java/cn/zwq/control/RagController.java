package cn.zwq.control;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.document.Document;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 *  chapter 5 rag control
 *
 * 准备工作：
 *
 * 需要docker 安装一下 redis-stack ，支持向量化
 *
 * 先拉取最新镜像 docker pull redis/redis-stack:latest
 *
 * 启动容器 docker run -d --name redis-stack -p 6379:6379 -p 8001:8001 redis/redis-stack:latest
 *
 * 8001是redis-stack客户端
 *
 * 启动redis-stack docker start redis-stack
 *
 * 查看容器是否启动成功  docker ps -a 你要看port是否有值
 *
 * 查看容器命令 docker logs -f --tail redis-stack
 *
 * @author zhao weiqiang
 */
@RestController
@RequestMapping("/rag")
public class RagController {

    private final VectorStore vectorStore;

    private final ChatClient chatClient;

    public RagController(VectorStore vectorStore, ChatClient.Builder builder) {
        this.vectorStore = vectorStore;

        VectorStoreDocumentRetriever vectorStoreDocumentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .topK(3)
                .similarityThreshold(0.5)
                .build();

        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(vectorStoreDocumentRetriever)
                .build();

        /**
         * 上下文记忆advisor
         *
         * @see AdvisorsChatMemoryController 这里有写过
         */
        MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();

        MessageChatMemoryAdvisor chatMemoryAdvisor = MessageChatMemoryAdvisor
                .builder(messageWindowChatMemory)
                .build();


        this.chatClient = builder
                .defaultAdvisors(retrievalAugmentationAdvisor, chatMemoryAdvisor)
                .build();
    }

    /**
     * 数据向量化
     * 相当于 insert，其实就是把文本数据转成向量数据，并入库
     * @param text 文本内容
     */
    @GetMapping("/importData")
    public String importData(@RequestParam String text) {
        Document document = Document.builder().text(text).build();
        vectorStore.add(List.of(document));
        return "Import data successfully!";
    }

    /**
     * 向量相似度检索
     * 相当于 select，就是根据查询内容，从向量数据库中检索出相似度最高的向量数据
     * @param query 查询内容
     */
    @GetMapping("/search")
    public List<Document> search(@RequestParam String query) {
        SearchRequest build = SearchRequest.builder()
                .topK(2)
                .query(query)
                .build();

        return vectorStore.similaritySearch(build);
    }


    /**
     * llm rag链接本地向量数据库（redis-stack）
     * @param message 用户问题
     * @param conversationId 会话id
     */
    @GetMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam String message, @RequestParam String conversationId) {
        return chatClient.prompt()
                .system("你是郑州宜家机械有限公司的智能客服，请从专业的角度回答用户对售卖机器的问题")
                .user(message)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();
    }
}
