package com.chadrc.kql.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

actual fun getMembers(kClass: KClass<*>): Collection<KCallable<*>> {
    return ArrayList()
}

actual fun returnTypeOfProp(type: KProperty<*>): KClass<*>? {
    return Any::class
}