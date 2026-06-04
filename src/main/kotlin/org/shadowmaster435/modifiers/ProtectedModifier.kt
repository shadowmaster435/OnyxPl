package org.shadowmaster435.modifiers

import org.shadowmaster435.impl.abstracts.AccessModifier
import org.shadowmaster435.impl.enums.AccessType

object ProtectedModifier : AccessModifier() {
    override val accessType = AccessType.PROTECTED
    override val chainableWith = listOf(StaticModifier::class.java, AbstractModifier::class.java, FinalModifier::class.java)
}