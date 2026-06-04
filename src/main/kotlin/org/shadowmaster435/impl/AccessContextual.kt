package org.shadowmaster435.impl

import org.shadowmaster435.impl.enums.AccessContext
import org.shadowmaster435.impl.enums.AccessType

interface AccessContextual {
    val accessType: AccessType
    val static: Boolean

    fun accessible(context: AccessContext): Boolean {
        val s = context == AccessContext.STATIC_INSTANCE
        val c = context == AccessContext.CLASS
        val ic = context == AccessContext.INNER_CLASS
        val sc = context == AccessContext.SUB_CLASS
        val spc = context == AccessContext.SUPER_CLASS
        return when(accessType) {
            AccessType.PUBLIC -> if (static) true else !s
            AccessType.PROTECTED -> if (static) sc || spc || c || ic else spc || ic || c
            AccessType.PRIVATE -> ic || c
        }
    }

}