package org.shadowmaster435.misc

import org.shadowmaster435.impl.CodeObject
import org.shadowmaster435.impl.Modifier
import org.shadowmaster435.impl.enums.CodeObjType

class OnyxModifiers(val mods: List<Modifier>) : CodeObject<List<Modifier>> {
    override val objType: CodeObjType = CodeObjType.DATA
    override fun toString(): String {
        var str = ""
        for (mod in mods) {
            val name = mod::class.simpleName!!

            str += name.replace("Modifier", "").lowercase() + if (mod === mods.last()) "" else " "
        }
        return str
    }
}