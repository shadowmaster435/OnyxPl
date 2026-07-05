package org.shadowmaster435.impl

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.misc.OnyxPackage
import kotlin.jvm.Throws

open class OnyxType(val name: String, val pkg: OnyxPackage, val supertypes: List<OnyxType> = listOf(), val size: Int) {
    open val polymorphic = false
    open val variadicPolymorphic = false
    override fun toString(): String {
        return name
    }

    fun qualifiedName()  = "$pkg.$name"

    fun castableTo(type: OnyxType): Boolean {
        if (this === type) return true
        type.supertypes.forEach {
            return if (it == type) true
            else it.castableTo(type)
        }
        return false
    }
}