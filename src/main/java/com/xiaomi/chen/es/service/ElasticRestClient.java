package com.xiaomi.chen.es.service;

import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import javax.annotation.Resource;

/**
 * @author chenwei
 * @version 1.0
 * @date 2018/12/19
 * @description
 */
@Resource
public class ElasticRestClient {

    @Resource
    private RestHighLevelClient restHighLevelClient;



    public void createIndex(){
        // 1、创建 创建索引request 参数：索引名mess
        CreateIndexRequest request = new CreateIndexRequest("mess");

        // 2、设置索引的settings
        request.settings(Settings.builder().put("index.number_of_shards", 3) // 分片数
                .put("index.number_of_replicas", 2) // 副本数
                .put("analysis.analyzer.default.tokenizer", "ik") // 默认分词器
        );

        // 3、设置索引的mappings
        request.mapping("_doc",
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
        request.alias(new Alias("mmm"));

        // 5、 发送请求
        // 5.1 同步方式发送请求
//        CreateIndexResponse createIndexResponse = restHighLevelClient.index(request, null)
//                .indices().(request);

//        // 6、处理响应
//        boolean acknowledged = createIndexResponse.isAcknowledged();
//        System.out.println("acknowledged = " + acknowledged);
    }
}
