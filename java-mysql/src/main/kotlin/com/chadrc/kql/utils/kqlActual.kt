package com.chadrc.kql.utils

import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

actual fun <T : Any> KClass<T>.zeroParamConstructors(): Collection<KFunction<T>> =
        this.constructors.filter { it.parameters.isEmpty() || it.parameters.all { it.isOptional } }

actual fun getMembers(kClass: KClass<*>): Collection<KCallable<*>> = kClass.members