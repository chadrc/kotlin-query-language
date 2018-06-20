package com.chadrc.kql.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

expect fun getMembers(kClass: KClass<*>): Collection<KCallable<*>>
expect fun returnTypeOfProp(type: KProperty<*>): KClass<*>?

expect fun classHasProperty(kClass: KClass<*>, kProperty: KProperty<*>): Boolean