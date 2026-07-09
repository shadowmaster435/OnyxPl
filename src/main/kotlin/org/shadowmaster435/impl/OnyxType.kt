package org.shadowmaster435.impl

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.misc.OnyxPackage
import kotlin.jvm.Throws

open class OnyxType private constructor(val name: String, val pkg: OnyxPackage, val supertypes: List<OnyxType> = listOf(), val size: Int, val nullable: Boolean) {
    open val polymorphic = false
    open val variadicPolymorphic = false
    override fun toString(): String {
        return name + if (nullable) "?" else ""
    }

    constructor(name: String, pkg: OnyxPackage, supertypes: List<OnyxType> = listOf(), size: Int) : this(name, pkg, supertypes, size, false)

    val nullableType get() = OnyxType(name, pkg, supertypes, size, true)
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