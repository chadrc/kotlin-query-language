package com.chadrc.kql.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

actual fun getMembers(kClass: KClass<*>): Collection<KCallable<*>> = kClass.members

@Suppress("UNUSED")
actual fun <T : Any> KClass<T>.returnTypeOfProp(type: KProperty<*>): KClass<*>? {
    return type.returnType.classifier as? KClass<*>
}

actual fun classHasProperty(kClass: KClass<*>, kProperty: KProperty<*>): Boolean {
    return getMembers(kClass).contains(kProperty)
}