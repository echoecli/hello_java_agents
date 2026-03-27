package cn.zwq.control;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
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

        this.chatClient = builder
                .defaultAdvisors(retrievalAugmentationAdvisor)
                .build();
    }

    /**
     * 向量化测试
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

    @GetMapping(value = "/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .system("你是和信证券的人事经理，需要用诙谐幽默的语气，并添加一点小表情，回答用户的问题")
                .user(message)
                .call()
                .content();
    }
}
