package com.chadrc.kql.exceptions

import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class LeftPropOperandNotOnQueryClass(prop: KProperty<*>, kClass: KClass<*>) :
        Exception("Property ${prop.name} is not apart of class ${kClass.simpleName}")