# Redis 向量存储配置指南

## 问题诊断

错误信息：`ERR unknown command FT._LIST`
原因：Redis 不支持 RediSearch 模块，无法执行向量搜索相关命令。

## 解决方案

### 方案一：使用 Redis Stack（推荐）

Redis Stack 是 Redis 的扩展版本，内置了 RediSearch、RedisJSON 等模块。

#### Windows 安装方式：

1. **Docker 方式（推荐）**：
```bash
docker run -d -p 6379:6379 redis/redis-stack-server:latest
```

2. **直接下载安装**：
- 访问：https://redis.io/docs/about/about-stack/
- 下载 Windows 版本
- 安装并启动 Redis Stack Server

#### 验证安装：
```bash
# 连接到 Redis
redis-cli

# 检查模块列表
MODULE LIST

# 应该能看到类似输出：
# 1) 1) "name"
#    2) "search"
#    3) "ver"
#    4) (integer) 999999
```

### 方案二：现有 Redis 添加 RediSearch 模块

如果你已经有 Redis 实例运行：

1. 下载 RediSearch 模块：https://github.com/RediSearch/RediSearch/releases
2. 在 redis.conf 中添加：
```
loadmodule /path/to/redisearch.so
```
3. 重启 Redis 服务

### 方案三：开发环境快速启动

使用 Docker Compose：

```yaml
# docker-compose.yml
version: '3.8'
services:
  redis-stack:
    image: redis/redis-stack:latest
    ports:
      - "6379:6379"
    volumes:
      - redis-data:/data

volumes:
  redis-data:
```

启动命令：
```bash
docker-compose up -d
```

## 项目配置检查

### 1. Maven 依赖 ✅
```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-vector-store-redis</artifactId>
</dependency>
```

### 2. Application 配置 ✅
```yaml
spring:
  ai:
    vectorstore:
      redis:
        initialize-schema: true
        prefix: zwq_rag_prefix
        index-name: zwq_rag_index
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
```

### 3. 环境检查工具 ✅
项目已包含 `RedisVectorStoreChecker` 类，启动时会自动检查：
- Redis 连接状态
- RediSearch 模块可用性
- 向量存储功能

## 启动验证

启动应用后查看日志：

```
=== Redis Vector Store 环境检查 ===
✅ Redis 连接成功
Redis 版本: 7.2.0
✅ RediSearch 模块已安装
RediSearch 版本: 999999
✅ FT._LIST 命令可用
✅ VectorStore Bean 已成功创建: RedisVectorStore
=== 检查完成 ===
```

## 常见问题

### Q: 仍然出现 FT._LIST 错误怎么办？
A: 确认使用的是 Redis Stack 而不是普通 Redis

### Q: Docker 容器启动失败？
A: 检查端口是否被占用：
```bash
netstat -an | findstr 6379
```

### Q: 如何测试向量搜索功能？
A: 可以使用以下测试代码：
```java
@Autowired
private VectorStore vectorStore;

// 添加文档
vectorStore.add(List.of(
    new Document("doc1", "这是第一个文档"),
    new Document("doc2", "这是第二个文档")
));

// 相似度搜索
List<Document> results = vectorStore.similaritySearch(
    SearchRequest.query("文档").withTopK(5)
);
```

## 性能优化建议

1. **索引配置**：
```yaml
spring:
  ai:
    vectorstore:
      redis:
        distance-type: COSINE  # COSINE, IP, L2
        top-k: 4
```

2. **连接池配置**：
```yaml
spring:
  data:
    redis:
      lettuce:
        pool:
          max-active: 20
          max-idle: 10
          min-idle: 2
```

3. **内存优化**：
- 根据向量维度调整 Redis 内存分配
- 考虑使用 Redis 持久化策略

## 参考资源

- Redis Stack 官方文档：https://redis.io/docs/about/about-stack/
- RediSearch 文档：https://redis.io/docs/interact/search-and-query/
- Spring AI Redis Vector Store：https://docs.spring.io/spring-ai/reference/api/vectordbs/redis.html