package org.shadowmaster435.classes

import org.shadowmaster435.built_ins.OnyxPackages
import org.shadowmaster435.code.OnyxCodeBlock
import org.shadowmaster435.code.OnyxFunction
import org.shadowmaster435.code.fields.OnyxField
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.memory.OnyxClassInstance
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.misc.OnyxPackage

val anyType = OnyxType("Any", OnyxPackages.onyxPrimitives, listOf())
open class OnyxClass(
    val modifiers: OnyxModifiers,
    name: String, pkg: OnyxPackage,
    val constructorMembers: List<OnyxMember> = listOf(),
    val codeBlock: OnyxCodeBlock? = null,
    supertypes: List<OnyxType> = listOf(),
    val type: OnyxType = OnyxType(name, pkg, supertypes + listOf(anyType))
): OnyxMember {
    override var initialized: Boolean = false
    var castType: OnyxType = type; private set

    private val initSequence = listOf(
        OnyxFunction::class.java,
        OnyxField::class.java
    )

    open fun cast(clazz: OnyxClass) {
        if (clazz.type == type) return
        else if (type.castableTo(clazz.type)) castType = clazz.type
    }


    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            codeBlock?.initialize(namedScopeMembers)
            initialized = true
        }
    }

    override fun instantiate(vararg params: DataProvider): OnyxMember? {
        return OnyxClassInstance(this, (codeBlock?.members ?: listOf()) + constructorMembers)
    }

}