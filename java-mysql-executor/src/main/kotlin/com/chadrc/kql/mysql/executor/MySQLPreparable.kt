package com.chadrc.kql.mysql.executor

import kotlin.reflect.KProperty

interface MySQLPreparable {
    val queryString: String
    val params: List<KProperty<*>>
}