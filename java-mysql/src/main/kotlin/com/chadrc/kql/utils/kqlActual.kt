package com.chadrc.kql.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

actual fun getMembers(kClass: KClass<*>): Collection<KCallable<*>> = kClass.members

actual fun returnTypeOfProp(type: KProperty<*>): KClass<*>? {
    return type.returnType.classifier as? KClass<*>
}