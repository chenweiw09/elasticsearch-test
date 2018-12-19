package com.xiaomi.chen.es.core

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

import java.net.InetAddress
import java.net.UnknownHostException

/**
 * @author chenwei
 * @version 1.0
 * @date 2018/12/19
 * @description
 */
@Configuration
open class EsClient {

    @Value("\${es.ip}")
    private val esIp: String? = null

    @Value("\${es.port}")
    private val port: Int = 0

    @Value("{es.cluster.name}")
    private val esClusterName: String? = null

    @Value("{es.node.name}")
    private val esNodeName: String? = null

    @Bean
    @Throws(UnknownHostException::class)
    open fun client(): TransportClient {
        val transportAddress = InetSocketTransportAddress(InetAddress.getByName(esIp), port)

        val settings = Settings.builder().put(esClusterName, esNodeName).build()
        return PreBuiltTransportClient(settings).let {
            it.addTransportAddress(transportAddress)
            it
        }
    }

    @Bean
    open fun restHighLevelClient(): RestHighLevelClient {
        return RestHighLevelClient(RestClient.builder(
                HttpHost(esIp, port,"http")
        ))
    }

}
