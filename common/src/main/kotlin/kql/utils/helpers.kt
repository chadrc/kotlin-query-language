package kql.utils

import kql.exceptions.NoStubConstructorException
import kotlin.reflect.KClass

fun <T : Any> KClass<T>.zeroParamConstructors() =
        this.constructors.filter { it.parameters.isEmpty() || it.parameters.all { it.isOptional } }

fun <T : Any> KClass<T>.firstZeroParamConstructor() = try {
    this.zeroParamConstructors().first()
} catch (exception: NoSuchElementException) {
    null
}

fun <T : Any> KClass<T>.stubInstanceAction(action: (it: T) -> Unit) {
    val stubConstructor = this.firstZeroParamConstructor() ?: throw NoStubConstructorException()
    action(stubConstructor.callBy(HashMap()))
}

inline fun <reified T : Any> stubInstanceAction(noinline action: (it: T) -> Unit) {
    T::class.stubInstanceAction(action)
}