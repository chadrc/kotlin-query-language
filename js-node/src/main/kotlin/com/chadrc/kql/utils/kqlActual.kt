package com.chadrc.kql.utils

import kotlinext.js.Object
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

actual fun getMembers(kClass: KClass<*>): Collection<KCallable<*>> {
    val props = ArrayList<KCallable<*>>()
    // Create an instance and get its keys
    // We'll treat any key that doesn't begin with an '_' as a member
    val c: dynamic = kClass.js
    val instance = js("new c()")
    val keys = Object.keys(instance as Any)
    for (key in keys) {
        if (!key.startsWith("_")) {
            props.add(MockProperty(key))
        }
    }

    // Using prototype of js class
    // Any property that has a 'get' in its descriptor will be a member
    val prototype = c.prototype
    val jsProps = Object.getOwnPropertyNames(prototype as Any)
    for (prop in jsProps) {
        val descriptor = Object.getOwnPropertyDescriptor<Any>(prototype, prop)
        if (descriptor.get != undefined) {
            props.add(MockProperty(prop))
        }
    }

    return props.toList()
}

actual fun returnTypeOfProp(type: KProperty<*>): KClass<*>? {
    return Any::class
}

actual fun classHasProperty(kClass: KClass<*>, kProperty: KProperty<*>): Boolean {
    // Need to check by name, cause kProperty is likely a native impl
    // and getMembers returns MockProperty instances
    return getMembers(kClass).find { it.name == kProperty.name } != null
}