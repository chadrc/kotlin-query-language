package com.chadrc.kql

import com.chadrc.kql.models.Author
import com.chadrc.kql.models.Post
import com.chadrc.kql.utils.PropertyMappings
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

actual fun setup() {
    val postMappings = ArrayList<Pair<KProperty<*>, KClass<*>>>()
    postMappings.add(Post::author to Author::class)
    PropertyMappings.mappings[Post::class] = postMappings
}