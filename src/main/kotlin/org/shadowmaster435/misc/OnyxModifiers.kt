package org.shadowmaster435.misc

import org.shadowmaster435.classes.OnyxClass
import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.enums.AccessType
import org.shadowmaster435.impl.enums.CodeObjType
import org.shadowmaster435.impl.enums.ModifierScope
import org.shadowmaster435.modifiers.AbstractModifier
import org.shadowmaster435.modifiers.MetaModifier
import org.shadowmaster435.modifiers.PackageModifier
import org.shadowmaster435.modifiers.PrivateModifier
import org.shadowmaster435.modifiers.ProtectedModifier
import org.shadowmaster435.modifiers.PublicModifier
import org.shadowmaster435.modifiers.StaticModifier

class OnyxModifiers(val mods: List<Modifier>) : CodeObject {
    constructor(vararg mods: Modifier): this(mods.toList())

    override val objType: CodeObjType = CodeObjType.DATA
    val isMeta = mods.contains(MetaModifier)
    val isAbstract = mods.contains(AbstractModifier)
    val isPrivate = mods.contains(PrivateModifier)
    val isProtected = mods.contains(ProtectedModifier)
    val isPackage = mods.contains(PackageModifier)
    val isPublic = mods.contains(PublicModifier)
    val isStatic = mods.contains(StaticModifier)

    fun accessPermits(isInstance: Boolean, from: OnyxClass, to: OnyxClass): Boolean {
        val a = if (isPublic) true
        else if (isProtected) from.type.castableTo(to.type) && from == to
        else if (isPackage) from.type.pkg == to.type.pkg
        else if (isPrivate) from == to
        else false


        return if (a) {
            if (isStatic) true
            else isInstance
        } else false
    }

    override fun toString(): String {
        var str = ""
        for (mod in mods) {
            if (mod is PublicModifier) continue
            val name = mod::class.simpleName!!

            str += name.replace("Modifier", "").lowercase() + if (mod === mods.last()) "" else " "
        }
        return str
    }
    companion object {
    }
}