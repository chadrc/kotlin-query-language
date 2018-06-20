package com.chadrc.kql.utils

import kotlin.reflect.*

class MockProperty(private val _name: String) : KProperty<Any> {
    override val name: String
        get() = _name

    // Don't need reset
    override val annotations: List<Annotation>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isAbstract: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isFinal: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isOpen: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val parameters: List<KParameter>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val returnType: KType
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val typeParameters: List<KTypeParameter>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val visibility: KVisibility?
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun call(vararg args: Any?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun callBy(args: Map<KParameter, Any?>): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val getter: KProperty.Getter<Any>
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isConst: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    override val isLateinit: Boolean
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
}