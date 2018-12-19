package com.xiaomi.chen.es.domain

/**
 * Created by Wei Chen on 15:32 2018/12/19.
 * 产品可能属于多个分类
 */

class Product{

    var id =0

    var productName=""

    var productId = 0

    var desc = ""

    var price = 0.0

    var categorys = listOf<Category>()
}