package kql.utils

import kql.exceptions.NoStubConstructorException
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.zeroParamConstructors() =
        this.constructors.filter { it.parameters.isEmpty() || it.parameters.all { it.isOptional } }

fun <T : Any> KClass<T>.stubInstanceAction(action: (it: T) -> Unit) {
    val stubConstructor = this.zeroParamConstructors().firstOrNull() ?: throw NoStubConstructorException()
    action(stubConstructor.callBy(HashMap()))
}