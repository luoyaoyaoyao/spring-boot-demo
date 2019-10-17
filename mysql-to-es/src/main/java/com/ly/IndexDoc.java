package com.ly;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;

import java.io.IOException;

/**
 * Refer link: https://www.cnblogs.com/leeSmall/p/9218779.html
 */
public class IndexDoc {

    public static void main(String[] args) throws IOException {
        try (RestHighLevelClient client = ESClient.getRestHighLevelClient()) {

            // 1、创建索引请求
            IndexRequest request = new IndexRequest(
                    "mess",   //索引
                    "_doc",     // mapping type
                    "1");     //文档id

            // 2、准备文档数据
            request.source(MockData.getData());

            //4、发送请求
            IndexResponse indexResponse = null;
            try {
                // 同步方式
                indexResponse = client.index(request, RequestOptions.DEFAULT);
            } catch (ElasticsearchException e) {
                // 捕获，并处理异常
                //判断是否版本冲突、create但文档已存在冲突
                if (e.status() == RestStatus.CONFLICT) {
                    System.out.println("冲突了，请在此写冲突处理逻辑！\n" + e.getDetailedMessage());
                }

                System.out.println("索引异常: " + e.getDetailedMessage());
            }

            //5、处理响应
            if (indexResponse != null) {
                String index = indexResponse.getIndex();
                String type = indexResponse.getType();
                String id = indexResponse.getId();
                long version = indexResponse.getVersion();
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.println("新增文档成功，处理逻辑代码写到这里。");
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    System.out.println("修改文档成功，处理逻辑代码写到这里。");
                }
                // 分片处理信息
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {

                }
                // 如果有分片副本失败，可以获得失败原因信息
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        String reason = failure.reason();
                        System.out.println("副本失败原因：" + reason);
                    }
                }
            }
        }
    }
}
