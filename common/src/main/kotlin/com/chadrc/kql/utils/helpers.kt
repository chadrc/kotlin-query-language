package com.chadrc.kql.utils

//import com.chadrc.kql.exceptions.NoStubConstructorException
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.KFunction

expect fun <T : Any> KClass<T>.zeroParamConstructors(): Collection<KFunction<T>>
expect fun getMembers(kClass: KClass<*>): Collection<KCallable<*>>

//fun <T : Any> KClass<T>.zeroParamConstructors() =
//        this.constructors.filter { it.parameters.isEmpty() || it.parameters.all { it.isOptional } }

//fun <T : Any> KClass<T>.stubInstanceAction(action: (it: T) -> Unit) {
//    val stubConstructor = this.zeroParamConstructors().firstOrNull() ?: throw NoStubConstructorException()
//    action(stubConstructor.callBy(HashMap()))
//}