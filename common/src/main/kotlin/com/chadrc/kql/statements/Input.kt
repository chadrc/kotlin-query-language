package com.chadrc.kql.statements

import kotlin.reflect.KClass

class Input<T : Any>(val name: String, kClass: KClass<T>)

inline fun <reified T : Any> input(name: String) = Input(name, T::class)