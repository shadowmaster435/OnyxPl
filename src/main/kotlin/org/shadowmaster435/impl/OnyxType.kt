package org.shadowmaster435.impl

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.misc.OnyxPackage
import kotlin.jvm.Throws

open class OnyxType(val name: String, val pkg: OnyxPackage, val supertypes: List<OnyxType> = listOf()) {
    fun castableTo(type: OnyxType): Boolean {
        type.supertypes.forEach {
            return if (it == type) true
            else it.castableTo(type)
        }
        return false
    }
}