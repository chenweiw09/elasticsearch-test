package com.xiaomi.chen.es.service

import com.xiaomi.chen.es.domain.Product
import com.xiaomi.chen.es.utils.JsonUtil
import lombok.extern.slf4j.Slf4j
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.annotation.Resource


/**
 * @author chenwei
 * @version 1.0
 * @date 2018/12/19
 * @description  使用java客户端操作索引
 */
@Slf4j
@Service
class ElasticJavaClient(@Resource private val client: TransportClient) {


    /**
     * 创建一个索引，并且创建文档
     */
    open fun addIndex(list: List<Product>) {

        val json = JsonUtil.toJSONString(list)
        val response = client.prepareIndex(ES_INDEX, ES_TYPE).setSource(json).get()
        log.info(response.toString())

        //创建成功 反会的状态码是201
        if (response.status().status == DOCUMENT_CODE) {
            log.info(response.id)
        }
    }


    fun getIndex(id: String) {
        var getResponse = client.prepareGet(ES_INDEX, ES_TYPE, id).get()

        //得到的是Map类型的结果
        val map = getResponse.getSource()
        map.forEach { k, v -> println("key:value = $k:$v") }
    }


    fun updateIndex(id: String, content: String) {
        val updateRequest = UpdateRequest(ES_INDEX, ES_TYPE, id).doc(content)
        val response = client.update(updateRequest).get()

        if (response.status().getStatus() == 200) {
            System.out.println("更新成功")
        }
    }


    fun delIndex(id: String) {
        val response = client.prepareDelete(ES_INDEX, ES_TYPE, id).get()
        response?.let {
            if (it.status().status == 200) {
                System.out.println("删除成功")
            }
        }
    }


    // 设置查询类型
    // 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询
    // 2.SearchType.SCAN = 扫描查询,无序
    // 3.SearchType.COUNT = 不设置的话,这个为默认值,还有的自己去试试吧
    private fun querySearch(index: String, type: String, term: String, queryString: String) {
        val response = client.prepareSearch(index).let {
            it.setTypes(type)
            it.setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
            it.setQuery(QueryBuilders.matchQuery(term, queryString))
            // 设置查询关键词
            it.highlighter(HighlightBuilder().field(term).preTags("<em>").postTags("</em>"))
            // 设置查询数据的位置,分页用
            it.setFrom(0)
            // 设置查询结果集的最大条数
            it.setSize(60)
            // 设置是否按查询匹配度排序
            it.setExplain(true)
            it.execute().actionGet()
        }


        val searchHits = response.getHits()
        println("-----------------在[$term]中搜索关键字[$queryString]---------------------")
        println("共匹配到:" + searchHits.getTotalHits() + "条记录!")
        val hits = searchHits.getHits()
        for (searchHit in hits) {
            //获取高亮的字段
            val highlightFields = searchHit.getHighlightFields()
            val highlightField = highlightFields.get(term)
            println("高亮字段:" + highlightField?.getName() + "\n高亮部分内容:" + highlightField!!.getFragments()[0].string())
            val sourceAsMap = searchHit.sourceAsMap()
            val keySet = sourceAsMap.keys
            for (string in keySet) {
                //key value 值对应关系
                println(string + ":" + sourceAsMap.get(string))
            }
            println()
        }
    }

    companion object {
        private val DOCUMENT_CODE = 201
        private val log = LoggerFactory.getLogger(ElasticJavaClient::class.java)

        private val ES_INDEX = "product"

        private val ES_TYPE = "product_plan"
    }

}
