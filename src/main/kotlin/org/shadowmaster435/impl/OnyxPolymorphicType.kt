package org.shadowmaster435.impl

import org.shadowmaster435.classes.anyType
import org.shadowmaster435.misc.OnyxPackage

class OnyxPolymorphicType(name: String, pkg: OnyxPackage, supertypes: List<OnyxType> = listOf(), size: Int, val polymorphicTypes: Map<String, OnyxType>) : OnyxType(name, pkg, supertypes, size) {
    override val polymorphic = true
    fun of(vararg types: Pair<String, OnyxType>): OnyxPolymorphicType {
        val map = buildMap {
            types.forEach {
                val type = polymorphicTypes[it.first] ?: throw RuntimeException("Missing subtypes for polymorphic type $name")
                if (it.second.castableTo(type)) {
                    put(it.first, it.second)
                } else throw RuntimeException("Incompatible type $name with ${type.name}")
            }
        }
        return OnyxPolymorphicType(name, pkg, supertypes, size, map)
    }
    override fun toString(): String {
        return "$name<${ run {
            var str = ""
            var i = 0
            polymorphicTypes.forEach {
                str += if (it.value != anyType)
                    "${it.key}: ${it.value}"
                else
                    it.key
                if (i < polymorphicTypes.size - 1) str += ", "
                i++
            }
        }}>"
    }
}
