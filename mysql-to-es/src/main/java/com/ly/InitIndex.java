package com.ly;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

public class InitIndex {
    public static void main(String[] args) throws IOException {
        RestHighLevelClient client = ESClient.getRestHighLevelClient();

        // 1、创建 创建索引request 参数：索引名mess
        CreateIndexRequest indexRequest = new CreateIndexRequest("mess");

        // 2、设置索引的settings
//        indexRequest.settings(Settings.builder().put("index.number_of_shards", 3) // 分片数
//                .put("index.number_of_replicas", 2) // 副本数
//                .put("analysis.analyzer.default.tokenizer", "ik_smart") // 默认分词器
//        );

        // 3、设置索引的mappings
        indexRequest.mapping("_doc",
                "  {\n" +
                        "    \"_doc\": {\n" +
                        "      \"properties\": {\n" +
                        "        \"message\": {\n" +
                        "          \"type\": \"text\"\n" +
                        "        }\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }",
                XContentType.JSON);

        // 4、 设置索引的别名
//        indexRequest.alias(new Alias("mmm"));

        // 5、 发送请求
        // 5.1 同步方式发送请求
        CreateIndexResponse createIndexResponse = client.indices()
                .create(indexRequest, RequestOptions.DEFAULT);

        // 6、处理响应
        boolean acknowledged = createIndexResponse.isAcknowledged();
        boolean shardsAcknowledged = createIndexResponse
                .isShardsAcknowledged();
        System.out.println("acknowledged = " + acknowledged);
        System.out.println("shardsAcknowledged = " + shardsAcknowledged);

        // 5.1 异步方式发送请求
            /*ActionListener<CreateIndexResponse> listener = new ActionListener<CreateIndexResponse>() {
                @Override
                public void onResponse(
                        CreateIndexResponse createIndexResponse) {
                    // 6、处理响应
                    boolean acknowledged = createIndexResponse.isAcknowledged();
                    boolean shardsAcknowledged = createIndexResponse
                            .isShardsAcknowledged();
                    System.out.println("acknowledged = " + acknowledged);
                    System.out.println(
                            "shardsAcknowledged = " + shardsAcknowledged);
                }

                @Override
                public void onFailure(Exception e) {
                    System.out.println("创建索引异常：" + e.getMessage());
                }
            };

            client.indices().createAsync(indexRequest, listener);
            */
    }
}
