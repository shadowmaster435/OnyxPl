package org.shadowmaster435.classes

import org.shadowmaster435.built_ins.OnyxPackages
import org.shadowmaster435.code.OnyxCodeBlock
import org.shadowmaster435.code.OnyxFunction
import org.shadowmaster435.code.fields.OnyxField
import org.shadowmaster435.impl.DataProvider
import org.shadowmaster435.impl.OnyxMember
import org.shadowmaster435.impl.OnyxType
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.memory.OnyxClassInstance
import org.shadowmaster435.misc.OnyxModifiers
import org.shadowmaster435.misc.OnyxPackage

val anyType = OnyxType("Any", OnyxPackages.onyxPrimitives, listOf(), size = -1)
open class OnyxClass(
    val modifiers: OnyxModifiers,
    name: String, val pkg: OnyxPackage,
    val constructorMembers: List<OnyxMember> = listOf(),
    codeBlock: OnyxCodeBlock? = null,
    val supertypes: List<OnyxType> = listOf(),
    val type: OnyxType = OnyxType(name, pkg, supertypes, fun(): Int {
        var size = codeBlock?.size ?: 0
        var usesAbstractTypes = false
        constructorMembers.forEach {
            if (it is OnyxField) {
                if (it.type.size == -1) usesAbstractTypes = true
                else size += it.type.size
            }
        }
        return size * if (usesAbstractTypes) -1 else 1
    }.invoke())
): OnyxMember {
    override var initialized: Boolean = false
    var codeBlock = codeBlock; internal set
    internal fun staticInit() {
        pkg.addClass(this)
    }
    override fun initialize(namedScopeMembers: HashMap<String, OnyxMember>) {
        if (!initialized) {
            codeBlock?.initialize(namedScopeMembers)
            initialized = true
        }
    }
    private fun accessCheck(modifiers: OnyxModifiers, accessType: AccessType) = when(accessType) {
        AccessType.PUBLIC -> modifiers.isPublic
        AccessType.PROTECTED -> modifiers.isProtected
        AccessType.PRIVATE -> modifiers.isPrivate
        AccessType.PACKAGE -> modifiers.isPackage
    }

    fun memberFields(accessType: AccessType): List<OnyxField> {
        return buildList {
            codeBlock?.members?.forEach {
                if (it is OnyxField) if (accessCheck(it.modifiers, accessType) && !it.modifiers.isStatic) {
                    add(it)
                }
            }
        } + buildList { constructorMembers.forEach {
            if (it is OnyxField) if (accessCheck(it.modifiers, accessType)) add(it)
        } }
    }


    fun memberFunctions(accessType: AccessType): List<OnyxFunction> {
        return buildList {
            codeBlock?.members?.forEach {
                if (it is OnyxFunction) if (when(accessType) {
                        AccessType.PUBLIC -> it.modifiers.isPublic
                        AccessType.PROTECTED -> it.modifiers.isProtected
                        AccessType.PRIVATE -> it.modifiers.isPrivate
                        AccessType.PACKAGE -> it.modifiers.isPackage
                    }  && !it.modifiers.isStatic) {
                    add(it)
                }
            }
        }
    }

    override fun instantiate(thisInstance: DataProvider?, vararg params: DataProvider): OnyxMember? {
        return OnyxClassInstance(this, (codeBlock?.members ?: listOf()) + constructorMembers)
    }

    override fun toString() = "package ${type.pkg}\n\n${run{
        val str = modifiers.toString()
        if (str.isEmpty()) "" else "$str "
    }}class $type${
        run { 
            if (supertypes.isEmpty()) ""
            else {
                var str = ": "
                supertypes.forEach { 
                    str += if (it == supertypes.last()) it.name else "${it.name}, "
                }
                str
            }
        }
    } ${if (codeBlock != null) "{\n$codeBlock}" else ""}"

}