package com.chadrc.kql.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

expect fun <T : Any> KClass<T>.zeroParamConstructors(): Collection<KFunction<T>>
expect fun getMembers(kClass: KClass<*>): Collection<KCallable<*>>
expect fun returnTypeOfProp(type: KProperty<*>): KClass<*>?