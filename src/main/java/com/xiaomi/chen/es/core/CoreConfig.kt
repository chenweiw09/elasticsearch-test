package com.xiaomi.chen.es.core

import com.fasterxml.jackson.databind.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

/**
 * Created by Wei Chen on 15:14 2018/12/19.
 */

@Configuration
open class CoreConfig{

    @Bean
    open fun mappingJackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter(objectMapper())
    }

    @Bean
    open fun objectMapper() : ObjectMapper{

        return ObjectMapper().let {
            // 禁用空对象转换json校验
            it.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)

            it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 只有当set和get同时存在的时候，才进行json化，避免idl文件生成的很多不需要的属性
            it.configure(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS, true)
            // 所有参数输出都是下划线形式
            it.propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            it.propertyNamingStrategy = PropertyNamingStrategy.SnakeCaseStrategy()

            it
        }
    }


}